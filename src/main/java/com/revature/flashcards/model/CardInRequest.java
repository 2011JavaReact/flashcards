package com.revature.flashcards.model;

public class CardInRequest {
  public final int templateID;

  public final String data;

  public CardInRequest(int templateID, String data) {
    this.templateID = templateID;
    this.data = data;
  }
}
