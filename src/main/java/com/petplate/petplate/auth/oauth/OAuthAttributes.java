package com.petplate.petplate.auth.oauth;

import com.petplate.petplate.auth.oauth.userinfo.NaverOAuth2UserInfo;
import com.petplate.petplate.auth.oauth.userinfo.OAuth2UserInfo;
import com.petplate.petplate.user.domain.Role;
import com.petplate.petplate.user.domain.SocialType;
import com.petplate.petplate.user.domain.entity.User;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
    private String nameAttributeKey;//OAuth2 로그인 진행 시 키가 되는 필드 값,PK와 같은 의미
    private OAuth2UserInfo oauth2UserInfo;

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo){
        this.nameAttributeKey=nameAttributeKey;
        this.oauth2UserInfo=oauth2UserInfo;
    }
    public static OAuthAttributes of(SocialType socialType,
            String userNameAttributeName, Map<String, Object> attributes) {

        if (socialType == SocialType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }

        return null;
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {

        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }


    public User toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {

        User user = User.builder()
                .name(oauth2UserInfo.getName())
                .role(Role.GENERAL)
                .activated(false)
                .isReceiveAd(false)
               // .phoneNumber(oauth2UserInfo.getPhoneNumber())
                .password(UUID.randomUUID()+"password")
                .socialType(socialType)
                .socialLoginId(oauth2UserInfo.getId())
                .email(oauth2UserInfo.getEmail())
                .build();

        return user;
    }
}
