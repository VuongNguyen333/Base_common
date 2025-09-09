package org.vuongdev.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.vuongdev.common.constant.ConstantUtils;

@Configuration
public class RestTemplateConfig {
  @Value("${jwt.x-internal-token}")
  private String X_INTERNAL_TOKEN;

  @Bean
  @Qualifier(ConstantUtils.INTERNAL)
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();

    restTemplate.getInterceptors().add((request, body, execution) -> {
      HttpHeaders headers = request.getHeaders();
      if (!headers.containsKey(ConstantUtils.X_INTERNAL_TOKEN)) {
        headers.set(ConstantUtils.X_INTERNAL_TOKEN, X_INTERNAL_TOKEN);
      }
      return execution.execute(request, body);
    });

    return restTemplate;
  }

  @Bean
  @Qualifier(ConstantUtils.EXTERNAL)
  public RestTemplate externalRestTemplate() {
    return new RestTemplate();
  }
}
