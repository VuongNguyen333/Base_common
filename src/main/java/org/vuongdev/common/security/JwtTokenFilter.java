package org.vuongdev.common.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vuongdev.common.dto.response.Response;
import org.vuongdev.common.exception.AppException;
import org.vuongdev.common.exception.ErrorCode;
import org.vuongdev.common.utils.JwtTokenUtils;
import org.vuongdev.common.utils.LocalizationUtils;
import org.vuongdev.common.utils.ObjectMapperUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
  @Value("${jwt.secret-key}")
  private String SECRET_KEY;
  @Autowired
  private LocalizationUtils localizationUtils;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain)
          throws ExpiredJwtException, IOException, ServletException {

    try {
      String jwt = JwtTokenUtils.getToken(request);
      if (jwt != null) {
        User user = JwtTokenUtils.validate(jwt, SECRET_KEY);
        var authentication = new UsernamePasswordAuthenticationToken(user, user.getUsername(), user.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (AppException e) {
      log.error("Cannot set user authentication: {}", e.getMessage());
      ErrorCode errorCode = e.getErrorCode();
      Response<Object> apiResponse = Response.builder()
              .code(errorCode.getCode())
              .message(localizationUtils.getLocalizedMessage(errorCode.getMessage(), e.getParams()))
              .build();

      response.setStatus(errorCode.getStatusCode().value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding(StandardCharsets.UTF_8.name());
      response.getWriter().write(Objects.requireNonNull(ObjectMapperUtils.convertToJson(apiResponse)));
      response.flushBuffer();
      return;
    }
    filterChain.doFilter(request, response);
  }
}

