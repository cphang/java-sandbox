package org.xin.refactoring.user;

public class UserFactory {

  static int id = 0;

  public static User create(String string) {
    return new UserImpl(Integer.toString(id), string);
  }
}
