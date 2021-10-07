package com.appleisle.tincase.security;

import com.appleisle.tincase.domain.user.UserPrincipal;
import com.appleisle.tincase.service.CustomUserDetailsService;
import com.appleisle.tincase.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JWTCheckFilter extends BasicAuthenticationFilter {

    private final JWTUtil jwtUtil;

    public JWTCheckFilter(AuthenticationManager authManager, JWTUtil jwtUtil) {
        super(authManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (bearer == null || !bearer.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = bearer.substring("Bearer ".length());
        VerifyResult result = jwtUtil.verify(jwt);

        if (result.isSuccess()) {
            UsernamePasswordAuthenticationToken authToken = jwtUtil.getAuthFromToken(jwt);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);

            chain.doFilter(request, response);
        }
    }

}
