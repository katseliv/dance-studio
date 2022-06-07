package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityAlreadyExistsException;
import com.dataart.dancestudio.exception.EntityCreationException;
import com.dataart.dancestudio.exception.EntityNotFoundException;
import com.dataart.dancestudio.exception.UserCanNotBeDeletedException;
import com.dataart.dancestudio.mapper.UserMapperImpl;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.dto.view.UserForListDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.entity.Provider;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Spy
    private UserMapperImpl userMapperImpl;

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private LessonServiceImpl lessonServiceMock;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private final int id = 1;
    private final String username = "username";
    private final String firstName = "Alex";
    private final String lastName = "Smirnov";
    private final String newLastName = "Popov";
    private final String email = "email@email.ru";
    private final String phoneNumber = "89085674534";
    private final String timeZone = "Europe/Moscow";
    private final boolean deleted = false;

    @Test
    public void createUser() throws IOException, EntityAlreadyExistsException {
        // given
        final String password = "45";
        final String encodePassword = bCryptPasswordEncoder.encode(password);

        when(bCryptPasswordEncoder.encode(password)).thenReturn(encodePassword);
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 5, 7});

        final byte[] multipartFileBytes = multipartFile.getBytes();
        final UserEntity userEntity = UserEntity.builder()
                .id(id)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .image(multipartFileBytes)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(encodePassword)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();
        final UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .build();

        doReturn(userEntity).when(userMapperImpl).userRegistrationDtoToUserEntityWithPassword(userRegistrationDto, userEntity.getPassword());
        when(userRepositoryMock.findByEmail(userRegistrationDto.getEmail())).thenReturn(Optional.empty());
        when(userRepositoryMock.save(userEntity)).thenReturn(userEntity);

        // when
        final int userId = userServiceImpl.createUser(userRegistrationDto, Provider.LOCAL);

        // then
        verify(userRepositoryMock, times(1)).save(userEntity);
        assertEquals(id, userId);
    }

    @Test
    public void createUserWhenSuchUserAlreadyExists() throws IOException {
        // given
        final String password = "45";
        final String encodePassword = bCryptPasswordEncoder.encode(password);

        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 5, 7});

        final UserEntity userEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .password(encodePassword)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();
        final UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .build();

        when(userRepositoryMock.findByEmail(userRegistrationDto.getEmail())).thenReturn(Optional.of(userEntity));

        // when
        assertThrows(EntityAlreadyExistsException.class, () -> userServiceImpl.createUser(userRegistrationDto, Provider.LOCAL));

        // then
        verify(userMapperImpl, never()).userRegistrationDtoToUserEntityWithPassword(
                userRegistrationDto, userEntity.getPassword());
        verify(userRepositoryMock, never()).save(userEntity);
    }

    @Test
    public void createUserWhenUserIsNull() throws IOException {
        // given
        final String password = "45";
        final String encodePassword = bCryptPasswordEncoder.encode(password);

        when(bCryptPasswordEncoder.encode(password)).thenReturn(encodePassword);
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 5, 7});

        final UserEntity userEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .password(encodePassword)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();
        final UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .build();

        doReturn(userEntity).when(userMapperImpl).userRegistrationDtoToUserEntityWithPassword(userRegistrationDto, userEntity.getPassword());
        when(userRepositoryMock.findByEmail(userRegistrationDto.getEmail())).thenReturn(Optional.empty());
        when(userRepositoryMock.save(userEntity)).thenReturn(null);

        // when then
        final var actualException = assertThrowsExactly(EntityCreationException.class,
                () -> userServiceImpl.createUser(userRegistrationDto, Provider.LOCAL));
        verify(userMapperImpl, times(1)).userRegistrationDtoToUserEntityWithPassword(
                userRegistrationDto, userEntity.getPassword());
        verify(userRepositoryMock, times(1)).save(userEntity);
        assertEquals(actualException.getMessage(), "User not created!");
    }

    @Test
    public void getUserById() throws IOException {
        // given
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 5, 7});

        final UserEntity userEntity = UserEntity.builder()
                .id(id)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();
        final UserDto userDtoWithoutMultipartFile = UserDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .build();

        when(userMapperImpl.userEntityToUserDto(userEntity)).thenReturn(userDtoWithoutMultipartFile);
        when(userRepositoryMock.findById(id)).thenReturn(Optional.of(userEntity));

        // when
        final UserDto userDtoActual = userServiceImpl.getUserById(id);

        // then
        verify(userRepositoryMock, times(1)).findById(id);
        assertEquals(userDtoWithoutMultipartFile, userDtoActual);
    }

    @Test
    public void getUserByIdWhenOptionalNull() throws IOException {
        // given
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 5, 7});

        final UserEntity userEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();

        when(userRepositoryMock.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () -> userServiceImpl.getUserById(id));

        // then
        verify(userMapperImpl, never()).userEntityToUserDto(userEntity);
        verify(userRepositoryMock, times(1)).findById(id);
    }

    @Test
    public void getUserViewById() throws IOException {
        // given
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 5, 7});

        final UserEntity userEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();
        final UserViewDto userViewDto = UserViewDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .image(Base64.getEncoder().encodeToString(multipartFile.getBytes()))
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        when(userRepositoryMock.findById(id)).thenReturn(Optional.of(userEntity));

        // when
        final UserViewDto userViewDtoActual = userServiceImpl.getUserViewById(id);

        // then
        verify(userMapperImpl, times(1)).userEntityToUserViewDto(userEntity);
        verify(userRepositoryMock, times(1)).findById(id);
        assertEquals(userViewDto, userViewDtoActual);
    }

    @Test
    public void getUserViewByIdWhenOptionalNull() throws IOException {
        // given
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 5, 7});

        final UserEntity userEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();

        when(userRepositoryMock.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () -> userServiceImpl.getUserViewById(id));

        // then
        verify(userMapperImpl, never()).userEntityToUserViewDto(userEntity);
        verify(userRepositoryMock, times(1)).findById(id);
    }

    @Test
    public void getUserIdByEmail() {
        // given
        final String password = "45";
        final String encodePassword = bCryptPasswordEncoder.encode(password);

        final UserEntity userEntity = UserEntity.builder()
                .id(id)
                .email(email)
                .role(Role.USER)
                .password(encodePassword)
                .build();
        final UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .id(id)
                .email(email)
                .roles(List.of(Role.USER))
                .password(encodePassword)
                .build();

        when(userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // when
        final int userId = userServiceImpl.getUserIdByEmail(email);

        // then
        verify(userMapperImpl, times(1)).userEntityToUserDetailsDto(userEntity);
        verify(userRepositoryMock, times(1)).findByEmail(email);
        assertEquals(userDetailsDto.getId(), userId);
    }

    @Test
    public void getUserIdByEmailWhenOptionalNull() {
        // given
        final String password = "45";
        final String encodePassword = bCryptPasswordEncoder.encode(password);

        final UserEntity userEntity = UserEntity.builder()
                .id(id)
                .email(email)
                .role(Role.USER)
                .password(encodePassword)
                .build();

        when(userRepositoryMock.findByEmail(email)).thenReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () -> userServiceImpl.getUserIdByEmail(email));

        // then
        verify(userMapperImpl, never()).userEntityToUserDetailsDto(userEntity);
        verify(userRepositoryMock, times(1)).findByEmail(email);
    }


    @Test
    public void updateUserByIdWithChangedFieldAndImage() {
        // given
        final ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        final UserEntity userEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .image(new byte[]{1, 2, 5, 7})
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();
        final UserEntity newUserEntityWithUpdatedImage = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(newLastName)
                .image(new byte[]{1, 20, 30, 40})
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();
        final UserDto newUserDto = UserDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(newLastName)
                .base64StringImage(Base64.getEncoder().encodeToString(new byte[]{1, 20, 30, 40}))
                .email(email)
                .phoneNumber(phoneNumber)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .build();

        when(userRepositoryMock.findById(id)).thenReturn(Optional.ofNullable(userEntity));
        when(userRepositoryMock.save(any(UserEntity.class))).thenReturn(newUserEntityWithUpdatedImage);

        // when
        userServiceImpl.updateUserById(newUserDto, id);

        // then
        verify(userRepositoryMock, times(1)).save(userEntityArgumentCaptor.capture());
        final UserEntity newUserEntityActual = userEntityArgumentCaptor.getValue();
        assertTrue(new ReflectionEquals(newUserEntityWithUpdatedImage).matches(newUserEntityActual));
    }

    @Test
    public void updateUserByIdWithChangedFieldAndEmptyImage() throws IOException {
        // given
        final ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 5, 7});

        final UserEntity newUserEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(newLastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();
        final UserEntity userEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();
        final UserDto newUserDto = UserDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(newLastName)
                .base64StringImage(Base64.getEncoder().encodeToString(new byte[0]))
                .email(email)
                .phoneNumber(phoneNumber)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .build();

        when(userRepositoryMock.findById(id)).thenReturn(Optional.of(userEntity));

        // when
        userServiceImpl.updateUserById(newUserDto, id);

        // then
        verify(userRepositoryMock, times(1)).save(userEntityArgumentCaptor.capture());
        final UserEntity newUserEntityActual = userEntityArgumentCaptor.getValue();
        assertTrue(new ReflectionEquals(newUserEntity).matches(newUserEntityActual));
    }

    @Test
    public void updateUserByIdWithChangedFieldAndEmptyImageWhenOptionalNull() throws IOException {
        // given
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 5, 7});

        final UserEntity newUserEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(newLastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();
        final UserDto newUserDto = UserDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(newLastName)
                .base64StringImage(Base64.getEncoder().encodeToString(multipartFile.getBytes()))
                .email(email)
                .phoneNumber(phoneNumber)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .build();

        when(userRepositoryMock.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () -> userServiceImpl.updateUserById(newUserDto, id));

        // then
        verify(userRepositoryMock, never()).save(newUserEntity);
    }

    @Test
    public void doesNotUpdateUserByIdWithEmptyImage() throws IOException {
        // given
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 5, 7});

        final UserEntity userEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();
        final UserDto userDto = UserDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .base64StringImage(Base64.getEncoder().encodeToString(multipartFile.getBytes()))
                .email(email)
                .phoneNumber(phoneNumber)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .build();
        final UserEntity newUserEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(newLastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();

        when(userRepositoryMock.findById(id)).thenReturn(Optional.of(userEntity));

        // when
        userServiceImpl.updateUserById(userDto, id);

        // then
        verify(userRepositoryMock, never()).save(newUserEntity);
    }

    @Test
    public void doesNotUpdateUserByIdWithEmptyImageWhenOptionalNull() throws IOException {
        // given
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 5, 7});

        final UserDto userDto = UserDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .base64StringImage(Base64.getEncoder().encodeToString(multipartFile.getBytes()))
                .email(email)
                .phoneNumber(phoneNumber)
                .roleId(Role.USER.getId())
                .timeZone(timeZone)
                .build();
        final UserEntity newUserEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(newLastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();

        when(userRepositoryMock.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () -> userServiceImpl.updateUserById(userDto, id));

        // then
        verify(userRepositoryMock, never()).save(newUserEntity);
    }

    @Test
    public void deleteUserById() throws UserCanNotBeDeletedException {
        // given
        doNothing().when(userRepositoryMock).markAsDeletedById(id);
        when(userRepositoryMock.findById(id)).thenReturn(Optional.ofNullable(UserEntity.builder().build()));
        when(lessonServiceMock.numberOfUserLessons(id)).thenReturn(0);

        // when
        userServiceImpl.deleteUserById(id);

        // then
        verify(userRepositoryMock, times(1)).markAsDeletedById(id);
    }

    @Test
    public void deleteUserByIdWhenUserDoesNotExist() {
        // when
        assertThrows(EntityNotFoundException.class, () -> userServiceImpl.deleteUserById(id));

        // then
        verify(userRepositoryMock, never()).markAsDeletedById(id);
    }

    @Test
    public void deleteUserByIdWhenUserHasLessons() {
        // when
        when(userRepositoryMock.findById(id)).thenReturn(Optional.ofNullable(UserEntity.builder().build()));
        when(lessonServiceMock.numberOfUserLessons(id)).thenReturn(1);
        assertThrows(UserCanNotBeDeletedException.class, () -> userServiceImpl.deleteUserById(id));

        // then
        verify(userRepositoryMock, never()).markAsDeletedById(id);
    }

    @Test
    public void listTrainers() {
        // given
        final UserEntity userEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.TRAINER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();
        final UserForListDto userForListDto = UserForListDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        final int pageNumber = 1;
        final int pageSize = 5;

        final List<UserForListDto> userViewDtoListExpected = List.of(userForListDto);
        final List<UserEntity> userEntities = List.of(userEntity);
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        when(userRepositoryMock.findAllByRole(Role.TRAINER, pageable)).thenReturn(userEntities);

        // when
        final List<UserForListDto> userViewDtoListActual = userServiceImpl.listTrainers(pageable);

        // then
        verify(userMapperImpl, times(1)).userEntitiesToUserViewDtoList(userEntities);
        verify(userRepositoryMock, times(1)).findAllByRole(Role.TRAINER, pageable);
        assertEquals(userViewDtoListExpected, userViewDtoListActual);
    }

    @Test
    public void emptyListTrainers() {
        // given
        final int pageNumber = 1;
        final int pageSize = 5;

        final List<UserForListDto> userViewDtoListExpected = List.of();
        final List<UserEntity> userEntities = List.of();
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        when(userRepositoryMock.findAllByRole(Role.TRAINER, pageable)).thenReturn(userEntities);

        // when
        final List<UserForListDto> userViewDtoListActual = userServiceImpl.listTrainers(pageable);

        // then
        verify(userMapperImpl, times(1)).userEntitiesToUserViewDtoList(userEntities);
        verify(userRepositoryMock, times(1)).findAllByRole(Role.TRAINER, pageable);
        assertEquals(userViewDtoListExpected, userViewDtoListActual);
    }

    @Test
    public void listUsers() throws IOException {
        // given
        final UserEntity userEntity = UserEntity.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .image(multipartFile.getBytes())
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .timeZone(timeZone)
                .deleted(deleted)
                .build();
        final UserForListDto userForListDto = UserForListDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        final int pageNumber = 1;
        final int pageSize = 5;

        final List<UserForListDto> userViewDtoListExpected = List.of(userForListDto);
        final Page<UserEntity> userEntities = new PageImpl<>(List.of(userEntity));
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        when(userRepositoryMock.findAll(pageable)).thenReturn(userEntities);

        // when
        final List<UserForListDto> userViewDtoListActual = userServiceImpl.listUsers(pageable);

        // then
        verify(userRepositoryMock, times(1)).findAll(eq(pageable));
        assertEquals(userViewDtoListExpected, userViewDtoListActual);
    }

    @Test
    public void emptyListUsers() {
        // given
        final int pageNumber = 1;
        final int pageSize = 5;

        final List<UserForListDto> userViewDtoListExpected = List.of();
        final Page<UserEntity> userEntities = new PageImpl<>(List.of());
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        when(userRepositoryMock.findAll(pageable)).thenReturn(userEntities);

        // when
        final List<UserForListDto> userViewDtoListActual = userServiceImpl.listUsers(pageable);

        // then
        verify(userRepositoryMock, times(1)).findAll(eq(pageable));
        assertEquals(userViewDtoListExpected, userViewDtoListActual);
    }

    @Test
    public void numberOfEntities() {
        // given
        final int amountExpected = 5;

        when(userRepositoryMock.count()).thenReturn((long) amountExpected);

        // when
        final int amountActual = userServiceImpl.numberOfUsers();

        // then
        verify(userRepositoryMock, times(1)).count();
        assertEquals(amountExpected, amountActual);
    }

    @Test
    public void zeroNumberOfEntities() {
        // given
        final int amountExpected = 0;

        when(userRepositoryMock.count()).thenReturn((long) amountExpected);

        // when
        final int amountActual = userServiceImpl.numberOfUsers();

        // then
        verify(userRepositoryMock, times(1)).count();
        assertEquals(amountExpected, amountActual);
    }

}
