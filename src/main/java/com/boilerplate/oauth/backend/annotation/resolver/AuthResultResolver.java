package com.boilerplate.oauth.backend.annotation.resolver;

import com.boilerplate.oauth.backend.model.dto.UserDTO;
import com.boilerplate.oauth.backend.model.entity.Auth;
import com.boilerplate.oauth.backend.model.entity.User;
import com.boilerplate.oauth.backend.annotation.AuthResult;
import com.boilerplate.oauth.backend.exception.CommonException;
import com.boilerplate.oauth.backend.service.UserService;
import com.boilerplate.oauth.backend.service.auth.AuthService;
import com.boilerplate.oauth.backend.service.auth.ManageAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthResultResolver implements HandlerMethodArgumentResolver {

    private final ManageAuthService manageAuthService;
    private final AuthService authService;
    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthResult.class) != null &&
                parameter.getParameterType().equals(UserDTO.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        AuthResult authResult = parameter.getParameterAnnotation(AuthResult.class);
        if (Objects.isNull(authResult)) {
            throw CommonException.ILLEGAL_STATE_ERROR;
        }

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Auth auth = Optional.ofNullable(manageAuthService.parseAccessToken(request))
                .flatMap(authService::getAuthByUid)
                .orElse(null);

        if(Objects.isNull(auth)) {
            if(authResult.isRequired()) {
                throw CommonException.UNAUTHORIZED;
            } else {
                return null;
            }
        }

        User user = userService.getUserByUid(auth.getUid())
                .orElse(null);

        if(Objects.isNull(user)) {
            if(authResult.isRequired()) {
                throw CommonException.UNAUTHORIZED;
            }
        } else {
            if(user.isBlock()) {
                throw CommonException.FORBIDDEN;
            }
            return new UserDTO(user.getUid(), user.getNickname(), user.getType());
        }

        return null;
    }
}
