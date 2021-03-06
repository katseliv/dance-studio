package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.utils.ParseUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Integer>, JpaSpecificationExecutor<LessonEntity> {

    @Modifying
    @Query("UPDATE lessons SET deleted = TRUE WHERE id = ?1")
    void markAsDeletedById(int id);

    @NonNull
    @Override
    Page<LessonEntity> findAll(Specification<LessonEntity> specification, @NonNull Pageable pageable);

    static Specification<LessonEntity> hasTrainerNameAndDanceStyleNameAndDate(final String trainerName,
                                                                              final String danceStyleName,
                                                                              final String date) {
        return (root, query, criteriaBuilder) -> {
            final Predicate trainerNamePredicate = trainerName == null || trainerName.isBlank() ? null : criteriaBuilder.or(
                    criteriaBuilder.like(
                            root.join("userTrainer", JoinType.LEFT)
                                    .get("firstName"), "%" + trainerName + "%"),
                    criteriaBuilder.like(
                            root.join("userTrainer", JoinType.LEFT)
                                    .get("lastName"), "%" + trainerName + "%"));

            final Predicate danceStyleNamePredicate = danceStyleName == null || danceStyleName.isBlank() ? null : criteriaBuilder.like(
                    root.join("danceStyle", JoinType.LEFT)
                            .get("name"), "%" + danceStyleName + "%");

            final Predicate startDatetimePredicate = date == null || date.isBlank() ? null : criteriaBuilder.equal(
                    root.get("startDatetime").as(LocalDate.class), ParseUtils.parseDateString(date));

            final Predicate[] objects = Stream.of(trainerNamePredicate, danceStyleNamePredicate, startDatetimePredicate)
                    .filter(Objects::nonNull)
                    .toArray(Predicate[]::new);

            return criteriaBuilder.and(objects);
        };
    }

    List<LessonEntity> findAllByUserTrainerId(int userId, Pageable pageable);

    int countAllByUserTrainerId(int userId);

}
