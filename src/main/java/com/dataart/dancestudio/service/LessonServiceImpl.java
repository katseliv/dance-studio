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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class LessonServiceImpl implements LessonService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    @Autowired
    public LessonServiceImpl(final UserRepository userRepository, final BookingRepository bookingRepository,
                             final LessonRepository lessonRepository, final LessonMapper lessonMapper) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
    }

    @Override
    public int createLesson(final LessonDto lessonDto) {
        final Optional<UserEntity> userEntity = userRepository.findById(lessonDto.getUserTrainerId());
        if (userEntity.isPresent() && userEntity.get().getRole() == Role.TRAINER) {
            final LessonEntity lessonEntity = lessonRepository.save(lessonMapper.lessonDtoToLessonEntity(lessonDto));
            final int id = lessonEntity.getId();
            log.info("Lesson with id = {} has been created.", id);
            return id;
        } else {
            log.warn("Lesson hasn't been created.");
            throw new EntityCreationException("User doesn't have role TRAINER!");
        }
    }

    @Override
    public LessonDto getLessonById(final int id) {
        final Optional<LessonEntity> lessonEntity = lessonRepository.findById(id);
        lessonEntity.ifPresentOrElse(
                (lesson) -> log.info("Lesson with id = {} has been found.", lesson.getId()),
                () -> log.warn("Lesson hasn't been found."));
        return lessonEntity.map(lessonMapper::lessonEntityToLessonDto)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found!"));
    }

    @Override
    public LessonViewDto getLessonViewById(final int id) {
        final Optional<LessonEntity> lessonEntity = lessonRepository.findById(id);
        lessonEntity.ifPresentOrElse(
                (lesson) -> log.info("Lesson with id = {} has been found.", lesson.getId()),
                () -> log.warn("Lesson hasn't been found."));
        return lessonEntity.map(lessonMapper::lessonEntityToLessonViewDto)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found!"));
    }

    @Override
    public void updateLessonById(final LessonDto lessonDto, final int id) {
        final Optional<LessonEntity> lessonEntityOptional = lessonRepository.findById(id);
        if (lessonEntityOptional.isPresent()) {
            log.info("Lesson with id = {} has been found.", id);
        } else {
            log.warn("Lesson with id = {} hasn't been found.", id);
            throw new EntityNotFoundException("Lesson not found!");
        }
        final LessonEntity lessonEntity = lessonMapper.lessonDtoToLessonEntity(lessonDto);
        lessonEntity.setId(id);
        lessonRepository.save(lessonEntity);
        log.info("Lesson with id = {} has been updated.", id);
    }

    @Override
    public void deleteLessonById(final int id) {
        final Optional<LessonEntity> lessonEntity = lessonRepository.findById(id);
        if (lessonEntity.isPresent()) {
            log.info("Lesson with id = {} has been found.", id);
        } else {
            log.warn("Lesson with id = {} hasn't been found.", id);
            throw new EntityNotFoundException("Lesson not found!");
        }
        lessonRepository.markAsDeletedById(id);
        bookingRepository.markAsDeletedByLessonId(id);
        log.info("Lesson with id = {} has been deleted.", id);
    }

    @Override
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
    public List<LessonViewDto> listUserLessons(final int userId, final Pageable pageable) {
        final List<LessonEntity> lessonEntities = lessonRepository.findAllByUserTrainerId(userId, pageable);
        if (lessonEntities.size() != 0) {
            log.info("Lessons have been found.");
        } else {
            log.warn("There haven't been lessons.");
        }
        return lessonMapper.lessonEntitiesToLessonViewDtoList(lessonEntities);
    }

    @Override
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
    public int numberOfUserLessons(final int userId) {
        final int numberOfUserLessons = lessonRepository.countAllByUserTrainerId(userId);
        if (numberOfUserLessons != 0) {
            log.info("There have been lessons.");
        } else {
            log.warn("There haven't been lessons.");
        }
        return numberOfUserLessons;
    }

}
