package com.petplate.petplate.user.domain.entity;

import com.petplate.petplate.common.Inheritance.BaseEntity;
import com.petplate.petplate.user.domain.Role;
import com.petplate.petplate.user.domain.SocialType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String name;

    @Column(length = 50,nullable = false)
    private String username;

    @Column(length = 100,nullable = false)
    private String password;

    @Column(length = 13)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean isReceiveAd;

    @Column(nullable = false)
    private boolean activated;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Builder
    public User(Role role, String name, String username, String password,
            String phoneNumber,
            boolean isReceiveAd, boolean activated,SocialType socialType) {
        this.role = role;
        this.name = name;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isReceiveAd = isReceiveAd;
        this.activated = activated;
        this.level = 1;
        this.socialType = socialType;
    }
}
