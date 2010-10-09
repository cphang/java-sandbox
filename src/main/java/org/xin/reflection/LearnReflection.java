package org.xin.reflection;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class LearnReflection {

  private final static Logger log = LoggerFactory.getLogger(LearnReflection.class);

  @Ignore
  @Test
  public void foo() throws ClassNotFoundException {
    @SuppressWarnings("unchecked")
    Class<String> stringClass = (Class<String>) Class.forName("java.lang.String");
    assertNotNull(stringClass);

    for (Method method : stringClass.getDeclaredMethods()) {
      log.info("method: " + method);
    }
  }

  public enum E {
    A, B
  }

  @Ignore
  @Test
  public void shouldReturnItsClassName() {
    Class<? extends E> a = E.A.getClass();
    log.info("class name: " + a);
  }

  @Test
  public void shouldReturnStringClass() throws ClassNotFoundException {
    String aString = "foo";

    Class<? extends String> stringClass = aString.getClass();
    log.info("getName(): " + stringClass.getName());
    log.info("getSimpleName(): " + stringClass.getSimpleName());
    log.info("getCanonicalName(): " + stringClass.getCanonicalName());

    int[] integer = new int[]{1, 2};
    Class<? extends int[]> integerClass = integer.getClass();
    log.info("integer class: " + integerClass);

    Set<String> set = new HashSet<String>();
    log.info("set: " + set.getClass());

    Class<?> t = Class.forName("[D");
    log.info("Tread class: " + t);

    Class<?> doubleType = Double.TYPE;
    log.info("Tread class: " + doubleType);
  }
}