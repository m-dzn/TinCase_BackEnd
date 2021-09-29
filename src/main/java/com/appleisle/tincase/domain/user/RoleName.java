package com.appleisle.tincase.domain.user;

import lombok.Getter;

@Getter
public enum RoleName {
    ROLE_ADMIN(1, "ADMIN")
    , ROLE_USER(2, "USER")
    ;

    private final Integer id;
    private final String name;

    RoleName(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
