package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.JwtTokenMapper;
import com.dataart.dancestudio.model.dto.JwtTokenDto;
import com.dataart.dancestudio.model.entity.JwtTokenEntity;
import com.dataart.dancestudio.model.entity.JwtTokenType;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.JwtTokenRepository;
import com.dataart.dancestudio.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtTokenRepository jwtTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenMapper jwtTokenMapper;

    @Autowired
    public JwtTokenServiceImpl(final JwtTokenRepository jwtTokenRepository, final UserRepository userRepository,
                               final JwtTokenMapper jwtTokenMapper) {
        this.jwtTokenRepository = jwtTokenRepository;
        this.userRepository = userRepository;
        this.jwtTokenMapper = jwtTokenMapper;
    }

    @Override
    public void createJwtToken(final JwtTokenDto jwtTokenDto) {
        final Optional<UserEntity> userEntity = userRepository.findByEmail(jwtTokenDto.getEmail());
        if (userEntity.isPresent()) {
            final JwtTokenEntity jwtTokenEntity = jwtTokenMapper.jwtTokenDtoToJwtTokenEntity(jwtTokenDto);
            jwtTokenEntity.setUser(userEntity.get());
            jwtTokenRepository.save(jwtTokenEntity);
            log.info("Token was created.");
        } else {
            log.info("Token wasn't created.");
        }
    }

    @Override
    public String getJwtTokenByEmail(final String email, final JwtTokenType type) {
        final Optional<JwtTokenEntity> jwtTokenEntity = jwtTokenRepository.findByUserEmailAndType(email, type);
        jwtTokenEntity.ifPresentOrElse(
                (token) -> log.info("Token with id = {} was found.", token.getId()),
                () -> log.info("Token wasn't found."));
        return jwtTokenEntity.orElseThrow().getToken();
    }

    @Override
    public boolean existsByToken(final String token) {
        final boolean existsByToken = jwtTokenRepository.existsByToken(token);
        if (existsByToken) {
            log.info("Token exists.");
        } else {
            log.info("Token doesn't exist.");
        }
        return existsByToken;
    }

    @Override
    public boolean existsByUserEmail(final String email) {
        final boolean existsByUserEmail = existsByUserEmailAndType(email, JwtTokenType.ACCESS)
                && existsByUserEmailAndType(email, JwtTokenType.REFRESH);
        if (existsByUserEmail) {
            log.info("Tokens exist.");
        } else {
            log.info("Tokens don't exist.");
        }
        return existsByUserEmail;
    }

    private boolean existsByUserEmailAndType(final String email, final JwtTokenType type) {
        return jwtTokenRepository.existsByUserEmailAndType(email, type);
    }

    @Override
    public void updateJwtToken(final JwtTokenDto jwtTokenDto) {
        final JwtTokenEntity jwtTokenEntity = jwtTokenRepository.findByUserEmailAndType(
                jwtTokenDto.getEmail(), jwtTokenDto.getType()).orElseThrow();
        jwtTokenMapper.mergeJwtTokenEntityAndJwtTokenDto(jwtTokenEntity, jwtTokenDto);
        final JwtTokenEntity updatedJwtTokenEntity = jwtTokenRepository.save(jwtTokenEntity);
        log.info("Token with id = {} was updated.", updatedJwtTokenEntity.getId());
    }

    @Override
    public void deleteJwtTokensByEmail(final String email) {
        deleteJwtTokenByEmail(email, JwtTokenType.ACCESS);
        deleteJwtTokenByEmail(email, JwtTokenType.REFRESH);
        log.info("Tokens were deleted.");
    }

    private void deleteJwtTokenByEmail(final String email, final JwtTokenType type) {
        jwtTokenRepository.markAsDeletedByUserEmailAndType(email, type);
    }

}
