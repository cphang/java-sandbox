package org.xin.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class PropertyLoaderImpl implements PropertyLoader {
  private final static Logger log = LoggerFactory.getLogger(PropertyLoaderImpl.class);
  private final Properties properties = new Properties();
  private final String userCurrentPath = System.getProperty("user.dir");
  private final String fileToLoad = "test.properties";
  private final String absolutePath = userCurrentPath
      + System.getProperty("file.separator") + fileToLoad;

  public Properties loadProperty() throws FileNotFoundException, IOException {
    properties.load(new FileInputStream(new File(absolutePath)));
    return properties;
  }

  public void store(Properties properties) throws FileNotFoundException,
      IOException {
    properties.store(new FileOutputStream(new File(absolutePath)),
        new Date().toString());
  }

  public String get(String string) {
    return (String) properties.get(string);
  }
}