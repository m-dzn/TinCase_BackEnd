package com.appleisle.tincase.security.oauth2.userinfo;

import com.appleisle.tincase.domain.user.User;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getAvatar();

    public User toEntity() {
        User user = User.builder()
                .email(this.getEmail())
                .build();
        user.setNickname(this.getName());
        user.setAvatar(this.getAvatar());

        return user;
    }

}
