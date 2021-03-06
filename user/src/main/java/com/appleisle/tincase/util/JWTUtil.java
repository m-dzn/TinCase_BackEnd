package com.appleisle.tincase.util;

import com.appleisle.tincase.domain.user.UserPrincipal;
import com.appleisle.tincase.security.VerifyResult;
import com.appleisle.tincase.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public final class JWTUtil {

    @Value("${app.jwt.secretKey}")
    private String secretKey;
    @Value("${app.jwt.expiration}")
    private int expirationInMs;

    private Key key;

    private final CustomUserDetailsService userDetailsService;

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

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public UsernamePasswordAuthenticationToken getAuthFromToken(String token) {
        Long id = getUserIdFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserById(id);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public VerifyResult verify(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = Long.valueOf(claims.getSubject());

            return VerifyResult.builder()
                    .success(true)
                    .id(userId)
                    .build();
        } catch (Exception e) {
            return VerifyResult.builder()
                    .success(false)
                    .build();
        }
    }

}
