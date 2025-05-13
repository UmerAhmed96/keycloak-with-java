package com.brkz.demo.client;

import com.brkz.demo.config.KeycloakProperties;
import com.brkz.demo.constant.KeycloakConstants;
import com.brkz.demo.controller.dtos.request.RoleRequestDto;
import com.brkz.demo.controller.dtos.response.RoleResponseDto;
import com.brkz.demo.exception.KeycloakException;
import com.brkz.demo.exception.ResourceNotFoundException;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakRoleClient {
    private final KeycloakProperties properties;
    private final KeycloakTokenClient keycloakTokenClient;
    private final WebClient webClient;

    public RoleResponseDto createRole(RoleRequestDto roleDto) {
        try {
            String token = keycloakTokenClient.getAccessToken();
            String rolesUrl = String.format(KeycloakConstants.ROLES_PATH, properties.getRealm());
            log.debug("Creating role at URL: {}", rolesUrl);

            return webClient.post()
                    .uri(rolesUrl)
                    .header(HttpHeaders.AUTHORIZATION, KeycloakConstants.BEARER_PREFIX + token)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(roleDto)
                    .retrieve()
                    .bodyToMono(RoleResponseDto.class)
                    .block();
        } catch (WebClientResponseException.Forbidden e) {
            log.error("Permission denied. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Permission denied: User does not have required roles to create roles", e);
        } catch (WebClientResponseException e) {
            log.error("Failed to create role. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Failed to create role: " + e.getMessage(), e);
        }
    }

    public List<RoleResponseDto> getAllRoles() {
        try {
            String token = keycloakTokenClient.getAccessToken();
            String rolesUrl = String.format(KeycloakConstants.ROLES_PATH, properties.getRealm());
            log.debug("Getting all roles from URL: {}", rolesUrl);

            return webClient.get()
                    .uri(rolesUrl)
                    .header(HttpHeaders.AUTHORIZATION, KeycloakConstants.BEARER_PREFIX + token)
                    .retrieve()
                    .bodyToFlux(RoleResponseDto.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException.Forbidden e) {
            log.error("Permission denied. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Permission denied: User does not have required roles to view roles", e);
        } catch (WebClientResponseException e) {
            log.error("Failed to get roles. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Failed to get roles: " + e.getMessage(), e);
        }
    }

    public RoleResponseDto getRoleById(String id) {
        try {
            String token = keycloakTokenClient.getAccessToken();
            String roleUrl = String.format(KeycloakConstants.ROLE_BY_ID_PATH, properties.getRealm(), id);
            log.debug("Getting role from URL: {}", roleUrl);

            return webClient.get()
                    .uri(roleUrl)
                    .header(HttpHeaders.AUTHORIZATION, KeycloakConstants.BEARER_PREFIX + token)
                    .retrieve()
                    .bodyToMono(RoleResponseDto.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new ResourceNotFoundException("Role", "id", id);
        } catch (WebClientResponseException.Forbidden e) {
            log.error("Permission denied. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Permission denied: User does not have required roles to view roles", e);
        } catch (WebClientResponseException e) {
            log.error("Failed to get role. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Failed to get role: " + e.getMessage(), e);
        }
    }

    public RoleResponseDto updateRole(String id, RoleRequestDto roleDto) {
        try {
            String token = keycloakTokenClient.getAccessToken();
            String roleUrl = String.format(KeycloakConstants.ROLE_BY_ID_PATH, properties.getRealm(), id);
            log.debug("Updating role at URL: {}", roleUrl);

            return webClient.put()
                    .uri(roleUrl)
                    .header(HttpHeaders.AUTHORIZATION, KeycloakConstants.BEARER_PREFIX + token)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(roleDto)
                    .retrieve()
                    .bodyToMono(RoleResponseDto.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new ResourceNotFoundException("Role", "id", id);
        } catch (WebClientResponseException.Forbidden e) {
            log.error("Permission denied. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Permission denied: User does not have required roles to update roles", e);
        } catch (WebClientResponseException e) {
            log.error("Failed to update role. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Failed to update role: " + e.getMessage(), e);
        }
    }

    public void deleteRole(String id) {
        try {
            String token = keycloakTokenClient.getAccessToken();
            String roleUrl = String.format(KeycloakConstants.ROLE_BY_ID_PATH, properties.getRealm(), id);
            log.debug("Deleting role at URL: {}", roleUrl);

            webClient.delete()
                    .uri(roleUrl)
                    .header(HttpHeaders.AUTHORIZATION, KeycloakConstants.BEARER_PREFIX + token)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new ResourceNotFoundException("Role", "id", id);
        } catch (WebClientResponseException.Forbidden e) {
            log.error("Permission denied. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Permission denied: User does not have required roles to delete roles", e);
        } catch (WebClientResponseException e) {
            log.error("Failed to delete role. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Failed to delete role: " + e.getMessage(), e);
        }
    }
} 