package com.revature.flashcards.service;

import com.revature.flashcards.Config;
import com.revature.flashcards.dao.UserDao;
import com.revature.flashcards.exception.ServiceException;
import com.revature.flashcards.model.Auth;
import com.revature.flashcards.model.LoginRequest;
import com.revature.flashcards.model.UserInDb;
import com.revature.flashcards.response.APIError;
import com.revature.flashcards.util.JwtUtil;
import com.revature.flashcards.util.PasswordUtil;
import java.sql.SQLException;

public class LoginService {
  private final UserDao dao;

  public LoginService() {
    this.dao = new UserDao();
  }

  public LoginService(UserDao dao) {
    this.dao = dao;
  }

  public String getJWT(LoginRequest req) throws ServiceException {
    try {
      UserInDb u = dao.getByUsername(req.username).orElseThrow(() ->
          new ServiceException(APIError.NOT_FOUND, "user not found"));
      
      if (!PasswordUtil.doesMatch(req.password, u.password)) {
        throw new ServiceException(APIError.UNAUTHORIZED, "wrong password");
      }

      Auth auth = new Auth(u.id, u.admin);
      return JwtUtil.create(Config.value().jwtTTLMillis, Config.value().jwtKey,
          auth);
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }
}
