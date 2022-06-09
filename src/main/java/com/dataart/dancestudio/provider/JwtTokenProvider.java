package com.dataart.dancestudio.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenProvider implements Serializable {

    @Value("${jwt.access.expirationInMinutes}")
    public long accessTokenExpirationInMinutes;

    @Value("${jwt.refresh.expirationInDays}")
    public long refreshTokenExpirationInDays;

    @Value("${jwt.access.secret}")
    private String accessSecret;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    public String generateAccessToken(final UserDetailsDto userDetailsDto) throws IllegalArgumentException, JWTCreationException {
        final Role role = userDetailsDto.getRoles().stream().findFirst().orElse(Role.USER);
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(accessTokenExpirationInMinutes)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);

        return JWT.create()
                .withIssuer("Dance Studio")
                .withIssuedAt(new Date())
                .withSubject(userDetailsDto.getId().toString())
                .withClaim("email", userDetailsDto.getEmail())
                .withClaim("role", role.name())
                .withExpiresAt(accessExpiration)
                .sign(Algorithm.HMAC256(accessSecret));
    }

    public String generateRefreshToken(final UserDetailsDto userDetailsDto) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(refreshTokenExpirationInDays)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);

        return JWT.create()
                .withIssuer("Dance Studio")
                .withIssuedAt(new Date())
                .withSubject(userDetailsDto.getId().toString())
                .withClaim("email", userDetailsDto.getEmail())
                .withExpiresAt(refreshExpiration)
                .sign(Algorithm.HMAC256(refreshSecret));
    }

    public String updateIssuedAtOfRefreshToken(final UserDetailsDto userDetailsDto, final String oldRefreshToken) {
        final DecodedJWT jwt = JWT.decode(oldRefreshToken);
        final Date oldRefreshExpiration = jwt.getExpiresAt();

        return JWT.create()
                .withIssuer("Dance Studio")
                .withIssuedAt(new Date())
                .withSubject(userDetailsDto.getId().toString())
                .withClaim("email", userDetailsDto.getEmail())
                .withExpiresAt(oldRefreshExpiration)
                .sign(Algorithm.HMAC256(refreshSecret));
    }

    public boolean validateAccessToken(final String token) {
        return validateToken(token, accessSecret);
    }

    public boolean validateRefreshToken(final String token) {
        return validateToken(token, refreshSecret);
    }

    private boolean validateToken(final String token, final String secret) {
        try {
            final JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            verifier.verify(token);
            return true;
        } catch (final JWTVerificationException exception) {
            return false;
        }
    }

    public String getEmail(final String token) {
        final DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("email").asString();
    }

}
