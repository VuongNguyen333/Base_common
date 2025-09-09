package org.vuongdev.common.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.vuongdev.common.dto.response.Response;

public class ResponseUtils {
  public static <T> ResponseEntity<Response<T>> success(T data){
    Response<T> response = Response.<T>builder()
            .data(data)
            .build();
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  public static <T> ResponseEntity<Response<T>> success(T data, String message){
    Response<T> response = Response.<T>builder()
            .data(data)
            .message(message)
            .build();
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  public static <T> ResponseEntity<Response<T>> error(HttpStatus status, String message, Integer code){
    Response<T> response = Response.<T>builder()
            .code(code)
            .message(message)
            .build();
    return ResponseEntity.status(status).body(response);
  }
//  public static ResponseEntity<InputStreamResource> downloadFile(String fileName, InputStreamResource input) {
//    return ResponseEntity.ok()
//            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
//            .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
//            .contentType(MediaType.APPLICATION_OCTET_STREAM)
//            .body(input);
//  }
//
//  public static ResponseEntity<InputStreamResource> downloadFile(String fileName, ByteArrayInputStream outputStream) {
//    return ResponseEntity.ok()
//            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
//            .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
//            .contentType(MediaType.APPLICATION_OCTET_STREAM)
//            .body(new InputStreamResource(outputStream));
//  }
}