package com.appleisle.tincase.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ErrorResponse {

    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

}
