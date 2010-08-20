package org.xin.refactoring;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 * 
 */
@SuppressWarnings("serial")
public class App {

  protected static final String ME = "Me";

  public enum Type {
    FOO, BAR
  }

  private final Map<Type, MyCommand> commandByType = new HashMap<Type, MyCommand>();

  public App() {
    commandByType.put(Type.FOO, new FooCommand());
    commandByType.put(Type.BAR, new BarCommand());
  }

  public String process(Type foo) {
    return commandByType.get(foo).execute();
  }

  interface CommandFactory {
    MyCommand create();
  }

  private final Map<Type, CommandFactory> factoryMap = Collections.unmodifiableMap(new HashMap<Type, CommandFactory>() {
    {
      put(Type.FOO, new CommandFactory() {
        public MyCommand create() {
          return new FooCommand();
        }
      });
      put(Type.BAR, new CommandFactory() {
        public MyCommand create() {
          return new BarCommand();
        }
      });
    }
  });

  public String processUsingFactory(Type bar) {
    CommandFactory factory = factoryMap.get(bar);
    return factory.create().execute();
  }

  private final Map<Type, String> classNameByType = Collections.unmodifiableMap(new HashMap<Type, String>() {
    {
      put(Type.FOO, FooCommand.class.getName());
      put(Type.BAR, BarCommand.class.getName());
    }
  });

  public String processUsingReflection(Type arg) throws InstantiationException,
      IllegalAccessException, ClassNotFoundException {
    return ((MyCommand) Class.forName(classNameByType.get(arg)).newInstance()).execute();
  }

}
