package org.xin.jackson;

public class ContactImpl implements Contact {

  private final String name;
  private final int age;

  public ContactImpl(String name, int age) {
    this.name = name;
    this.age = age;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ContactImpl [string=" + name + ", age=" + age + "]";
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public int age() {
    return age;
  }

}
