package com.revature.flashcards.response;

public final class APIResponseError {
  public final APIError type;

  public final String msg;

  public APIResponseError(APIError type, String msg) {
    this.type = type;
    this.msg = msg;
  }
}
