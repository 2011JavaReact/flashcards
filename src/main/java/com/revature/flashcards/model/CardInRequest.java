package com.revature.flashcards.model;

public class CardInRequest {
  public final int userID;

  public final String templateID;

  public final String data;

  public CardInRequest(int userID, String templateID, String data) {
    this.userID = userID;
    this.templateID = templateID;
    this.data = data;
  }
}
