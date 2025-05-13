package com.brkz.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final KeycloakProperties properties;

    @Bean
    public WebClient webClient() {
        // Ensure the baseUrl is properly formatted
        String formattedBaseUrl = properties.getBaseUrl().endsWith("/") 
            ? properties.getBaseUrl().substring(0, properties.getBaseUrl().length() - 1) 
            : properties.getBaseUrl();
            
        return WebClient.builder()
                .baseUrl(formattedBaseUrl)
                .build();
    }
}
