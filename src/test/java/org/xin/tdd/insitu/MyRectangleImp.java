package org.xin.tdd.insitu;

public class MyRectangleImp implements MyRectangle {

  private final int width;
  private final int height;

  public MyRectangleImp(int i, int j, int k, int l) {
    this.width = i;
    this.height = j;
  }

  @Override
  public int getArea() {
    return width * height;
  }

  @Override
  public int getSumFoo() {
    return 2 * (width + height);
  }

}
