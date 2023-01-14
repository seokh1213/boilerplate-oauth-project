package com.boilerplate.oauth.backend.model.entity;

import com.boilerplate.oauth.backend.model.enums.AuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String uid;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    private @Nullable String encryptedPassword;
    @Column(nullable = false)
    private Instant createDt;
    @Column(nullable = false)
    private Instant updateDt;

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

    public Auth(String uid, String email, String encryptedPassword) {
        this.uid = uid;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
    }

    public Auth(String uid, String email, AuthProvider provider) {
        this.uid = uid;
        this.email = email;
        this.provider = provider;
    }
}
