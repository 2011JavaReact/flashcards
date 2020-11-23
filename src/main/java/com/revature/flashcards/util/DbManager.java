package com.revature.flashcards.util;

import com.revature.flashcards.Config;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.postgresql.Driver;

public class DbManager {
  private static boolean registeredDriver = false;

  /**
   * drops the database, sets it up, and adds dummy data to it.
   */
  void initDummyDB() throws SQLException {
    initDB();
    runScript(Config.value().dummyResourcePath);
  }

  /**
   * drops the database and sets it up
   */
  void initDB() throws SQLException {
    dropDB();
    createDB();
    runScript(Config.value().setupDbResourcesPath);
  }

  /**
   * kills conections to the db like dbeaver/pgadmin and then drops the db
   */
  void dropDB() throws SQLException {
    try (Connection conn = getPgConnection()) {
      Statement stmt = conn.createStatement();
      String dc = String.format(
          "SELECT pg_terminate_backend(pg_stat_activity.pid) " +
              "FROM pg_stat_activity " +
              "WHERE pg_stat_activity.datname = '%s' " +
              "AND pid <> pg_backend_pid()", Config.value().dbName
      );

      // dc all existing connections like pgadmin/dbeaver from the db
      stmt.execute(dc);
      stmt.execute("DROP DATABASE IF EXISTS " + Config.value().dbName);
    }
  }

  private Connection getPgConnection() throws SQLException {
    return getConnection("postgres");
  }

  /**
   * returns a connection for this database
   */
  public Connection getConnection() throws SQLException {
    return getConnection(Config.value().dbName);
  }

  /**
   * returns a connection for the given db name
   */
  private Connection getConnection(String dbName) throws SQLException {
    if (!registeredDriver) {
      DriverManager.registerDriver(new Driver());
      registeredDriver = true;
    }
    Config c = Config.value();
    String connString = String.format("%s%s", c.dbURL, dbName);
    return DriverManager.getConnection(connString, c.dbUsername, c.dbPassword);
  }

  private void createDB() throws SQLException {
    try (Connection conn = getPgConnection()) {
      Statement stmt = conn.createStatement();
      stmt.execute("CREATE DATABASE " + Config.value().dbName);
    }
  }

  /**
   * runs the given sql script file
   *
   * @param resource path to resource relative to the resources dir
   */
  private void runScript(String resource) throws SQLException {
    InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
    InputStreamReader ir = new InputStreamReader(is);

    try (Connection conn = getConnection()) {
      new ScriptRunner(conn).runScript(ir);
    }
  }
}
