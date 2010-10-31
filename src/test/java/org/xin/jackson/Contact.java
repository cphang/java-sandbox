package org.xin.jackson;

import java.io.Serializable;

public interface Contact extends Serializable {
  String name();

  int age();
}
