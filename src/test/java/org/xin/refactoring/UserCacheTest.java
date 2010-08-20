package org.xin.refactoring;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.xin.refactoring.user.User;
import org.xin.refactoring.user.UserCache;
import org.xin.refactoring.user.UserCacheImpl;
import org.xin.refactoring.user.UserFactory;

public class UserCacheTest {

  private final UserCache cache = new UserCacheImpl();

  @Before
  public void setUp() {
    final User newUser = UserFactory.create("John");
    cache.store(newUser);

  }

  @Test
  public void ShouldReturnUserIfIdIsGiven() {
    User user = cache.findById("0");
    assertEquals("0", user.getId());
    assertEquals("John", user.getName());
  }

  @Test
  public void ShouldReturnUserIfIdIsGiven2() {
    User user = cache.findById("0");

  }
}
