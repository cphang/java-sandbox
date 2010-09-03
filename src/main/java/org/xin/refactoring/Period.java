package org.xin.refactoring;

import java.util.Date;

public class Period {

  private final Date end;
  private final Date start;

  public Period(Date start, Date end) {
    this.start = copyOf(start);
    this.end = copyOf(end);
    if (start.after(end))
      throw new IllegalArgumentException("start: " + start + "is after " + end);
  }

  public Date getEnd() {
    return copyOf(end);
  }

  public Date getStart() {
    return copyOf(start);
  }

  private static Date copyOf(Date old) {
    return new Date(old.getTime());
  }
}
