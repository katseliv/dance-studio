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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        final int pageNumber = 1;
        final int pageSize = 5;

        final List<DanceStyleViewDto> danceStyleViewDtoListExpected = List.of(danceStyleViewDto);
        final Page<DanceStyleEntity> danceStyleEntities = new PageImpl<>(List.of(danceStyleEntity));
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        when(danceStyleRepositoryMock.findAll(eq(pageable))).thenReturn(danceStyleEntities);

        // when
        final List<DanceStyleViewDto> danceStyleViewDtoListActual = danceStyleServiceImpl.listDanceStyles(pageable);

        // then
        verify(danceStyleRepositoryMock, times(1)).findAll(eq(pageable));
        assertEquals(danceStyleViewDtoListExpected, danceStyleViewDtoListActual);
    }

    @Test
    public void emptyListDanceStyles() {
        // given
        final int pageNumber = 1;
        final int pageSize = 5;

        final List<DanceStyleViewDto> danceStyleViewDtoListExpected = List.of();
        final Page<DanceStyleEntity> danceStyleEntities = new PageImpl<>(List.of());
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        when(danceStyleRepositoryMock.findAll(eq(pageable))).thenReturn(danceStyleEntities);

        // when
        final List<DanceStyleViewDto> danceStyleViewDtoListActual = danceStyleServiceImpl.listDanceStyles(pageable);

        // then
        verify(danceStyleRepositoryMock, times(1)).findAll(eq(pageable));
        assertEquals(danceStyleViewDtoListExpected, danceStyleViewDtoListActual);
    }

    @Test
    public void numberOfEntities() {
        // given
        final int amountExpected = 5;

        when(danceStyleRepositoryMock.count()).thenReturn((long) amountExpected);

        // when
        final int amountActual = danceStyleServiceImpl.numberOfDanceStyles();

        // then
        verify(danceStyleRepositoryMock, times(1)).count();
        assertEquals(amountExpected, amountActual);
    }

    @Test
    public void zeroNumberOfEntities() {
        // given
        final int amountExpected = 0;

        when(danceStyleRepositoryMock.count()).thenReturn((long) amountExpected);

        // when
        final int amountActual = danceStyleServiceImpl.numberOfDanceStyles();

        // then
        verify(danceStyleRepositoryMock, times(1)).count();
        assertEquals(amountExpected, amountActual);
    }

}
