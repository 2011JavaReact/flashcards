package com.revature.flashcards.model;

public class UserInDb extends User {
  public final String password;

  public UserInDb(int id, String username, boolean admin, String password) {
    super(id, username, admin);
    this.password = password;
  }
}
