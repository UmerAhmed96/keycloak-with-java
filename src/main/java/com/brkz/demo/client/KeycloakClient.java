package com.brkz.demo.client;

import com.brkz.demo.config.KeycloakProperties;
import com.brkz.demo.constant.KeycloakConstants;
import com.brkz.demo.controller.dtos.request.UserRequestDto;
import com.brkz.demo.controller.dtos.response.UserResponseDto;
import com.brkz.demo.exception.KeycloakException;
import com.brkz.demo.exception.ResourceNotFoundException;
import com.brkz.demo.mapper.UserMapper;
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
public class KeycloakClient {
    private final KeycloakProperties properties;
    private final KeycloakTokenClient keycloakTokenClient;
    private final WebClient webClient;
    private final UserMapper userMapper;

    public UserResponseDto createUser(UserRequestDto userDto) {
        try {
            String token = keycloakTokenClient.getAccessToken();
            String usersUrl = String.format(KeycloakConstants.USERS_PATH, properties.getRealm());
            log.debug("Creating user at URL: {}", usersUrl);

            webClient.post()
                    .uri(usersUrl)
                    .header(HttpHeaders.AUTHORIZATION, KeycloakConstants.BEARER_PREFIX + token)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(userDto)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            String searchUrl = String.format(KeycloakConstants.USER_SEARCH_PATH, properties.getRealm(), userDto.getUsername());
            log.debug("Searching for created user at URL: {}", searchUrl);

            List<UserResponseDto> users = webClient.get()
                    .uri(searchUrl)
                    .header(HttpHeaders.AUTHORIZATION, KeycloakConstants.BEARER_PREFIX + token)
                    .retrieve()
                    .bodyToFlux(UserResponseDto.class)
                    .collectList()
                    .block();

            if (users != null && !users.isEmpty()) {
                UserResponseDto responseDto = users.get(0);
                userMapper.updateResponseDtoFromRequestDto(userDto, responseDto);
                return responseDto;
            } else {
                throw new KeycloakException("User was created but could not be retrieved");
            }
        } catch (WebClientResponseException.Forbidden e) {
            log.error("Permission denied. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Permission denied: User does not have required roles to create users", e);
        } catch (WebClientResponseException e) {
            log.error("Failed to create user. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Failed to create user: " + e.getMessage(), e);
        }
    }

    public List<UserResponseDto> getAllUsers() {
        try {
            String token = keycloakTokenClient.getAccessToken();
            String usersUrl = String.format(KeycloakConstants.USERS_PATH, properties.getRealm());
            log.debug("Getting all users from URL: {}", usersUrl);

            return webClient.get()
                    .uri(usersUrl)
                    .header(HttpHeaders.AUTHORIZATION, KeycloakConstants.BEARER_PREFIX + token)
                    .retrieve()
                    .bodyToFlux(UserResponseDto.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException.Forbidden e) {
            log.error("Permission denied. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Permission denied: User does not have required roles to view users", e);
        } catch (WebClientResponseException e) {
            log.error("Failed to get users. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Failed to get users: " + e.getMessage(), e);
        }
    }

    public UserResponseDto getUserById(String id) {
        try {
            String token = keycloakTokenClient.getAccessToken();
            String userUrl = String.format(KeycloakConstants.USER_BY_ID_PATH, properties.getRealm(), id);
            log.debug("Getting user from URL: {}", userUrl);

            return webClient.get()
                    .uri(userUrl)
                    .header(HttpHeaders.AUTHORIZATION, KeycloakConstants.BEARER_PREFIX + token)
                    .retrieve()
                    .bodyToMono(UserResponseDto.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new ResourceNotFoundException("User", "id", id);
        } catch (WebClientResponseException.Forbidden e) {
            log.error("Permission denied. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Permission denied: User does not have required roles to view users", e);
        } catch (WebClientResponseException e) {
            log.error("Failed to get user. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Failed to get user: " + e.getMessage(), e);
        }
    }

    public void deleteUser(String id) {
        try {
            String token = keycloakTokenClient.getAccessToken();
            String userUrl = String.format(KeycloakConstants.USER_BY_ID_PATH, properties.getRealm(), id);
            log.debug("Deleting user at URL: {}", userUrl);

            webClient.delete()
                    .uri(userUrl)
                    .header(HttpHeaders.AUTHORIZATION, KeycloakConstants.BEARER_PREFIX + token)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new ResourceNotFoundException("User", "id", id);
        } catch (WebClientResponseException.Forbidden e) {
            log.error("Permission denied. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Permission denied: User does not have required roles to delete users", e);
        } catch (WebClientResponseException e) {
            log.error("Failed to delete user. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new KeycloakException("Failed to delete user: " + e.getMessage(), e);
        }
    }
}
