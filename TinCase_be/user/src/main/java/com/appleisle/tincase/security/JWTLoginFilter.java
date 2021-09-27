package com.appleisle.tincase.security;

import com.appleisle.tincase.domain.user.UserPrincipal;
import com.appleisle.tincase.dto.request.LoginForm;
import com.appleisle.tincase.util.JWTProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();
    private final JWTProvider jwtProvider;

    public JWTLoginFilter(AuthenticationManager authManager, JWTProvider jwtProvider) {
        super(authManager);
        setFilterProcessesUrl("/auth/login");
        this.jwtProvider = jwtProvider;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException
    {
        LoginForm loginForm = objectMapper.readValue(request.getInputStream(), LoginForm.class);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginForm.getEmail(),
                loginForm.getPassword(),
                null
        );

        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication auth) throws IOException, ServletException
    {
        response.setHeader("auth_token", jwtProvider.makeAccessToken(auth));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes((UserPrincipal) auth.getPrincipal()));
    }
}
