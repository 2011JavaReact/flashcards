package com.revature.flashcards.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revature.flashcards.exception.ServiceException;
import com.revature.flashcards.model.Auth;
import com.revature.flashcards.response.APIError;
import com.revature.flashcards.response.APIResponse;
import com.revature.flashcards.response.APIResponseError;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

abstract class BasicServlet extends HttpServlet {
  protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();

  protected static void voidCallback(HttpServletResponse resp,
      VoidCallback cb) {
    PrintWriter writer = getWriter(resp);

    if (writer == null) {
      return;
    }

    try {
      Object answer = cb.execute();
      writer.append(gson.toJson(new APIResponse(answer)));
      resp.setStatus(200);
    } catch (ServiceException e) {
      onError(resp, writer, e.getMessage(), e.type);
    } catch (Throwable e) {
      onError(resp, writer, e.getMessage(), APIError.UNKNOWN);
    }
  }

  protected static <T> void bodyCallback(HttpServletRequest req,
      HttpServletResponse resp, Class<T> cls, BodyCallback<T> cb) {
    PrintWriter writer = getWriter(resp);
    BufferedReader reader = getReader(req, resp);
    T body;

    if (writer == null || reader == null) {
      return;
    }

    try {
      body = gson.fromJson(reader, cls);
    } catch (Throwable e) {
      onError(resp, writer, "couldnt parse body", APIError.BAD_REQUEST);
      return;
    }

    try {
      Object answer = cb.execute(body);
      writer.append(gson.toJson(new APIResponse(answer)));
      resp.setStatus(200);
    } catch (ServiceException e) {
      onError(resp, writer, e.getMessage(), e.type);
    } catch (Throwable e) {
      onError(resp, writer, e.getMessage(), APIError.UNKNOWN);
    }

    gson.fromJson(reader, cls);
  }

  private static void onError(HttpServletResponse resp, PrintWriter writer,
      String msg, APIError error) {
    APIResponseError err = new APIResponseError(error, msg == null ? "" : msg);
    writer.append(gson.toJson(new APIResponse(err)));
    resp.setStatus(error.httpStatus);
  }

  /**
   * returns null if ioexception is thrown. in this case, the response
   * code will also be set to 500
   */
  private static PrintWriter getWriter(HttpServletResponse resp) {
    PrintWriter writer;

    try {
      return resp.getWriter();
    } catch (IOException e) {
      resp.setStatus(500);
      return null;
    }
  }


  /**
   * returns null if ioexception is thrown. in this case, the response
   * code will also be set to 500
   */
  private static BufferedReader getReader(HttpServletRequest req,
      HttpServletResponse resp) {
    BufferedReader reader;

    try {
      return req.getReader();
    } catch (IOException e) {
      resp.setStatus(500);
      return null;
    }
  }

  /**
   * call this from your route handler if the client can provide a resource id
   * in the request route. if a number can be parsed from after the last /, it
   * will be returned.
   * <p>
   * examples:
   * /users/1 -> return 1
   * /users -> return nullable
   *
   * @return nullable if no id could be parsed from the given request path. if
   *     it could be parsed, the id is returned.
   */
  protected Optional<Integer> getResourceID(HttpServletRequest req) {
    try {
      String[] tokens = req.getRequestURI().split("/");
      int id = Integer.parseInt(tokens[tokens.length - 1]);
      return Optional.of(id);
    } catch (Throwable e) {
      return Optional.empty();
    }
  }

  /**
   * @return nullable Auth for the given request
   */
  protected Auth getAuth(HttpServletRequest req) {
    // TODO
    return null;
  }

  @FunctionalInterface
  protected interface VoidCallback {
    /**
     * @return whatever you want to return in the api response for the
     *     success body. can be null
     */
    Object execute() throws Throwable;
  }

  @FunctionalInterface
  protected interface BodyCallback<T> {
    /**
     * @return whatever you want to return in the api response for the
     *     success body. can be null
     */
    Object execute(T body) throws Throwable;
  }
}
