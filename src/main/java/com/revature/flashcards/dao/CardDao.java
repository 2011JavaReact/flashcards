package com.revature.flashcards.dao;

import com.revature.flashcards.model.Card;
import com.revature.flashcards.model.CardInRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardDao extends BasicDao<Card, CardInRequest> {
  public CardDao() {
    super(
        "cards",
        "card_id",

        "UPDATE cards SET card_data = ? WHERE card_id = ?",

        "INSERT INTO cards (card_template_id, card_data) VALUES (?, ?)"
    );
  }

  @Override
  protected Card extract(ResultSet rs) throws SQLException {
    return new Card(
        rs.getInt("card_template_id"),
        rs.getString("card_data"),
        rs.getInt("card_id")
    );
  }

  @Override
  protected void prepareInsert(PreparedStatement stmt, CardInRequest obj)
      throws SQLException {
    stmt.setInt(1, obj.templateID);
    stmt.setString(2, obj.data);
  }

  @Override
  protected void prepareUpdate(PreparedStatement stmt, CardInRequest obj,
      int id) throws SQLException {
    stmt.setString(1, obj.data);
    stmt.setInt(2, id);
  }
}
