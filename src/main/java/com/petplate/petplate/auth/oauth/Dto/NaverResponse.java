package com.petplate.petplate.auth.oauth.Dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NaverResponse {

    private String id;
    private String name;
    private String email;


}
