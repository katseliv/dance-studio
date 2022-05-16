package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.JwtTokenMapper;
import com.dataart.dancestudio.model.dto.JwtTokenDto;
import com.dataart.dancestudio.model.entity.JwtTokenEntity;
import com.dataart.dancestudio.model.entity.JwtTokenType;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.JwtTokenRepository;
import com.dataart.dancestudio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        final UserEntity userEntity = userRepository.findByEmail(jwtTokenDto.getEmail()).orElseThrow();
        final JwtTokenEntity jwtTokenEntity = jwtTokenMapper.jwtTokenDtoToJwtTokenEntity(jwtTokenDto);
        jwtTokenEntity.setUser(userEntity);
        jwtTokenRepository.save(jwtTokenEntity);
    }

    @Override
    public String getJwtTokenByEmail(final String email, final JwtTokenType type) {
        return jwtTokenRepository.findByUserEmailAndType(email, type).orElseThrow().getToken();
    }

    @Override
    public boolean existsByToken(final String token) {
        return jwtTokenRepository.existsByToken(token);
    }

    @Override
    public boolean existsByUserEmail(final String email) {
        return existsByUserEmailAndType(email, JwtTokenType.ACCESS) && existsByUserEmailAndType(email, JwtTokenType.REFRESH);
    }

    private boolean existsByUserEmailAndType(final String email, final JwtTokenType type) {
        return jwtTokenRepository.existsByUserEmailAndType(email, type);
    }

    @Override
    public void updateJwtToken(final JwtTokenDto jwtTokenDto) {
        final JwtTokenEntity jwtTokenEntity = jwtTokenRepository.findByUserEmailAndType(
                jwtTokenDto.getEmail(), jwtTokenDto.getType()).orElseThrow();
        jwtTokenMapper.mergeJwtTokenEntityAndJwtTokenDto(jwtTokenEntity, jwtTokenDto);
        jwtTokenRepository.save(jwtTokenEntity);
    }

    @Override
    public void deleteJwtTokensByEmail(final String email) {
        deleteJwtTokenByEmail(email, JwtTokenType.ACCESS);
        deleteJwtTokenByEmail(email, JwtTokenType.REFRESH);
    }

    private void deleteJwtTokenByEmail(final String email, final JwtTokenType type) {
        jwtTokenRepository.markAsDeletedByUserEmailAndType(email, type);
    }

}
