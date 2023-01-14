package com.boilerplate.oauth.backend.service.auth;

import com.boilerplate.oauth.backend.model.dto.AuthDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ManageAuthService {

    private final TokenService tokenService;
    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final int ACCESS_TOKEN_EXPIRE = 3600 * 24; // 1일
    private static final int REFRESH_TOKEN_EXPIRE = 3600 * 24 * 7; // 7일

    @Value("${front-end-url}")
    private String frontEndUrl;

    public String getAuthUrl() {
        return frontEndUrl;
    }

    public String getAuthFailureUrl(Map<String, String> parameterMap) {
        StringBuilder parameterBuilder = new StringBuilder();
        if (Objects.nonNull(parameterMap)) {
            parameterMap.forEach((key, value) -> parameterBuilder.append(key).append("=").append(value).append("&"));
        }

        return frontEndUrl + "/failure?" + parameterBuilder;
    }

    public String parseAccessToken(HttpServletRequest request) {
        String encryptedToken = getToken(request, ACCESS_TOKEN_COOKIE_NAME);
        return tokenService.parseAccessToken(encryptedToken);
    }

    public String parseRefreshToken(HttpServletRequest request) {
        String encryptedToken = getToken(request, REFRESH_TOKEN_COOKIE_NAME);
        return tokenService.parseRefreshToken(encryptedToken);
    }

    private String getToken(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if(Objects.isNull(cookies)) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public void clearAuth(HttpServletRequest request, HttpServletResponse response) {
        setToken(response, ACCESS_TOKEN_COOKIE_NAME, 0, "");
        setToken(response, REFRESH_TOKEN_COOKIE_NAME, 0, "");
    }

    private void setToken(HttpServletResponse  response, String cookieName, int expire, String data) {
        Cookie cookie = new Cookie(cookieName, data);
        cookie.setPath("/");
        cookie.setMaxAge(expire);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);

        response.addCookie(cookie);
    }

    public void transferAuth(HttpServletRequest request, HttpServletResponse response, AuthDTO authDTO) {
        // access token 발행
        String accessToken = tokenService.createAccessToken(authDTO.uid(), ACCESS_TOKEN_EXPIRE);
        // refresh token 발행
        String refreshToken = tokenService.createRefreshToken(authDTO.uid(), REFRESH_TOKEN_EXPIRE);

        // access token cookie set
        setToken(response, ACCESS_TOKEN_COOKIE_NAME, ACCESS_TOKEN_EXPIRE, accessToken);
        // refresh token cookie set
        setToken(response, REFRESH_TOKEN_COOKIE_NAME, REFRESH_TOKEN_EXPIRE, refreshToken);
    }
}
