package com.petplate.petplate.user.dto.response;

import com.petplate.petplate.user.domain.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MyProfileResponseDto {

    private String loginMethod;
    private String name;
    private String email;
    boolean isReceiveAd;

    public static MyProfileResponseDto from(User user){
        return new MyProfileResponseDto(user.getSocialType().name(),user.getName(),user.getEmail(),
                user.isReceiveAd());
    }

}
