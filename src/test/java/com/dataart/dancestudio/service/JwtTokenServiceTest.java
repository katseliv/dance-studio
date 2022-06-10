package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityCreationException;
import com.dataart.dancestudio.exception.EntityNotFoundException;
import com.dataart.dancestudio.mapper.JwtTokenMapperImpl;
import com.dataart.dancestudio.model.dto.JwtTokenDto;
import com.dataart.dancestudio.model.entity.JwtTokenEntity;
import com.dataart.dancestudio.model.JwtTokenType;
import com.dataart.dancestudio.model.Role;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.JwtTokenRepository;
import com.dataart.dancestudio.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtTokenServiceTest {

    @Spy
    private JwtTokenMapperImpl jwtTokenMapperImpl;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private JwtTokenRepository jwtTokenRepositoryMock;

    @InjectMocks
    private JwtTokenServiceImpl jwtTokenServiceImpl;

    private final int id = 1;
    private final String username = "username";
    private final String firstName = "Alex";
    private final String lastName = "Smirnov";
    private final String email = "email@email.com";
    private final String phoneNumber = "89085674534";
    private final String timeZone = "Europe/Moscow";
    private final boolean deleted = false;

    private final UserEntity userEntity = UserEntity.builder()
            .id(id)
            .username(username)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .phoneNumber(phoneNumber)
            .role(Role.USER)
            .timeZone(timeZone)
            .deleted(deleted)
            .build();

    private final String accessToken = "access";
    private final String newAccessToken = "newAccess";

    private final JwtTokenEntity jwtTokenEntity = JwtTokenEntity.builder()
            .id(id)
            .token(accessToken)
            .type(JwtTokenType.ACCESS)
            .user(userEntity)
            .build();

    private final JwtTokenEntity newJwtTokenEntity = JwtTokenEntity.builder()
            .id(id)
            .token(newAccessToken)
            .type(JwtTokenType.ACCESS)
            .user(userEntity)
            .build();

    private final JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
            .token(accessToken)
            .type(JwtTokenType.ACCESS)
            .email(email)
            .build();

    private final JwtTokenDto newJwtTokenDto = JwtTokenDto.builder()
            .token(newAccessToken)
            .type(JwtTokenType.ACCESS)
            .email(email)
            .build();

    @Test
    public void createJwtToken() {
        // given
        when(userRepositoryMock.findByEmail(jwtTokenDto.getEmail())).thenReturn(Optional.ofNullable(userEntity));
        when(jwtTokenMapperImpl.jwtTokenDtoToJwtTokenEntity(jwtTokenDto)).thenReturn(jwtTokenEntity);
        when(jwtTokenRepositoryMock.save(jwtTokenEntity)).thenReturn(jwtTokenEntity);

        // when
        jwtTokenServiceImpl.createJwtToken(jwtTokenDto);

        // then
        verify(jwtTokenRepositoryMock, times(1)).save(jwtTokenEntity);
    }

    @Test
    public void createJwtTokenWhenUserDoesNotExist() {
        // given
        when(userRepositoryMock.findByEmail(jwtTokenDto.getEmail())).thenReturn(Optional.empty());

        // when then
        final var actualException = assertThrowsExactly(EntityCreationException.class,
                () -> jwtTokenServiceImpl.createJwtToken(jwtTokenDto));
        verify(jwtTokenRepositoryMock, never()).save(jwtTokenEntity);
        assertEquals(actualException.getMessage(), "Token not created!");
    }

    @Test
    public void createJwtTokenWhenJwtTokenIsNull() {
        // given
        when(userRepositoryMock.findByEmail(jwtTokenDto.getEmail())).thenReturn(Optional.ofNullable(userEntity));
        when(jwtTokenMapperImpl.jwtTokenDtoToJwtTokenEntity(jwtTokenDto)).thenReturn(jwtTokenEntity);
        when(jwtTokenRepositoryMock.save(jwtTokenEntity)).thenReturn(null);

        // when then
        final var actualException = assertThrowsExactly(EntityCreationException.class,
                () -> jwtTokenServiceImpl.createJwtToken(jwtTokenDto));
        verify(jwtTokenRepositoryMock, times(1)).save(jwtTokenEntity);
        assertEquals(actualException.getMessage(), "Token not created!");
    }

    @Test
    public void getJwtTokenByEmailAndType() {
        // given
        when(jwtTokenRepositoryMock.findByUserEmailAndType(email, JwtTokenType.ACCESS)).thenReturn(Optional.of(jwtTokenEntity));

        // when
        final String tokenActual = jwtTokenServiceImpl.getJwtTokenByEmailAndType(email, JwtTokenType.ACCESS);

        // then
        verify(jwtTokenRepositoryMock, times(1)).findByUserEmailAndType(email, JwtTokenType.ACCESS);
        assertEquals(accessToken, tokenActual);
    }

    @Test
    public void getJwtTokenByEmailAndTypeWhenJwtTokenDoesNotExist() {
        // given
        when(jwtTokenRepositoryMock.findByUserEmailAndType(email, JwtTokenType.ACCESS)).thenReturn(Optional.empty());

        // when then
        final var actualException = assertThrowsExactly(EntityNotFoundException.class,
                () -> jwtTokenServiceImpl.getJwtTokenByEmailAndType(email, JwtTokenType.ACCESS));
        verify(jwtTokenRepositoryMock, times(1)).findByUserEmailAndType(email, JwtTokenType.ACCESS);
        assertEquals(actualException.getMessage(), "Token not found!");
    }

    @Test
    public void existsByToken() {
        // given
        when(jwtTokenRepositoryMock.existsByToken(accessToken)).thenReturn(true);

        // when
        final boolean tokenExists = jwtTokenServiceImpl.existsByToken(accessToken);

        // then
        verify(jwtTokenRepositoryMock, times(1)).existsByToken(accessToken);
        assertTrue(tokenExists);
    }

    @Test
    public void notExistsByToken() {
        // given
        when(jwtTokenRepositoryMock.existsByToken(accessToken)).thenReturn(false);

        // when
        final boolean tokenExists = jwtTokenServiceImpl.existsByToken(accessToken);

        // then
        verify(jwtTokenRepositoryMock, times(1)).existsByToken(accessToken);
        assertFalse(tokenExists);
    }

    @Test
    public void existsByUserEmail() {
        // given
        when(jwtTokenRepositoryMock.existsByUserEmailAndType(email, JwtTokenType.ACCESS)).thenReturn(true);
        when(jwtTokenRepositoryMock.existsByUserEmailAndType(email, JwtTokenType.REFRESH)).thenReturn(true);

        // when
        final boolean tokenExists = jwtTokenServiceImpl.existsByUserEmail(email);

        // then
        verify(jwtTokenRepositoryMock, times(1)).existsByUserEmailAndType(email, JwtTokenType.ACCESS);
        verify(jwtTokenRepositoryMock, times(1)).existsByUserEmailAndType(email, JwtTokenType.REFRESH);
        assertTrue(tokenExists);
    }

    @Test
    public void existsByUserEmailWhenAccessDoesNotExist() {
        // given
        when(jwtTokenRepositoryMock.existsByUserEmailAndType(email, JwtTokenType.ACCESS)).thenReturn(false);

        // when
        final boolean tokenExists = jwtTokenServiceImpl.existsByUserEmail(email);

        // then
        verify(jwtTokenRepositoryMock, times(1)).existsByUserEmailAndType(email, JwtTokenType.ACCESS);
        verify(jwtTokenRepositoryMock, never()).existsByUserEmailAndType(email, JwtTokenType.REFRESH);
        assertFalse(tokenExists);
    }

    @Test
    public void existsByUserEmailWhenRefreshDoesNotExist() {
        // given
        when(jwtTokenRepositoryMock.existsByUserEmailAndType(email, JwtTokenType.ACCESS)).thenReturn(true);
        when(jwtTokenRepositoryMock.existsByUserEmailAndType(email, JwtTokenType.REFRESH)).thenReturn(false);

        // when
        final boolean tokenExists = jwtTokenServiceImpl.existsByUserEmail(email);

        // then
        verify(jwtTokenRepositoryMock, times(1)).existsByUserEmailAndType(email, JwtTokenType.ACCESS);
        verify(jwtTokenRepositoryMock, times(1)).existsByUserEmailAndType(email, JwtTokenType.REFRESH);
        assertFalse(tokenExists);
    }

    @Test
    public void updateJwtToken() {
        // given
        final ArgumentCaptor<JwtTokenEntity> jwtTokenEntityArgumentCaptor = ArgumentCaptor.forClass(JwtTokenEntity.class);
        when(jwtTokenRepositoryMock.findByUserEmailAndType(email, JwtTokenType.ACCESS)).thenReturn(Optional.ofNullable(jwtTokenEntity));
        when(jwtTokenRepositoryMock.save(any(JwtTokenEntity.class))).thenReturn(newJwtTokenEntity);

        // when
        jwtTokenServiceImpl.updateJwtToken(newJwtTokenDto);

        // then
        verify(jwtTokenRepositoryMock, times(1)).save(jwtTokenEntityArgumentCaptor.capture());
        final JwtTokenEntity newJwtTokenEntityActual = jwtTokenEntityArgumentCaptor.getValue();
        assertTrue(new ReflectionEquals(newJwtTokenEntity).matches(newJwtTokenEntityActual));
    }

    @Test
    public void updateJwtTokenWhenJwtTokenDoesNotExist() {
        // given
        when(jwtTokenRepositoryMock.findByUserEmailAndType(email, JwtTokenType.ACCESS)).thenReturn(Optional.empty());

        // when then
        final var actualException = assertThrowsExactly(EntityCreationException.class,
                () -> jwtTokenServiceImpl.updateJwtToken(newJwtTokenDto));
        verify(jwtTokenRepositoryMock, never()).save(newJwtTokenEntity);
        assertEquals(actualException.getMessage(), "New access token hasn't been created!");
    }

    @Test
    public void deleteJwtTokensByEmail() {
        // given
        when(jwtTokenRepositoryMock.existsByUserEmailAndType(email, JwtTokenType.ACCESS)).thenReturn(true);
        when(jwtTokenRepositoryMock.existsByUserEmailAndType(email, JwtTokenType.REFRESH)).thenReturn(true);

        // when
        jwtTokenServiceImpl.deleteJwtTokensByEmail(email);

        // then
        verify(jwtTokenRepositoryMock, times(1)).markAsDeletedByUserEmailAndType(email, JwtTokenType.ACCESS);
        verify(jwtTokenRepositoryMock, times(1)).markAsDeletedByUserEmailAndType(email, JwtTokenType.REFRESH);
    }

    @Test
    public void deleteJwtTokensByEmailWhenAccessDoesNotExist() {
        // given
        when(jwtTokenRepositoryMock.existsByUserEmailAndType(email, JwtTokenType.ACCESS)).thenReturn(false);

        // when then
        final var actualException = assertThrowsExactly(EntityNotFoundException.class,
                () -> jwtTokenServiceImpl.deleteJwtTokensByEmail(email));
        verify(jwtTokenRepositoryMock, never()).markAsDeletedByUserEmailAndType(email, JwtTokenType.ACCESS);
        verify(jwtTokenRepositoryMock, never()).markAsDeletedByUserEmailAndType(email, JwtTokenType.REFRESH);
        assertEquals(actualException.getMessage(), "Tokens not found!");
    }

    @Test
    public void deleteJwtTokensByEmailWhenRefreshDoesNotExist() {
        // given
        when(jwtTokenRepositoryMock.existsByUserEmailAndType(email, JwtTokenType.ACCESS)).thenReturn(true);
        when(jwtTokenRepositoryMock.existsByUserEmailAndType(email, JwtTokenType.REFRESH)).thenReturn(false);

        // when then
        final var actualException = assertThrowsExactly(EntityNotFoundException.class,
                () -> jwtTokenServiceImpl.deleteJwtTokensByEmail(jwtTokenDto.getEmail()));
        verify(jwtTokenRepositoryMock, never()).markAsDeletedByUserEmailAndType(email, JwtTokenType.ACCESS);
        verify(jwtTokenRepositoryMock, never()).markAsDeletedByUserEmailAndType(email, JwtTokenType.REFRESH);
        assertEquals(actualException.getMessage(), "Tokens not found!");
    }

}
