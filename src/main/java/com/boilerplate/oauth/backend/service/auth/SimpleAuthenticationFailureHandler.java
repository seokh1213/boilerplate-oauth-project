package com.boilerplate.oauth.backend.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimpleAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final ManageAuthService manageAuthService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        manageAuthService.clearAuth(request, response);

        log.debug("[onAuthenticationFailure] ", exception);
        getRedirectStrategy().sendRedirect(request, response, manageAuthService.getAuthFailureUrl(Map.of("error", "server_error")));
    }
}
