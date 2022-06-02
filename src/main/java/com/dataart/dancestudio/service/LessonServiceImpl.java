package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityCreationException;
import com.dataart.dancestudio.exception.EntityNotFoundException;
import com.dataart.dancestudio.mapper.LessonMapper;
import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.FilteredViewListPage;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.BookingRepository;
import com.dataart.dancestudio.repository.LessonRepository;
import com.dataart.dancestudio.repository.UserRepository;
import com.dataart.dancestudio.utils.ParseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LessonServiceImpl implements LessonService, PaginationService<LessonViewDto> {

    @Value("${pagination.defaultPageNumber}")
    private int defaultPageNumber;
    @Value("${pagination.defaultPageSize}")
    private int defaultPageSize;
    @Value("${pagination.buttonLimit}")
    private int buttonLimit;

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    @Override
    @Transactional
    public int createLesson(final LessonDto lessonDto) {
        final Optional<UserEntity> userEntity = userRepository.findById(lessonDto.getUserTrainerId());
        userEntity.ifPresentOrElse(
                (user) -> {
                    if (user.getRole() != Role.TRAINER) {
                        log.warn("Lesson hasn't been created.");
                        throw new EntityCreationException("User doesn't have role TRAINER!");
                    }
                    log.info("User has been found.");
                },
                () -> {
                    log.warn("User hasn't been found.");
                    throw new EntityNotFoundException("User not found!");
                }
        );
        final LessonEntity lessonEntity = Optional.of(lessonDto)
                .map(lessonMapper::lessonDtoToLessonEntity)
                .map(lessonRepository::save)
                .orElseThrow(() -> new EntityCreationException("Lesson hasn't been created!"));
        log.info("Lesson with id = {} has been created.", lessonEntity.getId());
        return lessonEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public LessonDto getLessonById(final int id) {
        final Optional<LessonEntity> lessonEntity = lessonRepository.findById(id);
        lessonEntity.ifPresentOrElse(
                (lesson) -> log.info("Lesson with id = {} has been found.", lesson.getId()),
                () -> log.warn("Lesson hasn't been found."));
        return lessonEntity.map(lessonMapper::lessonEntityToLessonDto)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found!"));
    }

    @Override
    @Transactional(readOnly = true)
    public LessonViewDto getLessonViewById(final int id) {
        final Optional<LessonEntity> lessonEntity = lessonRepository.findById(id);
        lessonEntity.ifPresentOrElse(
                (lesson) -> log.info("Lesson with id = {} has been found.", lesson.getId()),
                () -> log.warn("Lesson hasn't been found."));
        return lessonEntity.map(lessonMapper::lessonEntityToLessonViewDto)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found!"));
    }

    @Override
    @Transactional
    public void updateLessonById(final LessonDto lessonDto, final int id) {
        final Optional<LessonEntity> lessonEntity = lessonRepository.findById(id);
        lessonEntity.ifPresentOrElse(
                (lesson) -> log.info("Lesson with id = {} has been found.", lesson.getId()),
                () -> {
                    log.warn("Lesson hasn't been found.");
                    throw new EntityNotFoundException("Lesson not found!");
                });
        Optional.of(lessonDto)
                .map(lessonMapper::lessonDtoToLessonEntity)
                .map((lesson) -> {
                    lesson.setId(id);
                    return lessonRepository.save(lesson);
                });
        log.info("Lesson with id = {} has been updated.", id);
    }

    @Override
    @Transactional
    public void deleteLessonById(final int id) {
        final Optional<LessonEntity> lessonEntity = lessonRepository.findById(id);
        lessonEntity.ifPresentOrElse(
                (lesson) -> log.info("Lesson with id = {} has been found.", lesson.getId()),
                () -> {
                    log.warn("Lesson hasn't been found.");
                    throw new EntityNotFoundException("Lesson not found!");
                });
        lessonRepository.markAsDeletedById(id);
        bookingRepository.markAsDeletedByLessonId(id);
        log.info("Lesson with id = {} has been deleted.", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonViewDto> listLessons(final String trainerName, final String danceStyleName, final String date,
                                           final Pageable pageable) {
        final Specification<LessonEntity> specification = LessonRepository.hasTrainerNameAndDanceStyleNameAndDate(
                trainerName, danceStyleName, date);
        final List<LessonEntity> lessonEntities = lessonRepository.findAll(specification, pageable).getContent();
        log.info("There have been found {} lessons.", lessonEntities.size());
        return lessonMapper.lessonEntitiesToLessonViewDtoList(lessonEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public int numberOfFilteredLessons(final String trainerName, final String danceStyleName, final String date) {
        final Specification<LessonEntity> specification = LessonRepository.hasTrainerNameAndDanceStyleNameAndDate(
                trainerName, danceStyleName, date);
        final long numberOfFilteredLessons = lessonRepository.count(specification);
        log.info("There have been found {} lessons.", numberOfFilteredLessons);
        return (int) numberOfFilteredLessons;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonViewDto> listUserLessons(final int userId, final Pageable pageable) {
        final Optional<UserEntity> userEntity = userRepository.findById(userId);
        userEntity.ifPresentOrElse(
                (booking) -> log.info("User with id = {} has been found.", userId),
                () -> {
                    log.warn("User with id = {} hasn't been found.", userId);
                    throw new EntityNotFoundException("User not found!");
                });

        final List<LessonEntity> lessonEntities = lessonRepository.findAllByUserTrainerId(userId, pageable);
        log.info("There have been found {} lessons for userId {}.", lessonEntities.size(), userId);
        return lessonMapper.lessonEntitiesToLessonViewDtoList(lessonEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public int numberOfUserLessons(final int userId) {
        final int numberOfUserLessons = lessonRepository.countAllByUserTrainerId(userId);
        log.info("There have been found {} lessons for userId {}.", numberOfUserLessons, userId);
        return numberOfUserLessons;
    }

    @Override
    @Transactional(readOnly = true)
    public ViewListPage<LessonViewDto> getUserViewListPage(final int id, final String page, final String size) {
        final int pageNumber = Optional.ofNullable(page).map(ParseUtils::parsePositiveInteger).orElse(defaultPageNumber);
        final int pageSize = Optional.ofNullable(size).map(ParseUtils::parsePositiveInteger).orElse(defaultPageSize);

        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        final List<LessonViewDto> listUserBookings = listUserLessons(id, pageable);
        final int totalAmount = numberOfUserLessons(id);

        return getViewListPage(totalAmount, pageSize, pageNumber, listUserBookings);
    }

    @Override
    @Transactional(readOnly = true)
    public FilteredViewListPage<LessonViewDto> getFilteredLessonViewListPage(final String page, final String size, final String trainerName,
                                                                             final String danceStyleName, final String date) {
        final int pageNumber = Optional.ofNullable(page).map(ParseUtils::parsePositiveInteger).orElse(defaultPageNumber);
        final int pageSize = Optional.ofNullable(size).map(ParseUtils::parsePositiveInteger).orElse(defaultPageSize);

        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        final List<LessonViewDto> lessonViewDtoList = listLessons(trainerName, danceStyleName, date, pageable);
        final int totalAmount = numberOfFilteredLessons(trainerName, danceStyleName, date);

        final int totalPages = (int) Math.ceil((double) totalAmount / pageSize);
        final int startPageNumber = getStartPageNumber(totalPages, pageNumber);
        final int endPageNumber = Math.max(Math.min(pageNumber + buttonLimit / 2, totalPages), buttonLimit);
        final int additive = (pageNumber - 1) * pageSize + 1;

        final Map<String, String> filterParameters = new HashMap<>();
        filterParameters.put("trainerName", trainerName);
        filterParameters.put("danceStyleName", danceStyleName);
        filterParameters.put("date", date);
        return FilteredViewListPage.<LessonViewDto>builder()
                .filerParameters(filterParameters)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .additive(additive)
                .startPageNumber(startPageNumber)
                .currentPageNumber(pageNumber)
                .endPageNumber(endPageNumber)
                .viewDtoList(lessonViewDtoList)
                .build();
    }

    @Override
    public int getButtonLimit() {
        return buttonLimit;
    }

}
