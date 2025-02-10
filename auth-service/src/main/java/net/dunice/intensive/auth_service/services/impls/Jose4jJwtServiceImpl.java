package net.dunice.intensive.auth_service.services.impls;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.dunice.intensive.auth_service.services.JwtService;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Jose4jJwtServiceImpl implements JwtService {
    private static final int RSA_KEY_SIZE = 2048;

    private static final String ISSUER = "dunice-server";

    private static final String AUDIENCE = "auth-client";

    private static final int EXPIRATION_MINUTES = 10;

    private static final String VERIFICATION_ALGORITHM = AlgorithmIdentifiers.RSA_USING_SHA256;

    private final UserDetailsService service;

    private final RsaJsonWebKey rsaKey;

    @SneakyThrows
    public Jose4jJwtServiceImpl(UserDetailsService service) {
        this.service = service;
        rsaKey = RsaJwkGenerator.generateJwk(RSA_KEY_SIZE);
        rsaKey.setKeyId(UUID.randomUUID().toString());
    }

    @Override
    public boolean isValid(String token) {
        String username = null;
        try {
            username = extractUsername(token);
            final var consumer = new JwtConsumerBuilder()
                    .setRequireJwtId()
                    .setRequireExpirationTime()
                    .setRequireSubject()
                    .setExpectedIssuer(ISSUER)
                    .setExpectedAudience(AUDIENCE)
                    .setExpectedSubject(username)
                    .setVerificationKey(rsaKey.getKey())
                    .setJwsAlgorithmConstraints(ConstraintType.PERMIT, VERIFICATION_ALGORITHM)
                    .build();

            final var claims = consumer.processToClaims(token);
            final var expectedRoles = claims.getStringListClaimValue(JwtService.ROLES_CLAIM);

            final var userDetails = service.loadUserByUsername(username);
            final var userRoles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            return userRoles.containsAll(expectedRoles);
        } catch (InvalidJwtException | UsernameNotFoundException | MalformedClaimException e) {
            log.warn("Invalid token for user: {} - {};\nJWT = {}", username, e.getMessage(), token);
            return false;
        }
    }

    @Override
    @SneakyThrows
    public String extractUsername(String token) {
        return new JwtConsumerBuilder()
                .build()
                .processToClaims(token)
                .getStringClaimValue(JwtService.USERNAME_CLAIM);
    }

    @Override
    @SneakyThrows
    public List<String> extractAuthorities(String token) {
        return new JwtConsumerBuilder()
                .build()
                .processToClaims(token)
                .getStringListClaimValue(JwtService.ROLES_CLAIM);
    }

    @Override
    @SneakyThrows
    public String generateToken(String username, List<String> authorities) {
        final var claims = new JwtClaims();
        claims.setIssuer(ISSUER);
        claims.setAudience(AUDIENCE);
        claims.setSubject(username);
        claims.setExpirationTimeMinutesInTheFuture(EXPIRATION_MINUTES);
        claims.setStringClaim(JwtService.USERNAME_CLAIM, username);
        claims.setStringListClaim(JwtService.ROLES_CLAIM, authorities);

        final var jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaKey.getPrivateKey());
        jws.setKeyIdHeaderValue(rsaKey.getKeyId());
        jws.setAlgorithmHeaderValue(VERIFICATION_ALGORITHM);

        return jws.getCompactSerialization();
    }
}
