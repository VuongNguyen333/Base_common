package org.vuongdev.common.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.vuongdev.common.dto.response.Response;
import org.vuongdev.common.exception.AppException;
import org.vuongdev.common.utils.ObjectMapperUtils;

import java.util.Objects;

@Service
public class RestTemplateService {
  @Autowired
  private RestTemplate restTemplate;

  /**
   * Generic GET method using RestTemplate.
   * @param url      URL endpoint to call, có thể có biến đường dẫn (ví dụ: "/api/users/{id}")
   * @param typeRef  ParameterizedTypeReference để chỉ định kiểu dữ liệu trả về (ví dụ: Response<User>)
   * @param params   Các tham số URL (path variables hoặc query params nếu cần)
   * @return         Đối tượng Response<T> đã được deserialize từ response của API
   */
  public <T> Response<T> getMethodRestTemplate(
          String url,
          ParameterizedTypeReference<Response<T>> typeRef,
          Object... params
  ) {
    try{
      HttpHeaders headers = new HttpHeaders();
      HttpEntity<Void> entity = new HttpEntity<>(headers);
      ResponseEntity<Response<T>> response= restTemplate.exchange(
              url,
              HttpMethod.GET,
              entity,
              typeRef,
              params
      );
      return Objects.requireNonNull(response.getBody());
    } catch (Exception exception) {
      this.throwException(exception.getMessage());
      return null;
    }
  }

  /**
   * Generic POST method using RestTemplate.
   * @param url      URL endpoint to call, có thể có biến đường dẫn (ví dụ: "/api/users/{id}")
   * @param typeRef  ParameterizedTypeReference để chỉ định kiểu dữ liệu trả về (ví dụ: Response<User>)
   * @param request  Dữ liệu gửi đi (request body)
   * @param params   Các tham số đường dẫn (nếu có)
   * @return         Đối tượng Response<T> đã được deserialize từ response của API
   */
  public <T, G> Response<T> postMethodRestTemplate(
          String url,
          ParameterizedTypeReference<Response<T>> typeRef,
          G request,
          Object... params
  ) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<G> entity = new HttpEntity<>(request, headers);
      ResponseEntity<Response<T>> response = restTemplate.exchange(
              url,
              HttpMethod.POST,
              entity,
              typeRef,
              params
      );
      return Objects.requireNonNull(response.getBody());
    } catch (Exception exception) {
      this.throwException(exception.getMessage());
      return null;
    }
  }

  /**
   * Generic file upload method using RestTemplate.
   * @param url      URL endpoint to call, có thể có biến đường dẫn (ví dụ: "/api/users/{id}")
   * @param typeRef  ParameterizedTypeReference để chỉ định kiểu dữ liệu trả về (ví dụ: Response<User>)
   * @param file     Tệp tin cần upload
   * @param params   Các tham số đường dẫn (nếu có)
   * @return         Đối tượng Response<T> đã được deserialize từ response của API
   */
  public <T> Response<T> uploadFile(
          String url,
          ParameterizedTypeReference<Response<T>> typeRef,
          MultipartFile file,
          Object... params
  ) {
    try {
      ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
        @Override
        public String getFilename() {
          return file.getOriginalFilename();
        }
      };

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("file", fileAsResource);

      HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
      ResponseEntity<Response<T>> response = restTemplate.exchange(
              url,
              HttpMethod.POST,
              entity,
              typeRef,
              params
      );
      return Objects.requireNonNull(response.getBody());
    } catch (Exception exception) {
      this.throwException(exception.getMessage());
      return null;
    }
  }

  @SneakyThrows
  private void throwException(String message) {
    String pre = message.split(":", 2)[1].trim();
    String formattedMessage = pre.substring(1, pre.length()-1).trim();
    Integer statusCode = Integer.valueOf(message.split(":", 2)[0].trim());
    Response<Object> response = ObjectMapperUtils.convertToObject(formattedMessage,  new TypeReference<Response<Object>>() {});
    throw new AppException(response, statusCode);
  }
}
