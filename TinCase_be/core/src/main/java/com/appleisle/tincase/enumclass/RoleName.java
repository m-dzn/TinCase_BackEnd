package com.appleisle.tincase.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleName {

    ROLE_ADMIN(1, "ADMIN")
    , ROLE_USER(2, "USER")
    ;

    private Integer id;
    private String value;

}
