package net.dunice.intensive.auth_service.services;

import java.util.List;

public interface JwtService {
    String USERNAME_CLAIM = "username";

    String ROLES_CLAIM = "roles";

    String BEARER_PREFIX = "Bearer ";

    boolean isValid(String token);

    String extractUsername(String token);

    List<String> extractAuthorities(String token);

    String generateToken(String username, List<String> authorities);
}
