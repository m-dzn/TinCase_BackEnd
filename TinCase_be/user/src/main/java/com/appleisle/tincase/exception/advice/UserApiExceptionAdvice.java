package com.appleisle.tincase.exception.advice;

import com.appleisle.tincase.dto.response.ErrorResponse;
import com.appleisle.tincase.exception.EmailExistsException;
import com.appleisle.tincase.exception.OAuth2ProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class UserApiExceptionAdvice {

    private final MessageSource messageSource;

    private static final String MESSAGE = "msg";

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ErrorResponse> emailNotFoundException(
            UsernameNotFoundException e)
    {
        String exceptionName = "usernameNotFound";

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        getMessage(exceptionName, e.getMessage())
                ));
    }

    @ExceptionHandler(EmailExistsException.class)
    protected ResponseEntity<ErrorResponse> emailExistsException(
            EmailExistsException e)
    {
        String exceptionName = "emailExists";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        getMessage(exceptionName, e.getEmail())
                ));
    }

    @ExceptionHandler(OAuth2ProcessingException.class)
    protected ResponseEntity<ErrorResponse> oAuth2ProcessingException(
            OAuth2ProcessingException e)
    {
        String exceptionName = "oAuth2Processing";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        getMessage(exceptionName, e.getMessage())
                ));
    }

    // MessageSource에서 에러 메시지를 가져오기 위한 private 메서드들
    private String getMessage(String exceptionName) {
        return getMessage(exceptionName, MESSAGE, null);
    }

    private String getMessage(String exceptionName, String ...args) {
        return getMessage(exceptionName, MESSAGE, args);
    }

    private String getMessage(String exceptionName, String property, Object[] args) {
        String code = exceptionName + "." + property;

        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

}
