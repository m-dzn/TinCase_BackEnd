package com.appleisle.tincase.util;

import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;

public class CookieUtil {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();

        // 쿠키가 1개 이상 존재하면 쿠키 배열 순회 검색
        if (cookies != null && cookies.length > 0) {
            // 찾는 쿠키가 있으면 해당 쿠키 반환
            for (Cookie cookie : cookies) {
                String currentKey = cookie.getName();

                if (currentKey.equals(key)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie[] cookies = request.getCookies();

        // 쿠키가 비어있으면 탈출
        if (cookies == null || cookies.length <= 0) return;

        // 찾는 쿠키가 있으면 삭제
        for (Cookie cookie : cookies) {
            String currentKey = cookie.getName();

            if (currentKey.equals(key)) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> clazz) {
        return clazz.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue()))
        );
    }

}
