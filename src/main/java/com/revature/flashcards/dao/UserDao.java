package com.revature.flashcards.dao;

import com.revature.flashcards.model.UserInDb;
import com.revature.flashcards.model.UserInRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDao extends BasicDao<UserInDb, UserInRequest> {
  public UserDao() {
    super(
        "users",
        "user_id",

        "UPDATE users SET user_name = ?, user_password = ?, user_admin = ? " +
            "WHERE user_id = ?",

        "INSERT INTO users (user_name, user_password, user_admin) VALUES " +
            "(?, ?, ?)"
    );
  }

  @Override
  protected UserInDb extract(ResultSet rs) throws SQLException {
    return new UserInDb(
        rs.getInt("user_id"),
        rs.getString("user_name"),
        rs.getBoolean("user_admin"),
        rs.getString("user_password")
    );
  }

  @Override
  protected void prepare(PreparedStatement stmt, UserInRequest obj)
      throws SQLException {
    stmt.setString(1, obj.username);
    stmt.setString(2, obj.password);
    stmt.setBoolean(3, false);
  }

  public Optional<UserInDb> getByUsername(String username) throws SQLException {
    String q = String.format("SELECT * from %s where user_name = ?", tableName);

    try (Connection conn = manager.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(q);
      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();
      return rs.next() ? Optional.of(extract(rs)) : Optional.empty();
    }
  }
}
