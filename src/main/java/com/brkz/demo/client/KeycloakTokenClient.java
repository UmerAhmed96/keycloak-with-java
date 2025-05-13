package com.brkz.demo.client;

import com.brkz.demo.config.KeycloakProperties;
import com.brkz.demo.constant.KeycloakConstants;
import com.brkz.demo.exception.KeycloakException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakTokenClient {
    private final KeycloakProperties properties;
    private final WebClient webClient;

    public String getAccessToken() {
        try {
            String tokenUrl = String.format(KeycloakConstants.TOKEN_PATH, properties.getRealm());
            log.debug("Requesting token from URL: {}", tokenUrl);
            
            Map<String, Object> response = webClient.post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters
                            .fromFormData("grant_type", KeycloakConstants.GRANT_TYPE)
                            .with("client_id", properties.getClientId())
                            .with("client_secret", properties.getClientSecret())
                            .with("username", properties.getUsername())
                            .with("password", properties.getPassword())
                            .with("scope", KeycloakConstants.SCOPE))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("access_token")) {
                log.debug("Successfully obtained access token");
                return (String) response.get("access_token");
            } else {
                log.error("Token response does not contain access_token. Response: {}", response);
                throw new KeycloakException("Failed to get access token: No token in response");
            }
        } catch (WebClientResponseException.Unauthorized e) {
            log.error("Authentication failed. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Authentication failed: Invalid credentials", e);
        } catch (WebClientResponseException e) {
            log.error("Keycloak request failed. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Failed to get access token: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while getting access token", e);
            throw new KeycloakException("Unexpected error while getting access token: " + e.getMessage(), e);
        }
    }
}
