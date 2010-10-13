/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 * 
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 * 
 * The contents of this file are subject to the terms of either the GNU General
 * Public License Version 2 only ("GPL") or the Common Development and
 * Distribution License("CDDL") (collectively, the "License"). You may not use
 * this file except in compliance with the License. You can obtain a copy of the
 * License at http://www.netbeans.org/cddl-gplv2.html or
 * nbbuild/licenses/CDDL-GPL-2-CP. See the License for the specific language
 * governing permissions and limitations under the License. When distributing
 * the software, include this License Header Notice in each file and include the
 * License file at nbbuild/licenses/CDDL-GPL-2-CP. Oracle designates this
 * particular file as subject to the "Classpath" exception as provided by Oracle
 * in the GPL Version 2 section of the License file that accompanied this code.
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2008 Sun
 * Microsystems, Inc. All Rights Reserved.
 * 
 * If you wish your version of this file to be governed by only the CDDL or only
 * the GPL Version 2, indicate your decision by adding "[Contributor] elects to
 * include this software in this distribution under the [CDDL or GPL Version 2]
 * license." If you do not indicate a single choice of license, a recipient has
 * the option to distribute your version of this file under either the CDDL, the
 * GPL Version 2 or to extend the choice of license to its licensees as provided
 * above. However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is made
 * subject to such option by the copyright holder.
 */
package org.xin.jna;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Tomas Zezula
 */
public final class FileSystemWatcher extends Notify {

  private static final Level DEBUG_LOG_LEVEL = Level.FINE;
  private static final Level PERF_LOG_LEVEL = Level.FINE;
  private static final long kFSEventStreamEventIdSinceNow = 0xFFFFFFFFFFFFFFFFL;
  private static final int kFSEventStreamCreateFlagNoDefer = 0x00000002;
  private static final int kFSEventStreamEventFlagMustScanSubDirs = 0x00000001;
  private static final int kFSEventStreamEventFlagMount = 0x00000040;
  private static final int kFSEventStreamEventFlagUnmount = 0x00000080;
  private static final double LATENCY = 1.0f;
  private static final int ENC_MAC_ROMAN = 0;
  private static final String DEFAULT_RUN_LOOP_MODE = "kCFRunLoopDefaultMode"; // NOI18N
  private static final Logger LOG = Logger.getLogger(FileSystemWatcher.class.getName());
  private final CoreFoundation cf;
  private final CoreServices cs;
  private final EventCallback callback;
  private final BlockingQueue<Event> events;
  private final ConcurrentHashMap<String, Key> listensOn;
  // @GuardedBy("this")
  private ExecutorService worker;
  // @GuardedBy("this")
  private Pointer[] rtData;

  private FileSystemWatcher() {
    cf = (CoreFoundation) Native.loadLibrary("CoreFoundation",
        CoreFoundation.class); // NOI18N
    cs = (CoreServices) Native.loadLibrary("CoreServices", CoreServices.class); // NOI18N
    callback = new EventCallbackImpl();
    events = new LinkedBlockingQueue<Event>();
    listensOn = new ConcurrentHashMap<String, Key>();
  }

  @Override
  public Key addWatch(String path, EventKind... mask) {
    // Mask not supported on MacOS X
    assert path != null;
    assert path.endsWith("/");
    final KeyImpl key = new KeyImpl(path);
    listensOn.put(path, key);
    return key;
  }

  @Override
  public Event nextEvent() throws IOException, InterruptedException {
    return events.take();
  }

  public synchronized void start() throws IOException, InterruptedException {
    if (worker != null) {
      throw new IllegalStateException("FileSystemWatcher already started."); // NOI18N
    }
    worker = Executors.newSingleThreadExecutor(new DaemonThreadFactory());
    final Exchanger<Object> exchanger = new Exchanger<Object>();
    worker.execute(new Runnable() {
      public void run() {
        try {
          Pointer[] _rtData = null;
          try {
            _rtData = createFSEventStream();
          } catch (final Throwable ex) {
            exchanger.exchange(ex);
          } finally {
            if (_rtData != null) {
              exchanger.exchange(_rtData);
              cf.CFRunLoopRun();
            }
          }
        } catch (final InterruptedException ie) {
          LOG.log(Level.WARNING, "Watcher interruped during start", ie); // NOI18N
        }
      }
    });
    final Object _data = exchanger.exchange(null);
    assert _data != null;
    if (_data instanceof Throwable) {
      worker.shutdown();
      worker = null;
      throw new IOException((Throwable) _data);
    } else {
      rtData = (Pointer[]) _data;
    }
  }

  public synchronized void stop() throws IOException {
    if (worker == null) {
      throw new IllegalStateException("FileSystemWatcher is not started."); // NOI18N
    }
    assert rtData != null;
    assert rtData.length == 2;
    assert rtData[0] != null;
    assert rtData[1] != null;
    cs.FSEventStreamStop(rtData[0]);
    cs.FSEventStreamInvalidate(rtData[0]);
    cs.FSEventStreamRelease(rtData[0]);
    cf.CFRunLoopStop(rtData[1]);
    worker.shutdown();
    worker = null;
    rtData = null;
  }

  private Pointer[] createFSEventStream() throws IOException {
    final Pointer root = cf.CFStringCreateWithCString(Pointer.NULL, "/",
        ENC_MAC_ROMAN); // NOI18N
    if (root == Pointer.NULL) {
      throw new IOException("Path creation failed."); // NOI18N
    }
    final Pointer arr = cf.CFArrayCreateMutable(Pointer.NULL,
        new NativeLong(1), Pointer.NULL);
    if (arr == Pointer.NULL) {
      throw new IOException("Path list creation failed."); // NOI18N
    }
    cf.CFArrayAppendValue(arr, root);

    final Pointer eventStream = cs.FSEventStreamCreate(Pointer.NULL, callback,
        Pointer.NULL, arr, kFSEventStreamEventIdSinceNow, LATENCY,
        kFSEventStreamCreateFlagNoDefer);
    if (eventStream == Pointer.NULL) {
      throw new IOException("Creation of FSEventStream failed."); // NOI18N
    }
    final Pointer loop = cf.CFRunLoopGetCurrent();
    final Pointer kCFRunLoopDefaultMode = findDefaultMode(loop);
    if (kCFRunLoopDefaultMode == null) {
      throw new IOException("Caller has no defaul run loop mode."); // NOI18N
    }
    cs.FSEventStreamScheduleWithRunLoop(eventStream, loop,
        kCFRunLoopDefaultMode);
    if (LOG.isLoggable(DEBUG_LOG_LEVEL)) {
      LOG.log(DEBUG_LOG_LEVEL, getStreamDescription(eventStream));
    }
    cs.FSEventStreamStart(eventStream);
    return new Pointer[]{eventStream, loop};
  }

  private Pointer findDefaultMode(final Pointer runLoop) {
    final Pointer modes = cf.CFRunLoopCopyAllModes(runLoop);
    if (modes != Pointer.NULL) {
      final int modesCount = cf.CFArrayGetCount(modes).intValue();
      for (int i = 0; i < modesCount; i++) {
        final Pointer mode = cf.CFArrayGetValueAtIndex(modes, new NativeLong(i));
        if (mode != Pointer.NULL
            && DEFAULT_RUN_LOOP_MODE.equals(cf.CFStringGetCStringPtr(mode,
                ENC_MAC_ROMAN))) {
          return mode;
        }
      }
    }
    return null;
  }

  private String getStreamDescription(final Pointer eventStream) {
    final Pointer desc = cs.FSEventStreamCopyDescription(eventStream);
    return desc == Pointer.NULL ? "" : cf.CFStringGetCStringPtr(desc,
        ENC_MAC_ROMAN); // NOI18N
  }

  public static FileSystemWatcher getDefault() {
    return H.INSTANCE;
  }

  public static interface EventCallback extends Callback {
    void invoke(Pointer streamRef, Pointer clientCallBackInfo,
        NativeLong numEvents, Pointer eventPaths, Pointer eventFlags,
        Pointer eventIds);
  }

  public static interface CoreFoundation extends Library {

    Pointer CFRunLoopGetCurrent();

    void CFRunLoopRun();

    void CFRunLoopStop(Pointer loop);

    Pointer CFRunLoopCopyAllModes(Pointer loop);

    Pointer CFArrayCreateMutable(Pointer allocator, NativeLong size,
        Pointer callback);

    void CFArrayAppendValue(Pointer theArray, Pointer value);

    Pointer CFArrayGetValueAtIndex(Pointer theArray, NativeLong index);

    NativeLong CFArrayGetCount(Pointer theArray);

    Pointer CFStringCreateWithCString(Pointer allocator, String string,
        int encoding);

    String CFStringGetCStringPtr(Pointer theString, int encoding);
  }

  public static interface CoreServices extends Library {

    Pointer FSEventStreamCreate(Pointer allocator, EventCallback callback,
        Pointer ctx, Pointer pathsToWatch, long sinceWhen, double latency,
        int flags);

    Pointer FSEventStreamCopyDescription(Pointer stream);

    void FSEventStreamScheduleWithRunLoop(Pointer stream, Pointer loop,
        Pointer mode);

    void FSEventStreamUnscheduleFromRunLoop(Pointer stream, Pointer loop,
        Pointer mode);

    void FSEventStreamStart(Pointer stream);

    void FSEventStreamStop(Pointer stream);

    void FSEventStreamInvalidate(Pointer stream);

    void FSEventStreamRelease(Pointer stream);
  }

  private static class H {
    final static FileSystemWatcher INSTANCE = new FileSystemWatcher();
  }

  private static class DaemonThreadFactory implements ThreadFactory {
    public Thread newThread(Runnable r) {
      final Thread t = new Thread(r);
      t.setDaemon(true);
      return t;
    }

  }

  private final class KeyImpl extends Key {

    private final String path;
    private volatile boolean canceled;

    private KeyImpl(final String path) {
      assert path != null;
      this.path = path;
    }

    @Override
    public boolean isValid() {
      return !canceled;
    }

    @Override
    public synchronized void cancel() {
      if (!canceled) {
        listensOn.remove(path);
        canceled = true;
      }
    }

    @Override
    public int hashCode() {
      return path.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
      if (!(other instanceof KeyImpl)) {
        return false;
      }
      return this.path.equals(((KeyImpl) other).path);
    }

    @Override
    public String toString() {
      return String.format("%s[%s]", KeyImpl.class.getSimpleName(), path); // NOI18N
    }

    String getPath() {
      return path;
    }
  }

  private class EventCallbackImpl implements EventCallback {
    public void invoke(Pointer streamRef, Pointer clientCallBackInfo,
        NativeLong numEvents, Pointer eventPaths, Pointer eventFlags,
        Pointer eventIds) {
      final long st = System.currentTimeMillis();
      final int length = numEvents.intValue();
      final Pointer[] pointers = eventPaths.getPointerArray(0, length);
      final int[] flags = eventFlags.getIntArray(0, length);
      for (int i = 0; i < length; i++) {
        final Pointer p = pointers[i];
        final int flag = flags[i];
        boolean found = false;
        final String path = p.getString(0);
        final Key key = listensOn.get(path);
        if (key != null) {
          if ((flag & kFSEventStreamEventFlagMustScanSubDirs) == kFSEventStreamEventFlagMustScanSubDirs
              || (flag & kFSEventStreamEventFlagMount) == kFSEventStreamEventFlagMount
              || (flag & kFSEventStreamEventFlagUnmount) == kFSEventStreamEventFlagUnmount) {
            events.add(createEvent(key, EventKind.OVERFLOW, path));
          } else {
            events.add(createEvent(key, EventKind.MODIFY, path));
          }
          found = true;
        }
        LOG.log(DEBUG_LOG_LEVEL, "Event on {0} interesting: {1}", new Object[]{
            path, found});
      }
      LOG.log(PERF_LOG_LEVEL, "Callback time: {0}",
          (System.currentTimeMillis() - st));
    }
  }
}
