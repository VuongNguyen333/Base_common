package org.vuongdev.common.exception;

import org.springframework.http.HttpStatus;
import org.vuongdev.common.constant.Messages;

public enum ErrorCodeUtils implements ErrorCode {

  UNAUTHENTICATED(401, Messages.UNAUTHENTICATED, HttpStatus.UNAUTHORIZED),
  ACCESS_DENIED(403, Messages.ACCESS_DENIED, HttpStatus.FORBIDDEN),
  ERROR(500, Messages.ERROR, HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_FORMAT(101, Messages.INVALID_FORMAT, HttpStatus.BAD_REQUEST),
  DATA_NOT_FOUND(102, Messages.DATA_NOTFOUND, HttpStatus.NOT_FOUND),
  TOKEN_EXPIRED(103, Messages.TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED),
  TOKEN_INVALID(104, Messages.TOKEN_INVALID, HttpStatus.UNAUTHORIZED)
  ;

  private final Integer code;
  private final String message;
  private final HttpStatus statusCode;

  ErrorCodeUtils(Integer code, String message, HttpStatus statusCode) {
    this.code = code;
    this.message = message;
    this.statusCode = statusCode;
  }

  @Override
  public Integer getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public HttpStatus getStatusCode() {
    return statusCode;
  }
}
