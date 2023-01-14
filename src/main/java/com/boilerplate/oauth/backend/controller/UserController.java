package com.boilerplate.oauth.backend.controller;

import com.boilerplate.oauth.backend.model.dto.BaseResponse;
import com.boilerplate.oauth.backend.model.dto.UserDTO;
import com.boilerplate.oauth.backend.annotation.AuthResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    @GetMapping
    public BaseResponse me(@AuthResult UserDTO userDTO) {
        return BaseResponse.of(userDTO);
    }
}
