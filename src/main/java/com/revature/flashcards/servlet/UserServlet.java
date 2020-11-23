package com.revature.flashcards.servlet;

import com.revature.flashcards.service.UserService;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/users/*")
public class UserServlet extends BasicServlet {
  private UserService service = new UserService();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    Optional<Integer> oid = getResourceID(req);



    // if (oid.isPresent()) {
    //   finish(resp, () -> {
    //
    //   });
    // } else {
    //   service.getAll()
    //   // resp.getWriter().append()
    // }
  }
}
