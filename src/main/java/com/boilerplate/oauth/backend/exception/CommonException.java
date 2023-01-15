package com.boilerplate.oauth.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonException extends BaseException {

    private final HttpStatus status;
    private final String errorDescription;

    private CommonException(HttpStatus status, String errorDescription) {
        super(errorDescription);
        this.status = status;
        this.errorDescription = errorDescription;
    }

    public static CommonException ALREADY_REGISTERED_USER = new CommonException(HttpStatus.CONFLICT, "ALREADY_REGISTERED_USER");
    public static CommonException ALREADY_REGISTERED_NICKNAME = new CommonException(HttpStatus.CONFLICT, "ALREADY_REGISTERED_NICKNAME");
    public static CommonException ALREADY_REGISTERED_EMAIL = new CommonException(HttpStatus.CONFLICT, "ALREADY_REGISTERED_EMAIL");
    public static CommonException INVALID_PROVIDER = new CommonException(HttpStatus.BAD_REQUEST, "INVALID_PROVIDER");
    public static CommonException INVALID_PARAMETER = new CommonException(HttpStatus.BAD_REQUEST, "INVALID_PARAMETER");
    public static CommonException MISSING_PARAMETER = new CommonException(HttpStatus.BAD_REQUEST, "MISSING_PARAMETER");
    public static CommonException BAD_REQUEST = new CommonException(HttpStatus.BAD_REQUEST, "BAD_REQUEST");
    public static CommonException WRONG_EMAIL_FORMAT = new CommonException(HttpStatus.BAD_REQUEST, "WRONG_EMAIL_FORMAT");
    public static CommonException UNAUTHORIZED = new CommonException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    public static CommonException GET_TOKEN_ERROR = new CommonException(HttpStatus.UNAUTHORIZED, "GET_TOKEN_ERROR");
    public static CommonException TOKEN_EXPIRED = new CommonException(HttpStatus.FORBIDDEN, "TOKEN_EXPIRED");
    public static CommonException FORBIDDEN = new CommonException(HttpStatus.FORBIDDEN, "FORBIDDEN");
    public static CommonException PAGE_NOT_FOUND = new CommonException(HttpStatus.NOT_FOUND, "PAGE_NOT_FOUND");
    public static CommonException ITEM_NOT_FOUND = new CommonException(HttpStatus.NOT_FOUND, "ITEM_NOT_FOUND");
    public static CommonException SERVER_ERROR = new CommonException(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_ERROR");
    public static CommonException ILLEGAL_STATE_ERROR = new CommonException(HttpStatus.INTERNAL_SERVER_ERROR, "ILLEGAL_STATE_ERROR");

}
