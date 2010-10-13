package org.xin.dropbox;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class DropboxServiceTest {

  private static final String FOO_TXT = "foo.txt";
  private final String DIRECTORY_TO_WATCH = "/Users/bender/gtd/projects/directory-watcher/dropbox";
  private final File dirToWatch = new File(DIRECTORY_TO_WATCH);
  private final Notifier notifier = new NotifierImpl();

  @Test
  public void shouldSendTheFileNameToTheServerIfANewFileIsCreatedInDirectory()
      throws IOException, InterruptedException {
    // Given
    if (fileFooExistInTarget()) {
      deleteIt();
    }
    assertTrue("File " + FOO_TXT + " still exsist", !fileFooExistInTarget());

    createWatcherForTarget();

    // When
    createNewFileInTarget();
    assertTrue("File " + FOO_TXT + " does not exsist", fileFooExistInTarget());

    // assert
    assertTrue("Should get foo.txt as message", notifier.getMessage());
  }

  private void createNewFileInTarget() throws IOException {
    new File(DIRECTORY_TO_WATCH + "/" + FOO_TXT).createNewFile();
  }

  private void deleteIt() {
    new File(DIRECTORY_TO_WATCH + "/" + FOO_TXT).delete();
  }

  private boolean fileFooExistInTarget() {
    final File[] listFiles = dirToWatch.listFiles();
    for (final File filename : listFiles) {
      if (filename.getName().equals(FOO_TXT))
        return true;
    }
    return false;
  }

  private void createWatcherForTarget() throws IOException,
      InterruptedException {
    final DropboxService service = new DropboxServiceImpl();
    service.watch(DIRECTORY_TO_WATCH, notifier);
    service.start();
  }
}
