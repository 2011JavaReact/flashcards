package com.revature.flashcards.servlet;

import com.revature.flashcards.model.Auth;
import com.revature.flashcards.model.CardInRequest;
import com.revature.flashcards.service.CardService;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cards/*")
public class CardServlet extends BasicServlet {
  private final CardService service = new CardService();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    voidCallback(resp, () -> {
      Optional<Integer> oid = getPathId(req);
      Optional<Integer> oqid = getIntQueryParam(req, "userId");
      Auth auth = getAuth(req);

      if (oid.isPresent()) {
        return service.get(auth, oid.get());
      } else if (oqid.isPresent()) {
        return service.getAllForUser(auth, oqid.get());
      }
      return service.getAll(auth);
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
    bodyCallback(req, resp, CardInRequest.class,
        (o) -> service.create(getAuth(req), o));
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
    bodyCallback(req, resp, CardInRequest.class, (o) -> {
      int id = getPathIdOrThrow(req);
      return service.update(getAuth(req), o, id);
    });
  }
}
