package com.revature.flashcards.dao;

import com.revature.flashcards.model.CardTemplate;
import com.revature.flashcards.model.CardTemplateInRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardTemplateDao extends
    BasicDao<CardTemplate, CardTemplateInRequest> {
  public CardTemplateDao() {
    super(
        "card_templates",
        "card_template_id",

        "UPDATE card_templates SET card_template_description = ?, " +
            "card_front_template = ?, card_back_template = ? " +
            "WHERE card_template_id = ?",

        "INSERT INTO card_templates (user_id, card_template_description, " +
            "card_front_template, card_back_template) VALUES " +
            "(?, ?, ?, ?)"
    );
  }

  @Override
  protected CardTemplate extract(ResultSet rs) throws SQLException {
    return new CardTemplate(
        rs.getInt("user_id"),
        rs.getString("card_template_description"),
        rs.getString("card_front_template"),
        rs.getString("card_back_template"),
        rs.getInt("card_template_id")
    );
  }

  @Override
  protected void prepareInsert(PreparedStatement stmt,
      CardTemplateInRequest obj) throws SQLException {
    stmt.setInt(1, obj.userID);
    stmt.setString(2, obj.description);
    stmt.setString(3, obj.front);
    stmt.setString(4, obj.back);
  }

  @Override
  protected void prepareUpdate(PreparedStatement stmt,
      CardTemplateInRequest obj, int id) throws SQLException {
    stmt.setString(1, obj.description);
    stmt.setString(2, obj.front);
    stmt.setString(3, obj.back);
    stmt.setInt(4, id);
  }
}
