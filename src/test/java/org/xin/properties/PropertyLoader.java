package org.xin.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public interface PropertyLoader {

  Properties loadProperty() throws FileNotFoundException, IOException;

  void store(Properties properties) throws FileNotFoundException, IOException;

  String get(String string);

}
