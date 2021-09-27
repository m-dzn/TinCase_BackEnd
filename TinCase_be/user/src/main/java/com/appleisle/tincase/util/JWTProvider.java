package com.appleisle.tincase.util;

import com.appleisle.tincase.domain.user.UserPrincipal;
import com.appleisle.tincase.security.VerifyResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public final class JWTProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private int expirationInMs;

    private Key key;

    @PostConstruct
    void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String makeAccessToken(Authentication auth) {
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        String userId = String.valueOf(userPrincipal.getId());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationInMs);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public VerifyResult verify(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return VerifyResult.builder()
                    .success(true)
                    .email(claims.getSubject())
                    .build();
        } catch (Exception e) {
            return VerifyResult.builder()
                    .success(false)
                    .build();
        }
    }

}
