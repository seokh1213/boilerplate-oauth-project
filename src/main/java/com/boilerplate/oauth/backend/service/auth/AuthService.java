package com.boilerplate.oauth.backend.service.auth;

import com.boilerplate.oauth.backend.model.dto.AuthDTO;
import com.boilerplate.oauth.backend.model.entity.Auth;
import com.boilerplate.oauth.backend.model.enums.AuthProvider;
import com.boilerplate.oauth.backend.repository.AuthRepository;
import com.boilerplate.oauth.backend.util.HashUtil;
import com.boilerplate.oauth.backend.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;

    public Optional<Auth> getAuthByUid(String uid) {
        return authRepository.findByUid(uid);
    }

    public AuthDTO saveAuth(Authentication authentication) {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;

        AuthProvider provider = AuthProvider.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());
        AuthDTO authDTO = parseAuthResult(authToken.getPrincipal(), provider);

        Auth auth = authRepository.findByUid(authDTO.uid())
                .orElseGet(() -> new Auth(authDTO.uid(), authDTO.email(), provider));

        authRepository.save(auth);

        return authDTO;
    }

    @SuppressWarnings("unchecked")
    private AuthDTO parseAuthResult(OAuth2User oAuth2User, AuthProvider authProvider) {
        try {
            String uid, email, nickname;
            switch (authProvider) {
                case NAVER -> {
                    Map<String, Object> responseMap = (Map<String, Object>) oAuth2User.getAttributes().get("response");

                    uid = responseMap.get("id").toString();
                    email = responseMap.get("email").toString();
                    nickname = responseMap.get("name").toString();
                }
                case KAKAO -> {
                    uid = oAuth2User.getAttributes().get("id").toString();
                    email = ((Map<String, Object>) oAuth2User.getAttributes().get("kakao_account")).get("email").toString();
                    nickname = ((Map<String, Object>) oAuth2User.getAttributes().get("properties")).get("nickname").toString();
                }
                default -> throw CommonException.SERVER_ERROR;
            }
            return new AuthDTO(HashUtil.sha256(uid), nickname, email);
        } catch (Exception e) {
            log.warn("[AuthService] Fail to login.", e);
            throw CommonException.SERVER_ERROR;
        }
    }
}
