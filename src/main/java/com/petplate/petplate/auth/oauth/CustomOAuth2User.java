package com.petplate.petplate.auth.oauth;

import com.petplate.petplate.user.domain.Role;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String username;
    private Role role;


    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attributes, String nameAttributeKey,
            String username,Role role){

        super(authorities,attributes,nameAttributeKey);
        this.username = username;
        this.role = role;

    }
}

