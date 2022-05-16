package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.UserMapper;
import com.dataart.dancestudio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return userMapper.userEntityToUserDetailsDto(userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("No such user in the database!!!")));
    }

}
