package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityAlreadyExistsException;
import com.dataart.dancestudio.mapper.UserMapperImpl;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.Role;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

    @Spy
    private UserMapperImpl userMapperImpl;

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    private final String email = "email@email.ru";

    @Test
    public void loadUserByUsername() throws EntityAlreadyExistsException {
        // given
        final String password = "45";
        final String encodePassword = bCryptPasswordEncoder.encode(password);

        final int id = 1;
        final String firstName = "Alex";
        final String lastName = "Smirnov";
        final String username = "username";
        final String phoneNumber = "89085674534";
        final boolean deleted = false;
        final String timeZone = "Europe/Moscow";
        final UserEntity userEntity = UserEntity.builder()
                .id(id)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(encodePassword)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();
        final UserDetailsDto userDetailsDtoExpected = UserDetailsDto.builder()
                .email(email)
                .password(password)
                .build();

        doReturn(userDetailsDtoExpected).when(userMapperImpl).userEntityToUserDetailsDto(userEntity);
        when(userRepositoryMock.findByEmail(userDetailsDtoExpected.getEmail())).thenReturn(Optional.of(userEntity));

        // when
        final UserDetails userDetailsActual = userDetailsServiceImpl.loadUserByUsername(userDetailsDtoExpected.getEmail());

        // then
        verify(userRepositoryMock, times(1)).findByEmail(userDetailsDtoExpected.getEmail());
        assertEquals(userDetailsDtoExpected, userDetailsActual);
    }

    @Test
    public void loadUserByUsernameWhenUserNotFound() throws EntityAlreadyExistsException {
        // given
        final String password = "45";
        final UserDetailsDto userDetailsDtoExpected = UserDetailsDto.builder()
                .email(email)
                .password(password)
                .build();
        when(userRepositoryMock.findByEmail(userDetailsDtoExpected.getEmail())).thenReturn(Optional.empty());

        // when then
        final var actualException = assertThrowsExactly(UsernameNotFoundException.class,
                () -> userDetailsServiceImpl.loadUserByUsername(userDetailsDtoExpected.getEmail()));
        verify(userRepositoryMock, times(1)).findByEmail(userDetailsDtoExpected.getEmail());
        assertEquals(actualException.getMessage(), "No such user in the database!");
    }

}
