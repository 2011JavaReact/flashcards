package com.revature.flashcards.model;

public class CardTemplateInRequest {
  public final int userID;

  public final String description;

  public final String front;

  public final String back;

  public CardTemplateInRequest(int userID, String description,
      String front, String back) {
    this.userID = userID;
    this.description = description;
    this.front = front;
    this.back = back;
  }
}
