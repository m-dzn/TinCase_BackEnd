package com.appleisle.tincase.config;

import com.appleisle.tincase.security.JWTCheckFilter;
import com.appleisle.tincase.security.JWTLoginFilter;
import com.appleisle.tincase.security.oauth2.OAuth2FailureHandler;
import com.appleisle.tincase.security.oauth2.OAuth2RequestCookieRepository;
import com.appleisle.tincase.security.oauth2.OAuth2SuccessHandler;
import com.appleisle.tincase.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    // OAuth2 관련 의존성
    private final DefaultOAuth2UserService oAuth2UserService;
    private final OAuth2RequestCookieRepository oAuth2RequestCookieRepository;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JWTLoginFilter jwtLoginFilter = new JWTLoginFilter(authenticationManager(), jwtUtil);
        JWTCheckFilter jwtCheckFilter = new JWTCheckFilter(authenticationManager(), userDetailsService, jwtUtil);

        http
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeRequests()
                    .antMatchers("/", "/auth/**").permitAll()
                    .anyRequest().permitAll();

        http.oauth2Login()
                .authorizationEndpoint()
                    .baseUri("/oauth2/authorize")
                    .authorizationRequestRepository(oAuth2RequestCookieRepository)
                    .and()
                .redirectionEndpoint()
                    .baseUri("/oauth2/callback/*")
                    .and()
                .failureHandler(oAuth2FailureHandler)
                .userInfoEndpoint()
                    .userService(oAuth2UserService)
                    .and()
                .successHandler(oAuth2SuccessHandler);

        http
                .addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtCheckFilter, BasicAuthenticationFilter.class);
    }

}
