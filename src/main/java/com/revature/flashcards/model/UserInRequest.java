package com.revature.flashcards.model;

public class UserInRequest {
  public final String username;

  public final String password;

  public UserInRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
