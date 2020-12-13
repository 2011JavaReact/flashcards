package com.revature.flashcards.service;

import com.revature.flashcards.dao.UserDao;
import com.revature.flashcards.exception.ServiceException;
import com.revature.flashcards.model.Auth;
import com.revature.flashcards.model.User;
import com.revature.flashcards.model.UserInDb;
import com.revature.flashcards.model.UserInRequest;
import com.revature.flashcards.response.APIError;
import com.revature.flashcards.util.PasswordUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService extends BasicService<UserDao, User, UserInRequest> {
  public UserService() {
    super(new UserDao());
  }

  public UserService(UserDao dao) {
    super(dao);
  }

  @Override
  public List<User> getAll(Auth auth) throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    } else if (!auth.admin) {
      throw new ServiceException(APIError.FORBIDDEN);
    }

    try {
      ArrayList<User> out = new ArrayList<>();
      dao.getAll().forEach(u -> out.add(new User(u.id, u.username, u.admin)));
      return out;
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public User get(Auth auth, int id) throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    } else if (!auth.admin && id != auth.userID) {
      throw new ServiceException(APIError.FORBIDDEN);
    }

    try {
      UserInDb u = dao.get(id).orElseThrow(
          () -> new ServiceException(APIError.NOT_FOUND));
      return new User(u.id, u.username, u.admin);
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public User create(Auth auth, UserInRequest o) throws ServiceException {
    try {
      if (dao.getByUsername(o.username).isPresent()) {
        throw new ServiceException(APIError.CONFLICT, "A user with " +
            "that username already exists!");
      }
      UserInDb u = dao.create(safeUser(o));
      return new User(u.id, u.username, u.admin);
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public User update(Auth auth, UserInRequest o, int id)
      throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    } else if (!auth.admin && id != auth.userID) {
      throw new ServiceException(APIError.FORBIDDEN);
    }

    try {
      Optional<UserInDb> tmp = dao.getByUsername(o.username);

      if (tmp.isPresent() && tmp.get().id != id) {
        throw new ServiceException(APIError.CONFLICT, "A user with " +
            "that username already exists!");
      }
      UserInDb latest = dao.update(id, safeUser(o));
      return new User(latest.id, latest.username, latest.admin);
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(Auth auth, int id) throws ServiceException {
    if (auth == null) {
      throw new ServiceException(APIError.UNAUTHORIZED);
    } else if (!auth.admin && id != auth.userID) {
      throw new ServiceException(APIError.FORBIDDEN);
    }

    try {
      dao.delete(id);
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  private UserInRequest safeUser(UserInRequest o) {
    return new UserInRequest(o.username, PasswordUtil.hash(o.password));
  }
}
