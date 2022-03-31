package com.dataart.dancestudio.test;

import com.dataart.dancestudio.mapper.UserMapperImpl;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.impl.UserRepository;
import com.dataart.dancestudio.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Spy
    private UserMapperImpl userMapperImpl;

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private UserDto userDto;
    private UserEntity userEntity;
    private UserViewDto userViewDto;
    private UserDto newUserDto;
    private UserEntity newUserEntity;

    private final int id = 1;
    private final String username = "username";
    private final String firstName = "Alex";
    private final String lastName = "Smirnov";
    private final String email = "email@email.ru";
    private final String phoneNumber = "89085674534";
    private final boolean isDeleted = false;
    private final String timeZone = "Europe/Moscow";
    private String encodePassword;

    @Before
    public void init() throws IOException {
        final String password = "45";
        encodePassword = bCryptPasswordEncoder.encode(password);

        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 5, 7});
        when(bCryptPasswordEncoder.encode(password)).thenReturn(encodePassword);

        userDto = UserDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .multipartFile(multipartFile)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .isDeleted(isDeleted)
                .build();
        userEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .password(encodePassword)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .isDeleted(isDeleted)
                .build();

        userViewDto = UserViewDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .image(Base64.getEncoder().encodeToString(multipartFile.getBytes()))
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        final String newLastName = "Popov";

        newUserDto = UserDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(newLastName)
                .multipartFile(multipartFile)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(encodePassword)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .isDeleted(isDeleted)
                .build();
        newUserEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(newLastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .password(encodePassword)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .isDeleted(isDeleted)
                .build();
    }

    @Test
    public void createUser() throws IOException {
        //given
        when(userRepositoryMock.save(userEntity)).thenReturn(id);

        // when
        final int lessonId = userServiceImpl.createUser(userDto);

        // then
        verify(userMapperImpl, times(1))
                .userDtoToUserEntityWithPassword(userDto, userEntity.getPassword());
        assertEquals(id, lessonId);
    }

    @Test
    public void getUserById() {
        //given
        userDto = UserDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(encodePassword)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .isDeleted(isDeleted)
                .build();

        when(userRepositoryMock.findById(id)).thenReturn(Optional.of(userEntity));

        // when
        final UserDto userDtoActual = userServiceImpl.getUserById(id);

        // then
        verify(userMapperImpl, times(1)).userEntityToUserDto(userEntity);
        verify(userRepositoryMock, times(1)).findById(id);
        assertEquals(userDto, userDtoActual);
    }

    @Test
    public void getUserViewById(){
        //given
        when(userRepositoryMock.findById(id)).thenReturn(Optional.of(userEntity));

        // when
        final UserViewDto lessonViewDtoActual = userServiceImpl.getUserViewById(id);

        // then
        verify(userMapperImpl, times(1)).userEntityToUserViewDto(userEntity);
        verify(userRepositoryMock, times(1)).findById(id);
        assertEquals(userViewDto, lessonViewDtoActual);
    }

    @Test
    public void updateUserByIdWithChangedFieldAndImage() throws IOException {
        //given
        final String newLastName = "Popov";
        newUserDto = UserDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(newLastName)
                .multipartFile(multipartFile)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(encodePassword)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .isDeleted(isDeleted)
                .build();
        newUserEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(newLastName)
                .image(new byte[]{1, 20, 30, 40})
                .email(email)
                .phoneNumber(phoneNumber)
                .password(encodePassword)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .isDeleted(isDeleted)
                .build();

        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 20, 30, 40});

        // when
        userServiceImpl.updateUserById(newUserDto, id);

        // then
        verify(userRepositoryMock, times(1)).update(newUserEntity, id);
        verify(userMapperImpl, times(1)).userDtoToUserEntity(newUserDto);
    }

    @Test
    public void updateUserByIdWithChangedFieldAndEmptyImage() throws IOException {
        //given
        final String newLastName = "Popov";
        final UserEntity userEntityFromDB = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .isDeleted(isDeleted)
                .build();
        newUserDto = UserDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(newLastName)
                .multipartFile(multipartFile)
                .email(email)
                .phoneNumber(phoneNumber)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .isDeleted(isDeleted)
                .build();
        newUserEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(newLastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .isDeleted(isDeleted)
                .build();

        when(multipartFile.isEmpty()).thenReturn(true);

        doNothing().when(userRepositoryMock).updateWithoutPicture(newUserEntity, id);
        when(userRepositoryMock.findById(id)).thenReturn(Optional.of(userEntityFromDB));

        // when
        userServiceImpl.updateUserById(newUserDto, id);

        // then
        verify(userRepositoryMock, times(1)).updateWithoutPicture(newUserEntity, id);
        verify(userMapperImpl, times(1)).userDtoToUserEntity(newUserDto);
    }

    @Test
    public void doesNotUpdateUserByIdWithEmptyImage() throws IOException {
        //given
        final UserEntity userEntityFromDB = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .isDeleted(isDeleted)
                .build();
        userDto = UserDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .multipartFile(multipartFile)
                .email(email)
                .phoneNumber(phoneNumber)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .isDeleted(isDeleted)
                .build();

        when(multipartFile.isEmpty()).thenReturn(true);

        when(userRepositoryMock.findById(id)).thenReturn(Optional.of(userEntityFromDB));

        // when
        userServiceImpl.updateUserById(userDto, id);

        // then
        verify(userRepositoryMock, never()).update(newUserEntity, id);
        verify(userRepositoryMock, never()).updateWithoutPicture(newUserEntity, id);
        verify(userMapperImpl, never()).userDtoToUserEntity(userDto);
    }

    @Test
    public void deleteUserById() {
        //given
        doNothing().when(userRepositoryMock).deleteById(id);

        // when
        userServiceImpl.deleteUserById(id);

        // then
        verify(userRepositoryMock, times(1)).deleteById(id);
    }

    @Test
    public void listUsers() {
        //given
        final List<UserViewDto> userViewDtoListExpected = new ArrayList<>();
        userViewDtoListExpected.add(userViewDto);

        final List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(userEntity);

        when(userRepositoryMock.findAll()).thenReturn(userEntities);

        // when
        final List<UserViewDto> userViewDtoListActual = userServiceImpl.listUsers();

        // then
        verify(userMapperImpl, times(1))
                .userEntitiesToUserViewDtoList(userEntities);
        verify(userRepositoryMock, times(1)).findAll();
        assertEquals(userViewDtoListExpected, userViewDtoListActual);
    }

}
