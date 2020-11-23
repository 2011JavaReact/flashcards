package com.revature.flashcards.response;

public enum APIError {
  UNKNOWN(500),
  BAD_REQUEST(400),
  UNAUTHORIZED(401),
  FORBIDDEN(403),
  NOT_FOUND(404),
  CONFLICT(409);

  public final int httpStatus;

  APIError(int httpStatus) {
    this.httpStatus = httpStatus;
  }
}
