package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.RoomMapperImpl;
import com.dataart.dancestudio.model.dto.view.RoomViewDto;
import com.dataart.dancestudio.model.entity.RoomEntity;
import com.dataart.dancestudio.model.entity.StudioEntity;
import com.dataart.dancestudio.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Spy
    private RoomMapperImpl roomMapperImpl;

    @Mock
    private RoomRepository roomRepositoryMock;

    @InjectMocks
    private RoomServiceImpl roomServiceImpl;

    private final int id = 1;
    private final int studioId = 1;
    private final String name = "Big";
    private final String description = "Description";

    private final StudioEntity studioEntity = StudioEntity.builder()
            .id(studioId)
            .name(name)
            .description(description)
            .build();
    private final RoomEntity roomEntity = RoomEntity.builder()
            .id(id)
            .name(name)
            .studio(studioEntity)
            .description(description)
            .build();
    private final RoomViewDto roomViewDto = RoomViewDto.builder()
            .id(id)
            .name(name)
            .studioId(studioId)
            .description(description)
            .build();

    @Test
    public void listRooms() {
        // given
        final int pageNumber = 1;
        final int pageSize = 5;

        final List<RoomViewDto> roomViewDtoListExpected = List.of(roomViewDto);
        final Page<RoomEntity> roomEntities = new PageImpl<>(List.of(roomEntity));
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        when(roomRepositoryMock.findAll(eq(pageable))).thenReturn(roomEntities);

        // when
        final List<RoomViewDto> roomViewDtoListActual = roomServiceImpl.listRooms(pageable);

        // then
        verify(roomRepositoryMock, times(1)).findAll(eq(pageable));
        assertEquals(roomViewDtoListExpected, roomViewDtoListActual);
    }

    @Test
    public void emptyListRooms() {
        // given
        final int pageNumber = 1;
        final int pageSize = 5;

        final List<RoomViewDto> roomViewDtoListExpected = List.of();
        final Page<RoomEntity> roomEntities = new PageImpl<>(List.of());
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        when(roomRepositoryMock.findAll(eq(pageable))).thenReturn(roomEntities);

        // when
        final List<RoomViewDto> roomViewDtoListActual = roomServiceImpl.listRooms(pageable);

        // then
        verify(roomRepositoryMock, times(1)).findAll(eq(pageable));
        assertEquals(roomViewDtoListExpected, roomViewDtoListActual);
    }

    @Test
    public void numberOfEntities() {
        // given
        final int amountExpected = 5;

        when(roomRepositoryMock.count()).thenReturn((long) amountExpected);

        // when
        final int amountActual = roomServiceImpl.numberOfRooms();

        // then
        verify(roomRepositoryMock, times(1)).count();
        assertEquals(amountExpected, amountActual);
    }

    @Test
    public void zeroNumberOfEntities() {
        // given
        final int amountExpected = 0;

        when(roomRepositoryMock.count()).thenReturn((long) amountExpected);

        // when
        final int amountActual = roomServiceImpl.numberOfRooms();

        // then
        verify(roomRepositoryMock, times(1)).count();
        assertEquals(amountExpected, amountActual);
    }

}
