package com.revature.flashcards.model;

public class Auth {
  public final int userID;

  public final boolean admin;

  public Auth(int userID, boolean admin) {
    this.userID = userID;
    this.admin = admin;
  }

  @Override
  public String toString() {
    return "Auth{" +
        "userID=" + userID +
        ", admin=" + admin +
        '}';
  }
}
