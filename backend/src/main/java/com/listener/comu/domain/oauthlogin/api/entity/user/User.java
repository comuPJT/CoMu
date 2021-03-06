package com.listener.comu.domain.oauthlogin.api.entity.user;

import com.listener.comu.domain.oauthlogin.oauth.entity.ProviderType;
import com.listener.comu.domain.oauthlogin.oauth.entity.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER")
public class User {
    @JsonIgnore
    @Id
    @Column(name = "USER_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userSeq;

    @Column(name = "USER_ID", length = 64, unique = true)
    @Size(max = 64)
    private String userId;

    @Column(name = "USERNAME", length = 100)
    @Size(max = 100)
    private String username;

    @JsonIgnore
    @Column(name = "PASSWORD", length = 128)
    @Size(max = 128)
    private String password;

    @Column(name = "EMAIL", length = 512, unique = true)
    @Size(max = 512)
    private String email;

    @Column(name = "EMAIL_VERIFIED_YN", length = 1)
    @Size(min = 1, max = 1)
    private String emailVerifiedYn;

    @Column
    @NotNull
    private int characterNum;

    @Column(name = "PROVIDER_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(name = "ROLE_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleType roleType;

    @Column(name = "CREATED_AT")
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT")
    @NotNull
    private LocalDateTime modifiedAt;

    @Column(name = "FIRST_VISIT_YN")
    @NotNull
    private String firstVisitYn;

    public User(
            @Size(max = 64) String userId,
            @Size(max = 100) String username,
            @Size(max = 512) String email,
            @Size(max = 1) String emailVerifiedYn,
            @NotNull ProviderType providerType,
            @NotNull RoleType roleType,
            @NotNull LocalDateTime createdAt,
            @NotNull LocalDateTime modifiedAt,
            @NotNull String firstVisitYn
    ) {
        this.userId = userId;
        this.username = username;
        this.password = "NO_PASS";
//        this.email = email != null ? email : "NO_EMAIL";
        this.email = email;
        this.emailVerifiedYn = emailVerifiedYn;
        this.providerType = providerType;
        this.roleType = roleType;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.firstVisitYn = firstVisitYn;
    }
}