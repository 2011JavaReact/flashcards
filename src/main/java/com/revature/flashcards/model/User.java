package com.revature.flashcards.model;

public class User {
  public final int id;

  public final String username;

  public final boolean admin;

  public User(int id, String username, boolean admin) {
    this.id = id;
    this.username = username;
    this.admin = admin;
  }
}
