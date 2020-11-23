package com.revature.flashcards.util;

import com.revature.flashcards.Config;

public class DbManagerDriver {
  public static void main(String[] args) throws Exception {
    String cmd = args.length > 0 ? args[0] : null;
    DbManager manager = new DbManager();
    Config c = Config.value();

    if ("dropdb".equals(cmd)) {
      System.out.println("dropping db " + c.dbName);
      manager.dropDB();
    } else if ("initdb".equals(cmd)) {
      System.out.println("initializing db " + c.dbName);
      manager.initDB();
    } else if ("dummydb".equals(cmd)) {
      System.out.println("initializing dummy db " + c.dbName);
      manager.initDummyDB();
    } else {
      System.err.printf("%s is an invalid command!%n", cmd);
      System.exit(1);
    }
  }
}
