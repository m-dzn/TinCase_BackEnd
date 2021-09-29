package com.appleisle.tincase.security.oauth2;

import com.appleisle.tincase.util.CookieUtil;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class OAuth2RequestCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_REQUEST_COOKIE_NAME = "oauth2_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int cookieExpireSeconds = 3;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtil.getCookie(request, OAUTH2_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return null;
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return AuthorizationRequestRepository.super.removeAuthorizationRequest(request, response);
    }
}
