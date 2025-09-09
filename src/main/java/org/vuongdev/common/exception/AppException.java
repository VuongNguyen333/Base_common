package org.vuongdev.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.vuongdev.common.dto.response.Response;

@Getter
@Setter
public class AppException extends RuntimeException {
  private ErrorCode errorCode;
  private Object[] params;
  private Response<Object> response;
  private Integer statusCode;

  public AppException(Response<Object> response, Integer statusCode, Object... params) {
    super(response.getMessage());
    this.response = response;
    this.statusCode = statusCode;
    this.params = params;
  }
  public AppException(ErrorCode errorCode, Object... params) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.params = params;
  }

}