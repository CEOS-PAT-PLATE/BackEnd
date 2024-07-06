package com.petplate.petplate.auth.oauth.service;

import com.petplate.petplate.auth.oauth.CustomOAuth2User;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Component
public class CustomOAuth2LoginAuthenticationProvider implements AuthenticationProvider {

    private final OAuth2AuthorizationCodeAuthenticationProvider authorizationCodeAuthenticationProvider;

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> userService;

    private final SocialLoginTokenUtil socialLoginTokenUtil;

    private static final String SOCIAL_LOGIN_REFRESH_TOKEN = "SocialLoginRefreshToken";

    private GrantedAuthoritiesMapper authoritiesMapper = ((authorities) -> authorities);

    /**
     * Constructs an {@code OAuth2LoginAuthenticationProvider} using the provided
     * parameters.
     * @param accessTokenResponseClient the client used for requesting the access token
     * credential from the Token Endpoint
     * @param userService the service used for obtaining the user attributes of the
     * End-User from the UserInfo Endpoint
     */
    @Autowired
    public CustomOAuth2LoginAuthenticationProvider(
            OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> userService,
            SocialLoginTokenUtil socialLoginTokenUtil) {
        Assert.notNull(userService, "userService cannot be null");
        this.authorizationCodeAuthenticationProvider = new OAuth2AuthorizationCodeAuthenticationProvider(
                accessTokenResponseClient);
        this.userService = userService;
        this.socialLoginTokenUtil = socialLoginTokenUtil;

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2LoginAuthenticationToken loginAuthenticationToken = (OAuth2LoginAuthenticationToken) authentication;
        // Section 3.1.2.1 Authentication Request -
        // https://openid.net/specs/openid-connect-core-1_0.html#AuthRequest scope
        // REQUIRED. OpenID Connect requests MUST contain the "openid" scope value.
        if (loginAuthenticationToken.getAuthorizationExchange()
                .getAuthorizationRequest()
                .getScopes()
                .contains("openid")) {
            // This is an OpenID Connect Authentication Request so return null
            // and let OidcAuthorizationCodeAuthenticationProvider handle it instead
            return null;
        }
        OAuth2AuthorizationCodeAuthenticationToken authorizationCodeAuthenticationToken;
        try {
            authorizationCodeAuthenticationToken = (OAuth2AuthorizationCodeAuthenticationToken) this.authorizationCodeAuthenticationProvider
                    .authenticate(
                            new OAuth2AuthorizationCodeAuthenticationToken(loginAuthenticationToken.getClientRegistration(),
                                    loginAuthenticationToken.getAuthorizationExchange()));
        }
        catch (OAuth2AuthorizationException ex) {
            OAuth2Error oauth2Error = ex.getError();
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
        }
        OAuth2AccessToken accessToken = authorizationCodeAuthenticationToken.getAccessToken();
        Map<String, Object> additionalParameters = authorizationCodeAuthenticationToken.getAdditionalParameters();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) this.userService.loadUser(new OAuth2UserRequest(
                loginAuthenticationToken.getClientRegistration(), accessToken, additionalParameters));

        System.out.println(authorizationCodeAuthenticationToken.getRefreshToken().getTokenValue());

        socialLoginTokenUtil.saveSocialLoginRefreshToken(customOAuth2User.getUsername(), authorizationCodeAuthenticationToken.getRefreshToken().getTokenValue());


        Collection<? extends GrantedAuthority> mappedAuthorities = this.authoritiesMapper
                .mapAuthorities(customOAuth2User.getAuthorities());
        OAuth2LoginAuthenticationToken authenticationResult = new OAuth2LoginAuthenticationToken(
                loginAuthenticationToken.getClientRegistration(), loginAuthenticationToken.getAuthorizationExchange(),
                customOAuth2User, mappedAuthorities, accessToken, authorizationCodeAuthenticationToken.getRefreshToken());
        authenticationResult.setDetails(loginAuthenticationToken.getDetails());
        return authenticationResult;
    }

    /**
     * Sets the {@link GrantedAuthoritiesMapper} used for mapping
     * {@link OAuth2User#getAuthorities()} to a new set of authorities which will be
     * associated to the {@link OAuth2LoginAuthenticationToken}.
     * @param authoritiesMapper the {@link GrantedAuthoritiesMapper} used for mapping the
     * user's authorities
     */
    public final void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
        Assert.notNull(authoritiesMapper, "authoritiesMapper cannot be null");
        this.authoritiesMapper = authoritiesMapper;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2LoginAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

