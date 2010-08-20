package org.xin.refactoring.user;

public interface UserCache {

  User findById(String string);

  void store(User newUser);

}
