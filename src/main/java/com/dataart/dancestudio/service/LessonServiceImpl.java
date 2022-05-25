package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityCreationException;
import com.dataart.dancestudio.exception.EntityNotFoundException;
import com.dataart.dancestudio.mapper.LessonMapper;
import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.BookingRepository;
import com.dataart.dancestudio.repository.LessonRepository;
import com.dataart.dancestudio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LessonServiceImpl implements LessonService, UserEntityService<LessonViewDto> {

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
        if (lessonEntities.size() != 0) {
            log.info("Lessons have been found.");
        } else {
            log.warn("There haven't been lessons.");
        }
        return lessonMapper.lessonEntitiesToLessonViewDtoList(lessonEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public int numberOfFilteredLessons(final String trainerName, final String danceStyleName, final String date) {
        final Specification<LessonEntity> specification = LessonRepository.hasTrainerNameAndDanceStyleNameAndDate(
                trainerName, danceStyleName, date);
        final long numberOfFilteredLessons = lessonRepository.count(specification);
        if (numberOfFilteredLessons != 0) {
            log.info("There have been lessons.");
        } else {
            log.warn("There haven't been lessons.");
        }
        return (int) numberOfFilteredLessons;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonViewDto> listUserEntities(final int userId, final Pageable pageable) {
        final Optional<UserEntity> userEntity = userRepository.findById(userId);
        userEntity.ifPresentOrElse(
                (booking) -> log.info("User with id = {} has been found.", userId),
                () -> {
                    log.warn("User with id = {} hasn't been found.", userId);
                    throw new EntityNotFoundException("User not found!");
                });

        final List<LessonEntity> lessonEntities = lessonRepository.findAllByUserTrainerId(userId, pageable);
        if (lessonEntities.size() != 0) {
            log.info("Lessons have been found.");
        } else {
            log.warn("There haven't been lessons.");
        }
        return lessonMapper.lessonEntitiesToLessonViewDtoList(lessonEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public int numberOfUserEntities(final int userId) {
        final int numberOfUserLessons = lessonRepository.countAllByUserTrainerId(userId);
        if (numberOfUserLessons != 0) {
            log.info("There have been lessons.");
        } else {
            log.warn("There haven't been lessons.");
        }
        return numberOfUserLessons;
    }

}
