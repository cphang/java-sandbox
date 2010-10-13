package org.xin.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoaderTest {

  private final static Logger log = LoggerFactory.getLogger(PropertiesLoaderTest.class);

  @Test
  public void shouldReadUserHomePath() {
    final String property = System.getProperty("user.home");
    assertNotNull(property);
  }

  @Test
  public void shouldLoadPropertyFileOutsideWarArchieve()
      throws FileNotFoundException, IOException {
    final String userCurrentPath = System.getProperty("user.dir");
    log.info("user.home: " + userCurrentPath);

    final String fileToLoad = "test.properties";
    final String absolutePath = userCurrentPath
        + System.getProperty("file.separator") + fileToLoad;
    final File file = new File(absolutePath);
    assertNotNull("can not load " + fileToLoad, file);

    final Properties properties = new Properties();
    properties.load(new FileInputStream(file));

    final int size = properties.size();
    assertTrue(size == 2);

    final String property = properties.getProperty("key0");
    assertEquals(property, "value0");
  }
}
