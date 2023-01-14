package com.boilerplate.oauth.backend.service.auth;

import com.boilerplate.oauth.backend.util.AES256Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class TokenService {

    private static final String delimiter = "&&";
    private static final String ACCESS_TOKEN_PREFIX = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN_PREFIX = "REFRESH_TOKEN";

    @Value("${token-key}")
    private String tokenKey;

    public String createAccessToken(String data, int expire) {
        return issueToken(data, ACCESS_TOKEN_PREFIX, expire);
    }

    public String createRefreshToken(String data, int expire) {
        return issueToken(data, REFRESH_TOKEN_PREFIX, expire);
    }

    public String parseAccessToken(String token) {
        return parseToken(token, ACCESS_TOKEN_PREFIX);
    }

    public String parseRefreshToken(String token) {
        return parseToken(token, REFRESH_TOKEN_PREFIX);
    }

    private String issueToken(String data, String tokenPrefix, int cookieExpire) {
        if(Objects.isNull(data)) {
            log.warn("[issueToken] data is null.");
            return null;
        }

        String expireString = String.valueOf((System.currentTimeMillis() + cookieExpire * 1000L));
        String rawToken = String.join(delimiter, tokenPrefix, expireString, data);
        String encryptedToken = AES256Util.encrypt(tokenKey, rawToken);

        log.debug("issueToken[{}]: {}", tokenPrefix, encryptedToken);

        if (Objects.isNull(encryptedToken)) {
            log.debug("[issueToken] fail to encrypt to token {}", rawToken);
            return null;
        }

        return encryptedToken;
    }

    private String parseToken(String encryptedToken, String tokenPrefix) {
        if(Objects.isNull(encryptedToken)) {
            log.warn("[parseToken] encrypted token is null.");
            return null;
        }

        log.debug("parseToken: {}", encryptedToken);

        String rawToken = AES256Util.decrypt(tokenKey, encryptedToken);
        if (Objects.isNull(rawToken)) {
            log.error("[parseToken] fail to decrypt token {}", encryptedToken);
            return null;
        }

        String[] data = rawToken.split(delimiter);

        if(!tokenPrefix.equals(data[0])) {
            log.debug("[parseToken] access token is expired {}", rawToken);
            return null;
        }

        long expireDt = Long.parseLong(data[1]);
        if (System.currentTimeMillis() > expireDt) {
            log.debug("[parseToken] access token is expired {}", rawToken);
            return null;
        }

        return data[2];
    }
}
