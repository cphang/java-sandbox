/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2008 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
package org.xin.jna;

import java.io.IOException;

/**
 * Native file system notification service.
 * @author Petr Nejedly
 * @author Tomas Zezula
 */
public abstract class Notify {
    
    public static enum EventKind { CREATE, DELETE, MODIFY, OVERFLOW };

    /**
     * Represents a Notify service registration
     * which can be used to cancel the registration
     * or or identify events.
     */
    public static abstract class Key {
        /**
         * Check validity of the key.
         * The key is valid until it's canceled
         * @return true if the Key is valid
         */
        public abstract boolean isValid();
        
        /**
         * Cancels the key. The Notify service
         * will no more track changes in folder
         * corresponding to registration key and
         * the key becomes invalid.
         */
        public abstract void cancel();
    }

    /**
     * An event in file system fired by the OS
     */
    public static final class Event {
        
        private final Key key;
        private final EventKind kind;
        private final String name;
        
        private Event(final Key key, final EventKind kind, final String name) {
            this.key = key;
            this.kind = kind;
            this.name = name;
        }
        
        /**
         * Returns the key for which the event happened
         * @return the registration key
         */
        public Key getKey() {
            return key;
        }
        
        /**
         * Returns the kind of the change.
         * @return the kind
         */
        public EventKind getKind() {
            return kind;
        }
        
        /**
         * Returns the name of folder in which change occurred
         * todo: Rename to getPath()
         * @return the path
         */
        public String getName() {
            return name;
        }
    }

    protected final Event createEvent(final Key key, final EventKind kind, final String name) {
      return new Event(key, kind, name);
    }
    
    /*
     * Adds the folder for listeing.
     * The listeinig is non recursive
     * @param String path to be watched
     * @param mask the events in which is client interested (may be ignored on some OSs)
     * 
     */
    public abstract Key addWatch(String path, EventKind ... mask);
        
    
    /**
     * Returns a file system event when available otherwise blocks caller
     * until the event is not available.
     * @return the event
     * @throws IOException signals native notifier failure
     * @throws InterruptedException when caller is interrupted
     */
    public abstract Event nextEvent() throws IOException, InterruptedException;
  }