package com.example.mywebsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.web.client.RestOperations;

/**
 * The OAuth2 login configuration for My Website.
 */
@Configuration
public class OAuth2LoginConfig {

    @Bean
    public RestOperations restOperations(OAuth2ProtectedResourceDetails resourceDetails,
                                             OAuth2ClientContext clientContext) {
        return new OAuth2RestTemplate(resourceDetails, clientContext);
    }

}