package com.appleisle.tincase.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginForm {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
