package com.boilerplate.oauth.backend.controller;

import com.boilerplate.oauth.backend.model.dto.AuthDTO;
import com.boilerplate.oauth.backend.model.dto.BaseResponse;
import com.boilerplate.oauth.backend.model.dto.UserDTO;
import com.boilerplate.oauth.backend.model.entity.User;
import com.boilerplate.oauth.backend.service.UserService;
import com.boilerplate.oauth.backend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/join")
    public BaseResponse join(@RequestParam String email, @RequestParam String password, @RequestParam String nickname) {

        AuthDTO authDTO = authService.joinAuth(email, password, nickname);
        User user = userService.syncUserWithDuplicateError(authDTO.uid(), authDTO.nickname());

        return BaseResponse.of(UserDTO.builder()
                .uid(user.getUid())
                .nickname(user.getNickname())
                .type(user.getType())
                .build());
    }

}
