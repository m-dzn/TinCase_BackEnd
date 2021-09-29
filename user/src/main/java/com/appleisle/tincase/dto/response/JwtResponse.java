package com.appleisle.tincase.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class JwtResponse {

    private String accessToken;

    public JwtResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}
