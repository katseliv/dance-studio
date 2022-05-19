package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.DanceStyleMapperImpl;
import com.dataart.dancestudio.model.dto.view.DanceStyleViewDto;
import com.dataart.dancestudio.model.entity.DanceStyleEntity;
import com.dataart.dancestudio.repository.DanceStyleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DanceStyleServiceTest {

    @Spy
    private DanceStyleMapperImpl danceStyleMapperImpl;

    @Mock
    private DanceStyleRepository danceStyleRepositoryMock;

    @InjectMocks
    private DanceStyleServiceImpl danceStyleServiceImpl;

    private final int id = 1;
    private final String name = "Big";
    private final String description = "Description";

    private final DanceStyleEntity danceStyleEntity = DanceStyleEntity.builder()
            .id(id)
            .name(name)
            .description(description)
            .build();
    private final DanceStyleViewDto danceStyleViewDto = DanceStyleViewDto.builder()
            .id(id)
            .name(name)
            .description(description)
            .build();

    @Test
    public void listDanceStyles() {
        // given
        final List<DanceStyleViewDto> danceStyleViewDtoListExpected = List.of(danceStyleViewDto);
        final List<DanceStyleEntity> danceStyleEntities = List.of(danceStyleEntity);

        when(danceStyleMapperImpl.danceStyleEntitiesToDanceStyleViewDtoList(danceStyleEntities)).thenReturn(danceStyleViewDtoListExpected);
        when(danceStyleRepositoryMock.findAll()).thenReturn(danceStyleEntities);

        // when
        final List<DanceStyleViewDto> bookingViewDtoListActual = danceStyleServiceImpl.listDanceStyleViews();

        // then
        verify(danceStyleRepositoryMock, times(1)).findAll();
        assertEquals(danceStyleViewDtoListExpected, bookingViewDtoListActual);
    }

}
