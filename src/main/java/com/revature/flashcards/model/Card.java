package com.revature.flashcards.model;

public class Card extends CardInRequest {
  public final int id;

  public Card(int templateID, String data, int id) {
    super(templateID, data);
    this.id = id;
  }
}
