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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Users")
@SQLDelete(sql="UPDATE users set deleted = true where user_id = ?")
@Where(clause = "deleted = false")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String name;

    @Column(length = 50,nullable = false)
    private String email;

    @Column(length = 100,nullable = false)
    private String socialLoginId;

    @Column(nullable = false)
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

    @Column(nullable = false)
    private boolean deleted = false;

    private String socialLoginRefreshToken;

    @Builder
    public User(Role role, String name, String password,
            String phoneNumber,String socialLoginId,
            boolean isReceiveAd, boolean activated,SocialType socialType, String email) {
        this.role = role;
        this.name = name;
        this.socialLoginId = socialLoginId;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isReceiveAd = isReceiveAd;
        this.activated = activated;
        this.level = 1;
        this.socialType = socialType;
        this.username = socialType+socialLoginId;
        socialLoginRefreshToken = null;
        this.email = email;
    }

    public void changeSocialLoginRefreshToken(final String socialLoginRefreshToken){
        this.socialLoginRefreshToken = socialLoginRefreshToken;
    }

    public void changeEmailBySocialLogin(final String email){
        this.email = email;
    }

    public void changeMyRole(){

        if(this.role == Role.GENERAL){
            this. role = Role.ADMIN;
            return;
        }

        this.role = Role.GENERAL;

    }
}
