package com.boilerplate.oauth.backend.controller;

import com.boilerplate.oauth.backend.model.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/login")
    public BaseResponse login() {
        return BaseResponse.of();
    }


}
