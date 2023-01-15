package com.boilerplate.oauth.backend.model.entity;

import com.boilerplate.oauth.backend.model.enums.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String uid;
    @Column(unique = true, nullable = false, length = 16)
    private String nickname;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type;
    @Column(nullable = false)
    private Instant createDt;
    @Column(nullable = false)
    private Instant updateDt;

    public User(String uid, @Nullable String nickname, UserType type) {
        this.uid = uid;
        this.nickname = nickname;
        this.type = type;
    }

    @PrePersist
    void preInsert() {
        Instant now = Instant.now();
        if (Objects.isNull(this.createDt)) {
            this.createDt = now;
        }
        if (Objects.isNull(this.updateDt)) {
            this.updateDt = now;
        }
    }

    @PreUpdate
    void preUpdate() {
        this.updateDt = Instant.now();
    }

    public boolean isBlock() {
        return type == UserType.BLOCK;
    }
}
