package com.revature.flashcards.model;

public class CardTemplate extends CardTemplateInRequest {
  public final int id;

  public CardTemplate(int userID, String description, String front,
      String back, int id) {
    super(userID, description, front, back);
    this.id = id;
  }
}
