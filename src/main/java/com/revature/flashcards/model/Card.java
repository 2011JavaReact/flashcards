package com.revature.flashcards.model;

public class Card extends CardInRequest {
  public final int id;

  public Card(int userID, String templateID, String data, int id) {
    super(userID, templateID, data);
    this.id = id;
  }
}
