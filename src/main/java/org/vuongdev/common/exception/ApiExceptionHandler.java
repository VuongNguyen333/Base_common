package org.vuongdev.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.vuongdev.common.dto.response.Response;
import org.vuongdev.common.utils.LocalizationUtils;

import java.util.Arrays;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiExceptionHandler {
  private final LocalizationUtils localizationUtils;

  @ExceptionHandler({AppException.class})
  protected ResponseEntity<Response<Object>> handleAppException(AppException appException) {
    log.error("AppException: {}", appException.getMessage(), appException);
    ErrorCode errorCode = appException.getErrorCode();
    Response<Object> responseError = Response.builder()
            .code(errorCode.getCode())
            .message(localizationUtils.getLocalizedMessage(errorCode.getMessage()))
            .build();
    return ResponseEntity.status(errorCode.getStatusCode()).body(responseError);
  }

  // validate error
  @ExceptionHandler({MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Response<Object>> handleValidationException(MethodArgumentNotValidException ex) {
    log.error("Validate error: {}", ex.getMessage(), ex);
    String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

    //handle PARAM parse error (for param)
    String[] errorCodes = ex.getBindingResult().getAllErrors().get(0).getCodes();
    if (Arrays.asList(errorCodes).contains("typeMismatch")) {
      ErrorCode errorCode = ErrorCodeUtils.INVALID_FORMAT;
      message = localizationUtils.getLocalizedMessage(errorCode.getMessage());
    } else {
      message = localizationUtils.getLocalizedMessage(message);
    }
    Response<Object> responseError = Response.builder()
            .message(message)
            .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
  }

  // authorization
  @ExceptionHandler({AccessDeniedException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<Response<Object>> handleAccessDeniedException(AccessDeniedException ex) {
    log.error("AccessDeniedException: {}", ex.getMessage(), ex);
    ErrorCode errorCode = ErrorCodeUtils.ACCESS_DENIED;
    Response<Object> responseError = Response.builder()
            .message(localizationUtils.getLocalizedMessage(errorCode.getMessage()))
            .build();
    return ResponseEntity.status(errorCode.getStatusCode()).body(responseError);
  }

  // JSON parse error (for request body)
  @ExceptionHandler({InvalidFormatException.class, HttpMessageNotReadableException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Response<Object>> handleInvalidFormatException(Exception ex) {
    log.error("JSON parse error: {}", ex.getMessage(), ex);
    ErrorCode errorCode = ErrorCodeUtils.INVALID_FORMAT;
    Response<Object> responseError = Response.builder()
            .message(localizationUtils.getLocalizedMessage(errorCode.getMessage()))
            .build();
    return ResponseEntity.status(errorCode.getStatusCode()).body(responseError);
  }

  @ExceptionHandler({RuntimeException.class, Exception.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Response<Object>> handleException(Exception ex) {
    log.error("Exception: {}", ex.getMessage(), ex);
    ErrorCode errorCode = ErrorCodeUtils.ERROR;
    Response<Object> responseError = Response.builder()
            .message(localizationUtils.getLocalizedMessage(errorCode.getMessage()))
            .code(errorCode.getCode())
            .build();
    return ResponseEntity.status(errorCode.getStatusCode()).body(responseError);
  }
}

