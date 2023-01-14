package com.boilerplate.oauth.backend.exception;

import com.boilerplate.oauth.backend.model.dto.BaseResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public BaseResponse requestBodyIsWrong(HttpMessageNotReadableException e) {
        log.debug("[requestBodyIsWrong]", e);
        Throwable throwable = e.getMostSpecificCause();
        if (throwable instanceof InvalidFormatException) {
            return BaseResponse.exception(CommonException.INVALID_PARAMETER);
        } else {
            return BaseResponse.exception(CommonException.MISSING_PARAMETER);
        }
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, MultipartException.class})
    public BaseResponse invalidParameter(Exception e) {
        log.debug("[invalidParameter]: {}", e.getMessage());
        return BaseResponse.exception(CommonException.INVALID_PARAMETER);
    }

    @ExceptionHandler({BaseException.class})
    public BaseResponse customExceptionHandler(BaseException baseException) {
        log.debug(baseException.getErrorDescription(), baseException);
        return BaseResponse.exception(baseException);
    }

    @ExceptionHandler({NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public BaseResponse noHandlerFoundException(Exception e) {
        log.debug("[noHandlerFoundException]", e);
        return BaseResponse.exception(CommonException.PAGE_NOT_FOUND);
    }

    @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
    public BaseResponse wrongAccessExceptionHandler(Exception e) {
        log.debug("[wrongAccessExceptionHandler]", e);
        return BaseResponse.exception(CommonException.UNAUTHORIZED);
    }

    @ExceptionHandler({Exception.class})
    public BaseResponse exceptionHandler(Exception e) {
        log.debug(e.getMessage(), e);
        return BaseResponse.exception(CommonException.SERVER_ERROR);
    }
}
