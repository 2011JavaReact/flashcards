package com.revature.flashcards.servlet;

import com.revature.flashcards.model.LoginRequest;
import com.revature.flashcards.service.LoginService;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends BasicServlet {
  private final LoginService service = new LoginService();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    bodyCallback(req, resp, LoginRequest.class, service::getJWT);
  }
}
