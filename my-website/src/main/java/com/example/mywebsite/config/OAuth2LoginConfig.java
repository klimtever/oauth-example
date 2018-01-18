package com.example.mywebsite.config;

import feign.RequestInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.ArrayList;
import java.util.Date;

/**
 * The OAuth2 login configuration for My Website.
 */
@Configuration
@EnableOAuth2Client
public class OAuth2LoginConfig {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.myWebsiteClientRegistration());
    }

    private ClientRegistration myWebsiteClientRegistration() {
        return ClientRegistration
                .withRegistrationId("myauth")
                .clientId("mywebsite")
                .clientSecret("verysecretpassword")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUriTemplate("http://localhost:8080/login/oauth2/code/myauth")
                .scope("myscope")
                .authorizationUri("http://localhost:9090/auth/oauth/authorize")
                .tokenUri("http://localhost:9090/auth/oauth/token")
                .userInfoUri("http://localhost:9090/auth/user")
                .userNameAttributeName("user")
                .clientName("my auth server")
                .build();
    }

    private OAuth2ProtectedResourceDetails resource() {
        ClientRegistration myauthClient = myWebsiteClientRegistration();
        AuthorizationCodeResourceDetails resource = new AuthorizationCodeResourceDetails();
        resource.setScope(new ArrayList<>(myauthClient.getScopes()));
        resource.setAccessTokenUri(myauthClient.getProviderDetails().getTokenUri());
        resource.setUserAuthorizationUri(myauthClient.getProviderDetails().getAuthorizationUri());
        resource.setClientId(myauthClient.getClientId());
        resource.setClientSecret(myauthClient.getClientSecret());
        resource.setPreEstablishedRedirectUri(myauthClient.getRedirectUriTemplate());
        resource.setUseCurrentUri(false);
        resource.setUserAuthorizationUri(myauthClient.getProviderDetails().getUserInfoEndpoint().getUri());
        resource.setId(myauthClient.getClientId());
        resource.setGrantType(AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
        return resource;
    }

    @Bean
    public RequestInterceptor requestTokenBearerInterceptor() {

        return requestTemplate -> {
            logTokenInfo();
            oAuth2ClientContext.setAccessToken(getAccessTokenFromAuthorizedClient());
            new OAuth2FeignRequestInterceptor(oAuth2ClientContext, resource()).apply(requestTemplate);
        };
    }

    private OAuth2AccessToken getAccessTokenFromAuthorizedClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("myauth", token.getName());
        org.springframework.security.oauth2.core.OAuth2AccessToken accessToken = client.getAccessToken();

        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken.getTokenValue());
        defaultOAuth2AccessToken.setExpiration(new Date(accessToken.getExpiresAt().toEpochMilli()));
        defaultOAuth2AccessToken.setScope(accessToken.getScopes());
        defaultOAuth2AccessToken.setTokenType(accessToken.getTokenType().getValue());
        return defaultOAuth2AccessToken;

    }

    private void logTokenInfo() {
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if(oAuth2ClientContext.getAccessToken() == null) {
            logger.warn("OAuth2ClientContext doesn't contain an access token!");
        }

        if(oAuth2ClientContext.getAccessTokenRequest() == null) {
            logger.warn("OAuth2ClientContext doesn't contain an access token request!");
        }

        if(oAuth2ClientContext.getAccessTokenRequest().getExistingToken() == null) {
            logger.warn("OAuth2ClientContext access token request doesn't contain an existing token in the acces token request!");
        }

        if(authorizedClientService.loadAuthorizedClient("myauth", authentication.getName()) == null) {
            logger.warn("AuthorizedClientService doesn't contain an access token.");
        }
    }


}