package com.revature.flashcards.dao;

import com.revature.flashcards.util.DbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

abstract class BasicDao<T, P> {
  public final String tableName;

  protected final DbManager manager = new DbManager();

  protected final String idKey;

  private final String updateSQL;

  private final String insertSQL;

  public BasicDao(String tableName, String idKey, String updateSQL,
      String insertSQL) {
    this.tableName = tableName;
    this.idKey = idKey;
    this.updateSQL = updateSQL;
    this.insertSQL = insertSQL;
  }

  /**
   * extract <T> from rs. you can assume the cursor is position
   */
  protected abstract T extract(ResultSet rs) throws SQLException;

  /**
   * replace ? marks in update/insert statements by setting the values
   * for the given obj on the given statement.
   */
  protected abstract void prepare(PreparedStatement stmt, P obj)
      throws SQLException;

  public final List<T> getAll() throws SQLException {
    List<T> out = new ArrayList<>();
    String q = "SELECT * from " + tableName;

    try (Connection conn = manager.getConnection()) {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(q);

      while (rs.next()) {
        out.add(extract(rs));
      }
    }

    return out;
  }

  public final Optional<T> get(int id) throws SQLException {
    String q =
        String.format("SELECT * from %s where %s = %d", tableName, idKey, id);

    try (Connection conn = manager.getConnection()) {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(q);
      return rs.next() ? Optional.of(extract(rs)) : Optional.empty();
    }
  }

  public final T create(P obj) throws SQLException {
    try (Connection conn = manager.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(insertSQL,
          Statement.RETURN_GENERATED_KEYS);

      prepare(stmt, obj);
      conn.setAutoCommit(false);

      if (stmt.executeUpdate() != 1) {
        throw new SQLException("insert into " + tableName + " failed.");
      }

      ResultSet generatedKeys = stmt.getGeneratedKeys();

      if (generatedKeys.next()) {
        T result = extract(generatedKeys);
        conn.commit();
        return result;
      }

      throw new SQLException(
          "insert into " + tableName + " didnt generate an" + " id.");
    }
  }

  public final T update(int id, P obj) throws SQLException {
    try (Connection conn = manager.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(updateSQL,
          Statement.RETURN_GENERATED_KEYS);

      prepare(stmt, obj);

      if (stmt.executeUpdate() != 1 || !stmt.getGeneratedKeys().next()) {
        throw new SQLException("update for row in " + tableName + " failed.");
      }

      return extract(stmt.getGeneratedKeys());
    }
  }

  public final void delete(int id) throws SQLException {
    String q =
        String.format("DELETE from %s where %s = %d", tableName, idKey, id);

    try (Connection conn = manager.getConnection()) {
      Statement stmt = conn.createStatement();
      stmt.executeQuery(q);
    }
  }
}
