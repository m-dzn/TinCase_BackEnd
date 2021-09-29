package com.appleisle.tincase.security;

import com.appleisle.tincase.domain.user.UserPrincipal;
import com.appleisle.tincase.util.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTCheckFilter extends BasicAuthenticationFilter {

    private final UserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    public JWTCheckFilter(AuthenticationManager authManager, UserDetailsService userDetailsService, JWTUtil jwtUtil) {
        super(authManager);
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (bearer == null || !bearer.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = bearer.substring("Bearer ".length());
        VerifyResult result = jwtUtil.verify(token);

        if (result.isSuccess()) {
            UserPrincipal userPrincipal = (UserPrincipal) userDetailsService
                    .loadUserByUsername(result.getEmail());

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userPrincipal.getUsername(),
                    null,
                    userPrincipal.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);

            chain.doFilter(request, response);
        }
    }

}
