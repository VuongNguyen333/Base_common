package org.vuongdev.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LocalizationUtils {
  private final MessageSource messageSource;
  private final LocaleResolver localeResolver;

  public String getLocalizedMessage(String messageKey, Object... params) {
    String EXCEPTION_MESSAGE = "Có lỗi xảy ra, xin vui lòng thử lại sau";
    try {
      HttpServletRequest request = WebUtils.getCurrentRequest();
      Locale locale = this.localeResolver.resolveLocale(request);

      // Locale locale = new Locale("vi", "VN");
      return this.messageSource.getMessage(messageKey, params, locale);
    } catch (Exception e) {
      return EXCEPTION_MESSAGE;
    }
  }
}