package com.petplate.petplate.auth.oauth.service;

import com.petplate.petplate.auth.oauth.CustomOAuth2User;
import com.petplate.petplate.auth.oauth.OAuthAttributes;
import com.petplate.petplate.user.domain.SocialType;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.repository.UserRepository;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    private static final String NAVER="naver";

    private final RedisTemplate<String,String> redisTemplate;
    private static final String SOCIAL_LOGIN_ACCESS_TOKEN = "SocialLoginAccessToken";

    private static final Long SOCIAL_LOGIN_ACCESS_TOKEN_EXPIRE = 3600L;






    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate=new DefaultOAuth2UserService();
        OAuth2User oAuth2User=delegate.loadUser(userRequest);

        //1
        String registrationId=userRequest.getClientRegistration().getRegistrationId();//Naver or google
        SocialType socialType=getSocialType(registrationId);

        //2
        String userNameAttributeName=userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();//Naver :response Google: sub

        //3
        Map<String,Object> attributes=oAuth2User.getAttributes();



        OAuthAttributes extractAttributes=OAuthAttributes.of(socialType,userNameAttributeName,attributes);

        User createdMember=getUser(extractAttributes,socialType);

        /*
        String socialLoginAccessToken = userRequest.getAccessToken().getTokenValue();
        System.out.println("socialLoginAccessToken"+socialLoginAccessToken);
        */



        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdMember.getRole().name())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdMember.getUsername(),createdMember.getRole()
        );

    }
    //"naver"=>Enum
    private SocialType getSocialType(String registrationId){

        if(NAVER.equals(registrationId)){
            return SocialType.NAVER;
        }
        return null;
    }

    //email과 socialtype으로 member
    private User getUser(OAuthAttributes attributes,SocialType socialType){
        User  findUser=userRepository.findBySocialTypeAndUsername(socialType,attributes.getOauth2UserInfo().getEmail()).orElseGet(()->saveUser(attributes,socialType));
        return findUser;
    }

    // 소셜 로그인의 경우 비밀번호와 아이디는 저장되지 않는다. 따라서 아이디 와 비밀번호를 각각 소셜로그인 계정의 이메일과 UUID+"password" 로 임의로 설정한다.
    private User saveUser(OAuthAttributes attributes,SocialType socialType){
        User createdUser = attributes.toEntity(socialType,attributes.getOauth2UserInfo());
        return userRepository.save(createdUser);
    }
}
