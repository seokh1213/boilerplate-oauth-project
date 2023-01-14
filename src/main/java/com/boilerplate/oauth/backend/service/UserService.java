package com.boilerplate.oauth.backend.service;

import com.boilerplate.oauth.backend.model.entity.User;
import com.boilerplate.oauth.backend.model.enums.UserType;
import com.boilerplate.oauth.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User syncUser(String uid, String email, String nickname) {

        User user = userRepository.findByUid(uid)
                .orElseGet(()-> new User(uid, nickname, UserType.USER));

        return userRepository.save(user);
    }

    public Optional<User> getUserByUid(String uid) {
        return userRepository.findByUid(uid);
    }
}
