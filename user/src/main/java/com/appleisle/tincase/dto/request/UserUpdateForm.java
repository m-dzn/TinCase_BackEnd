package com.appleisle.tincase.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdateForm {

    @NotBlank
    private String nickname;

    private String address;

    private String addressDetail;

}
