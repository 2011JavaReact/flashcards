package com.revature.flashcards.servlet;

import com.revature.flashcards.model.Auth;
import com.revature.flashcards.model.UserInRequest;
import com.revature.flashcards.service.UserService;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/users/*")
public class UserServlet extends BasicServlet {
  private final UserService service = new UserService();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    voidCallback(resp, () -> {
      Optional<Integer> oid = getPathId(req);
      Auth a = getAuth(req);
      return oid.isPresent() ? service.get(a, oid.get()) : service.getAll(a);
    });
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
    voidCallback(resp, () -> {
      service.delete(getAuth(req), getPathIdOrThrow(req));
      return null;
    });
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    bodyCallback(req, resp, UserInRequest.class,
        (o) -> service.create(getAuth(req), o));
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
    bodyCallback(req, resp, UserInRequest.class, (o) -> {
      int id = getPathIdOrThrow(req);
      return service.update(getAuth(req), o, id);
    });
  }
}
