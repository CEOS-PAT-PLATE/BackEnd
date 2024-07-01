package com.petplate.petplate.auth.oauth.userinfo;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo{

    private static final String userInfoName = "response";
    private static final String ID="id";
    private static final String NAME="name";
    private static final String EMAIL="email";

    private static final String MOBILE="mobile";
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {

        Map<String, Object> response = (Map<String, Object>) attributes.get(userInfoName);

        if (response == null) {
            return null;
        }

        return (String) response.get(ID);
    }

    @Override
    public String getName() {

        Map<String, Object> response = (Map<String, Object>) attributes.get(userInfoName);

        if (response == null) {
            return null;
        }

        return (String) response.get(NAME);
    }

    @Override
    public String getEmail() {

        Map<String, Object> response = (Map<String, Object>) attributes.get(userInfoName);

        if (response == null) {
            return null;
        }

        return (String) response.get(EMAIL);
    }

  /*  @Override
    public String getPhoneNumber(){

        Map<String,Object> response = (Map<String, Object>) attributes.get(userInfoName);


        if(response == null){
            return null;
        }

        return (String) response.get(MOBILE);
    }*/
}

