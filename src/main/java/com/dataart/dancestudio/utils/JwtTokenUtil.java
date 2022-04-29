package com.dataart.dancestudio.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;


@Component
public class JwtTokenUtil implements Serializable {

    @Value("${jwt.expirationInSeconds}")
    public long jwtTokenExpirationInSeconds;

    @Value("${jwt.secret}")
    private String secret;

    public String generateAccessToken(final UserDetailsDto userDetailsDto) throws IllegalArgumentException, JWTCreationException {
        final Role role = userDetailsDto.getRoles().stream().findFirst().orElse(Role.USER);
        return JWT.create()
                .withIssuer("Dance Studio")
                .withIssuedAt(new Date())
                .withSubject("User Details")
                .withClaim("id", userDetailsDto.getId())
                .withClaim("email", userDetailsDto.getEmail())
                .withClaim("role", role.name())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(jwtTokenExpirationInSeconds)))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveSubject(final String token) throws JWTVerificationException {
        final JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("Dance Studio")
                .withSubject("User Details")
                .build();
        final DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }

}
