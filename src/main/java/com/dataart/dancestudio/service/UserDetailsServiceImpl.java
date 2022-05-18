package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.UserMapper;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserDetailsServiceImpl(final UserRepository userRepository, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        userEntity.ifPresentOrElse(
                (user) -> log.info("User with id = {} was found.", user.getId()),
                () -> log.info("User wasn't found."));
        return userMapper.userEntityToUserDetailsDto(userEntity.orElseThrow(
                () -> new UsernameNotFoundException("No such user in the database!")));
    }

}
