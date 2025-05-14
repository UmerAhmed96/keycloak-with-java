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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakRoleClient {
    private final WebClient webClient;
    private final KeycloakProperties keycloakProperties;
    private final KeycloakTokenClient keycloakTokenClient;

    public RoleResponseDto createRole(RoleRequestDto roleDto) {
        String token = keycloakTokenClient.getAccessToken();
        String url = String.format("%s/admin/realms/%s/roles",
                keycloakProperties.getBaseUrl(),
                keycloakProperties.getRealm());
        
        log.debug("Creating role at URL: {}", url);

        try {
            webClient.post()
                    .uri(url)
                    .header("Authorization", KeycloakConstants.BEARER_PREFIX + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(roleDto))
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            return RoleResponseDto.builder()
                    .name(roleDto.getName())
                    .description(roleDto.getDescription())
                    .build();
        } catch (Exception e) {
            log.error("Failed to create role. Error: {}", e.getMessage());
            throw new KeycloakException("Failed to create role", e);
        }
    }

    public List<RoleResponseDto> getAllRoles() {
        String token = keycloakTokenClient.getAccessToken();
        String url = String.format("%s/admin/realms/%s/roles",
                keycloakProperties.getBaseUrl(),
                keycloakProperties.getRealm());
        
        log.debug("Getting all roles from URL: {}", url);

        try {
            List<Map<String, Object>> roles = webClient.get()
                    .uri(url)
                    .header("Authorization", KeycloakConstants.BEARER_PREFIX + token)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();

            return roles.stream()
                    .map(role -> RoleResponseDto.builder()
                            .name((String) role.get("name"))
                            .description((String) role.get("description"))
                            .build())
                    .toList();
        } catch (Exception e) {
            log.error("Failed to get roles. Error: {}", e.getMessage());
            throw new KeycloakException("Failed to get roles", e);
        }
    }

    public RoleResponseDto getRoleById(String id) {
        String token = keycloakTokenClient.getAccessToken();
        String url = String.format("%s/admin/realms/%s/roles/%s",
                keycloakProperties.getBaseUrl(),
                keycloakProperties.getRealm(),
                id);
        
        log.debug("Getting role by ID from URL: {}", url);

        try {
            Map<String, Object> role = webClient.get()
                    .uri(url)
                    .header("Authorization", KeycloakConstants.BEARER_PREFIX + token)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return RoleResponseDto.builder()
                    .name((String) role.get("name"))
                    .description((String) role.get("description"))
                    .build();
        } catch (Exception e) {
            log.error("Failed to get role by ID. Error: {}", e.getMessage());
            throw new KeycloakException("Failed to get role by ID", e);
        }
    }

    public RoleResponseDto updateRole(String id, RoleRequestDto roleDto) {
        String token = keycloakTokenClient.getAccessToken();
        String url = String.format("%s/admin/realms/%s/roles/%s",
                keycloakProperties.getBaseUrl(),
                keycloakProperties.getRealm(),
                id);
        
        log.debug("Updating role at URL: {}", url);

        try {
            webClient.put()
                    .uri(url)
                    .header("Authorization", KeycloakConstants.BEARER_PREFIX + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(roleDto))
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            return RoleResponseDto.builder()
                    .name(roleDto.getName())
                    .description(roleDto.getDescription())
                    .build();
        } catch (Exception e) {
            log.error("Failed to update role. Error: {}", e.getMessage());
            throw new KeycloakException("Failed to update role", e);
        }
    }

    public void deleteRole(String id) {
        String token = keycloakTokenClient.getAccessToken();
        String url = String.format("%s/admin/realms/%s/roles/%s",
                keycloakProperties.getBaseUrl(),
                keycloakProperties.getRealm(),
                id);
        
        log.debug("Deleting role at URL: {}", url);

        try {
            webClient.delete()
                    .uri(url)
                    .header("Authorization", KeycloakConstants.BEARER_PREFIX + token)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            log.error("Failed to delete role. Error: {}", e.getMessage());
            throw new KeycloakException("Failed to delete role", e);
        }
    }
} 