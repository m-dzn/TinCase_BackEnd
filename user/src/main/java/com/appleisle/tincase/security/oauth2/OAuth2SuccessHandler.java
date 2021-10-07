package com.appleisle.tincase.security.oauth2;

import com.appleisle.tincase.exception.UnauthorizedRedirectURIException;
import com.appleisle.tincase.util.CookieUtil;
import com.appleisle.tincase.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.oauth2.authorizedRedirectUris}")
    private List<String> authorizedRedirectUris;
    private static final String TOKEN_PARAMETER_NAME = "token";

    private final JWTUtil jwtUtil;
    private final OAuth2RequestCookieRepository oAuth2RequestCookieRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication auth) throws IOException, ServletException
    {
        String targetUrl = determineTargetUrl(request, response, auth);

        if (response.isCommitted()) {
            logger.debug("Http Response가 이미 커밋되었습니다. 해당 주소로 리다이렉트할 수 없습니다. : " + targetUrl);
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(
            HttpServletRequest request, HttpServletResponse response, Authentication auth)
    {
        Optional<String> redirectUri = CookieUtil.getCookie(
                        request,
                        OAuth2RequestCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME
                ).map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new UnauthorizedRedirectURIException();
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String token = jwtUtil.makeAccessToken(auth);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam(TOKEN_PARAMETER_NAME, token)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        oAuth2RequestCookieRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return authorizedRedirectUris.stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(authorizedRedirectUri);

                    if (authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                        && authorizedURI.getPort() == clientRedirectUri.getPort())
                    {
                        return true;
                    }

                    return false;
                });
    }

}
