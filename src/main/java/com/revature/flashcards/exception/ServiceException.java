package com.revature.flashcards.exception;

import com.revature.flashcards.response.APIError;

public class ServiceException extends Exception {
  public final APIError type;

  public ServiceException(APIError APIError) {
    this.type = APIError;
  }

  public ServiceException(String reason) {
    this(APIError.UNKNOWN, reason);
  }

  public ServiceException(Throwable throwable) {
    this(APIError.UNKNOWN, throwable);
  }

  public ServiceException(APIError APIError, String reason) {
    super(reason);
    this.type = APIError;
  }

  public ServiceException(APIError APIError, Throwable throwable) {
    super(throwable);
    this.type = APIError;
  }
}
