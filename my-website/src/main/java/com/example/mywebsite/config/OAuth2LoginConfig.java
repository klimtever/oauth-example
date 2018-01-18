package com.example.mywebsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

/**
 * The OAuth2 login configuration for My Website.
 */
@Configuration
@EnableOAuth2Client
public class OAuth2LoginConfig {

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

}