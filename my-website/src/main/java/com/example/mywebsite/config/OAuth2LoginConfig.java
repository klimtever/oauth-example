package com.example.mywebsite.config;

import feign.RequestInterceptor;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * The OAuth2 login configuration for My Website.
 */
@Configuration
@EnableOAuth2Client
public class OAuth2LoginConfig {

    @Bean
    public RequestInterceptor requestTokenBearerInterceptor(OAuth2ClientContext oAuth2ClientContext,
                                                            OAuth2ProtectedResourceDetails resourceDetails) {
        return new OAuth2FeignRequestInterceptor(oAuth2ClientContext, resourceDetails);
    }
}