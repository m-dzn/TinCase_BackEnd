package com.appleisle.tincase.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class MessageConfiguration implements WebMvcConfigurer {

    @Bean // Session에 locale을 설정하도록 빈 등록 (default: 'ko')
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        
        localeResolver.setDefaultLocale(Locale.KOREAN);
        return localeResolver;
    }
    
    @Bean // 지역 설정(lang)을 변경하는 인터셉터 빈 등록
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        
        localeInterceptor.setHttpMethods("lang");
        return localeInterceptor;
    }
    
    @Override // 인터셉터를 시스템 레지스트리에 등록
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean // yml 파일을 참조하는 MessageSource 선언
    public MessageSource messageSource(
            @Value("${messages.basename}") String basename,
            @Value("${messages.encoding}") String encoding
    ) {
        YamlMessageSource messageSource = new YamlMessageSource();

        messageSource.setBasename(basename);
        messageSource.setDefaultEncoding(encoding);

        messageSource.setAlwaysUseMessageFormat(true);
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setFallbackToSystemLocale(true);

        return messageSource;
    }

}
