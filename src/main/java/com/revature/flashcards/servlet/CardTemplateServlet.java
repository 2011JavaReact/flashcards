package com.revature.flashcards.servlet;

import com.revature.flashcards.model.Auth;
import com.revature.flashcards.model.CardTemplateInRequest;
import com.revature.flashcards.service.CardTemplateService;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/card_templates/*")
public class CardTemplateServlet extends BasicServlet {
  private final CardTemplateService service = new CardTemplateService();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    voidCallback(resp, () -> {
      Optional<Integer> oid = getPathId(req);
      Optional<Integer> oqid = getIntQueryParam(req, "userId");
      Auth auth = getAuth(req);

      if (oid.isPresent()) {
        System.out.println(1);
        return service.get(auth, oid.get());
      } else if (oqid.isPresent()) {
        System.out.println(2);
        return service.getAllForUser(auth, oqid.get());
      }
      System.out.println(3);
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
    bodyCallback(req, resp, CardTemplateInRequest.class,
        (o) -> service.create(getAuth(req), o));
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
    bodyCallback(req, resp, CardTemplateInRequest.class, (o) -> {
      int id = getPathIdOrThrow(req);
      return service.update(getAuth(req), o, id);
    });
  }
}
