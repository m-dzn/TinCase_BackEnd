package com.appleisle.tincase.security;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyResult {

    private boolean success;
    private Long id;

}
