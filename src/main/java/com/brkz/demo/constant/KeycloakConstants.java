package com.brkz.demo.constant;

public final class KeycloakConstants {
    private KeycloakConstants() {
        // Private constructor to prevent instantiation
    }

    public static final String TOKEN_PATH = "/realms/%s/protocol/openid-connect/token";
    public static final String USERS_PATH = "/admin/realms/%s/users";
    public static final String USER_BY_ID_PATH = "/admin/realms/%s/users/%s";
    public static final String USER_SEARCH_PATH = "/admin/realms/%s/users?username=%s";
    
    public static final String ROLES_PATH = "/admin/realms/%s/roles";
    public static final String ROLE_BY_ID_PATH = "/admin/realms/%s/roles/%s";
    
    public static final String GRANT_TYPE = "password";
    public static final String SCOPE = "openid";
    
    public static final String BEARER_PREFIX = "Bearer ";
} 