package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.UserMapper;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        userEntity.ifPresentOrElse(
                (user) -> log.info("User for email = {} with id = {} has been found.", email, user.getId()),
                () -> log.warn("User for email = {} hasn't been found.", email));
        return userMapper.userEntityToUserDetailsDto(userEntity.orElseThrow(
                () -> new UsernameNotFoundException("No such user in the database!")));
    }

}
