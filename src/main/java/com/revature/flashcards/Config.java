package com.revature.flashcards;

public class Config {
  private static Config singleton;

  /**
   * @return the singleton Config instance
   */
  public static Config value() {
    if (singleton == null) {
      singleton = new Config();
    }
    return singleton;
  }

  /**
   * how many milliseconds a jwt can last before its expired.
   */
  public final long jwtTTLMillis = 60000;

  /**
   * path to dummy resource .sql file relative to resources dir
   */
  public final String dummyResourcePath = "addDummyData.sql";

  /**
   * path to setup db resource .sql file relative to resources dir
   */
  public final String setupDbResourcesPath = "setupDB.sql";

  /**
   * jdbc conn string w/o database. also guaranteed to end in slash
   */
  public final String dbURL;

  /**
   * name of postgres database you want to operate on. cannot == postgres
   */
  public final String dbName;

  /**
   * username for the database you want to operate on
   */
  public final String dbUsername;

  /**
   * password for the database you want to operate on
   */
  public final String dbPassword;

  /**
   * used for jwt signing/decoding
   */
  public final String jwtKey =
      "This signing key must be really long for the HS256 algo to work.";

  private Config() {
    dbUsername = getEnv("DB_USERNAME");
    dbPassword = getEnv("DB_PASSWORD");
    dbURL = getEnv("DB_URL");
    dbName = getEnv("DB_NAME");
    // jwtKey = getEnv("JWT_KEY");

    if (dbName.equals("postgres")) {
      throw new RuntimeException("postgres database is reserved! dont use it!");
    } else if (!dbURL.endsWith("/")) {
      throw new RuntimeException("DB_URL should end with a /");
    }
  }

  private String getEnv(String var) {
    String ret = System.getenv(var);
    if (ret == null) {
      throw new RuntimeException(String.format("env var %s not set!", var));
    }
    return ret;
  }
}