package com.appleisle.tincase.security.oauth2;

import com.appleisle.tincase.enumclass.OAuthProvider;
import com.appleisle.tincase.exception.OAuth2ProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getUserInfo(String registrationId, Map<String, Object> attributes) {

        if (registrationId.equalsIgnoreCase(OAuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(OAuthProvider.github.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2ProcessingException(registrationId);
        }

    }
}
