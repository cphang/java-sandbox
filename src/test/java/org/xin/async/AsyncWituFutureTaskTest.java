package org.xin.async;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

public class AsyncWituFutureTaskTest {

  private static final int RESULT = 4;
  private final static Logger log = LoggerFactory.getLogger(AsyncWituFutureTaskTest.class);

  @Test
  public void shouldFoo() throws Exception {
    final FutureTask<Integer> futureTask = asyncProcessing();
    final Executor executor = new DirectExecutor();
    executor.execute(futureTask);
    log.info(Integer.toString(1));
    log.info(Integer.toString(2));
    log.info(Integer.toString(futureTask.get()));
    log.info(Integer.toString(3));
  }

  private FutureTask<Integer> asyncProcessing() {
    final FutureTask<Integer> futureTask = new FutureTask<Integer>(
        new Callable<Integer>() {

          @Override
          public Integer call() throws Exception {
            return runLongProcess();
          }
        });
    return futureTask;
  }

  class DirectExecutor implements Executor {
    public void execute(Runnable r) {
      new Thread(r).start();
    }
  }

  public int runLongProcess() throws InterruptedException {
    log.info("start running a long process");
    Thread.sleep(3000);
    return RESULT;
  }
}
