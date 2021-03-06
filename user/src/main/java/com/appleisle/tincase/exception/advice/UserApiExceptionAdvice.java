package com.appleisle.tincase.exception.advice;

import com.appleisle.tincase.dto.response.ErrorResponse;
import com.appleisle.tincase.exception.*;
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
    protected ResponseEntity<ErrorResponse> usernameNotFound(
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
    protected ResponseEntity<ErrorResponse> emailExists(
            EmailExistsException e)
    {
        String exceptionName = "emailExists";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        getMessage(exceptionName, e.getEmail())
                ));
    }

    @ExceptionHandler(OAuth2InvalidProviderException.class)
    protected ResponseEntity<ErrorResponse> oAuth2InvalidProvider(
            OAuth2InvalidProviderException e)
    {
        String exceptionName = "oAuth2InvalidProvider";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        getMessage(exceptionName, e.getMessage())
                ));
    }

    @ExceptionHandler(OAuth2ExistsException.class)
    protected ResponseEntity<ErrorResponse> oAuth2Exists(
            OAuth2ExistsException e)
    {
        String exceptionName = "oAuth2Exists";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        getMessage(exceptionName, e.getMessage())
                ));
    }

    @ExceptionHandler(UnauthorizedRedirectURIException.class)
    protected ResponseEntity<ErrorResponse> unauthorizedRedirectURI(
            UnauthorizedRedirectURIException e)
    {
        String exceptionName = "unauthorizedRedirectURI";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        getMessage(exceptionName, e.getMessage())
                ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ErrorResponse> resource(ResourceNotFoundException e) {
        String exceptionName = "resourceNotFound";

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        getMessage(exceptionName, e.getResourceName(), e.getFieldName(), e.getFieldValue())
                ));
    }

    // MessageSource?????? ?????? ???????????? ???????????? ?????? private ????????????
    private String getMessage(String exceptionName) {
        return getYamlMessage(exceptionName, MESSAGE, null);
    }

    private String getMessage(String exceptionName, Object ...args) {
        return getYamlMessage(exceptionName, MESSAGE, args);
    }

    private String getYamlMessage(String exceptionName, String property, Object[] args) {
        String code = exceptionName + "." + property;

        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

}
