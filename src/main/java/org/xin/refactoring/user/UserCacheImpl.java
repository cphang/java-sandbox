package org.xin.refactoring.user;

import java.util.HashMap;
import java.util.Map;

public class UserCacheImpl implements UserCache {

  private final Map<String, User> cache = new HashMap<String, User>();

  public User findById(String id) {
    return cache.get(id);
  }

  public User findByName(String name) {
    return cache.get(name);
  }

  public void store(User newUser) {
    cache.put(newUser.getId(), newUser);
    cache.put(newUser.getName(), newUser);
  }
}
