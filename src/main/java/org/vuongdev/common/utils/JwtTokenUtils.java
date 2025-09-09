package org.vuongdev.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.vuongdev.common.exception.AppException;
import org.vuongdev.common.exception.ErrorCodeUtils;

import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {

  public static String getToken(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
      return token.substring(7);
    }
    return null;
  }

  public static User validate(String token, String secretKey) {
    try {
      Claims claims = Jwts.parserBuilder()
              .setSigningKey(getSignInKey(secretKey))
              .build()
              .parseClaimsJws(token)
              .getBody();

      String username = claims.getSubject();
      List<String> roles = claims.get("roles", List.class);
      List<GrantedAuthority> authorities = roles.stream()
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList());

      return new User(username, "", authorities);

    } catch (ExpiredJwtException e) {
      throw new AppException(ErrorCodeUtils.TOKEN_EXPIRED);
    } catch (JwtException e) {
      throw new AppException(ErrorCodeUtils.TOKEN_INVALID);
    } catch (Exception e) {
      throw new AppException(ErrorCodeUtils.ERROR);
    }
  }

  public static Key getSignInKey(String secretKey) {
    byte[] bytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(bytes);
  }
}
