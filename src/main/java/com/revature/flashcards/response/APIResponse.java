package com.revature.flashcards.response;

public final class APIResponse {
  /**
   * http status code for this response
   */
  public final int status;

  /**
   * error for this response. can be null.
   */
  public final APIResponseError error;

  /**
   * success body for this response. can be null.
   */
  public final Object success;

  public APIResponse() {
    this.status = 200;
    this.error = null;
    this.success = null;
  }

  public APIResponse(Object success) {
    this.status = 200;
    this.error = null;
    this.success = success;
  }

  public APIResponse(APIResponseError error) {
    this.status = error.type.httpStatus;
    this.error = error;
    this.success = null;
  }

}
