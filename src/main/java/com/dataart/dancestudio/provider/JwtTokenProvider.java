package com.dataart.dancestudio.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dataart.dancestudio.exception.DecodeJwtTokenException;
import com.dataart.dancestudio.model.Role;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
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

        log.info("Access Jwt Token for email = {} has been generated.", userDetailsDto.getEmail());
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

        log.info("Refresh Jwt Token for email = {} has been generated.", userDetailsDto.getEmail());
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

        log.info("Jwt Token's \"Issued At\" for email = {} has been updated.", userDetailsDto.getEmail());
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
            log.info("Jwt Token is valid.");
            return true;
        } catch (final JWTVerificationException exception) {
            log.warn("Jwt Token is invalid!");
            return false;
        }
    }

    public String getEmail(final String token) {
        try {
            final DecodedJWT jwt = JWT.decode(token);
            final String email = jwt.getClaim("email").asString();
            log.info("Jwt Token is valid. Email = {} has been received.", email);
            return email;
        } catch (final JWTDecodeException exception) {
            log.warn("Jwt Token is invalid! Can't get email!");
            throw new DecodeJwtTokenException("Jwt Token is invalid!");
        }
    }

}
