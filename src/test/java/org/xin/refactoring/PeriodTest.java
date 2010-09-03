package org.xin.refactoring;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class PeriodTest {

  private final static Logger log = LoggerFactory.getLogger(PeriodTest.class);

  private static final int ONE_HOUR_IN_MILISECONDS = 60 * 60 * 1000;

  @Test
  public void shouldReturnTheStartAndEndDateUnmodified() {
    Date start = new Date();
    Date end = new Date(start.getTime() + ONE_HOUR_IN_MILISECONDS);
    log.info("End: " + end);
    Date foo = new Date(end.getTime());

    Period period = new Period(start, end);
    end.setYear(1980);

    log.info("period.getEnd() " + period.getEnd());

    assertTrue("End is modified", foo.equals(period.getEnd()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowAnExceptionIsStartIsAfterEnd() {
    Date start = new Date();
    Date end = new Date();
    end.setTime(start.getTime() - ONE_HOUR_IN_MILISECONDS);
    new Period(start, end);
  }
}
