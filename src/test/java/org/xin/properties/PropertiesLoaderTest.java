package org.xin.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoaderTest {

  private final static Logger log = LoggerFactory.getLogger(PropertiesLoaderTest.class);
  private final PropertyLoader loader = new PropertyLoaderImpl();

  @Test
  public void shouldReadUserHomePath() {
    final String property = System.getProperty("user.home");
    assertNotNull(property);
  }

  @Test
  public void shouldLoadPropertyFileOutsideWarArchieve()
      throws FileNotFoundException, IOException {

    final Properties properties = loader.loadProperty();

    final int size = properties.size();
    assertTrue(size > 0);

    final String property = properties.getProperty("key2");
    assertEquals(property, "value2");

    log.info("value: " + property);
  }

  @Test
  public void shouldStorePropertyFileOutsideWarArchive()
      throws FileNotFoundException, IOException {
    final Properties properties = loader.loadProperty();

    properties.setProperty("key3", "value3");

    loader.store(properties);
    loader.loadProperty();
    assertTrue(loader.get("key2").equals("value2"));
  }
}
