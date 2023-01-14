package com.boilerplate.oauth.backend.model.dto;

import com.boilerplate.oauth.backend.model.enums.UserType;
import lombok.Builder;

@Builder
public record UserDTO(String uid, String nickname, UserType type) {
}
