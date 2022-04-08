package com.dataart.dancestudio.model.dto.view;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class LessonViewDto {

    private final Integer id;
    private final String trainerFirstName;
    private final String trainerLastName;
    private final String danceStyleName;
    private final LocalDateTime startDatetime;

    public static LessonViewDtoBuilder builder() {
        return new LessonViewDtoBuilder();
    }

    public static class LessonViewDtoBuilder {

        private Integer id;
        private String trainerFirstName;
        private String trainerLastName;
        private String danceStyleName;
        private LocalDateTime startDatetime;

        private boolean isUsed = false;

        public LessonViewDtoBuilder() {
        }

        public LessonViewDtoBuilder id(final Integer id) {
            this.id = id;
            return this;
        }

        public LessonViewDtoBuilder trainerFirstName(final String trainerFirstName) {
            this.trainerFirstName = trainerFirstName;
            return this;
        }

        public LessonViewDtoBuilder trainerLastName(final String trainerLastName) {
            this.trainerLastName = trainerLastName;
            return this;
        }

        public LessonViewDtoBuilder danceStyleName(final String danceStyleName) {
            this.danceStyleName = danceStyleName;
            return this;
        }

        public LessonViewDtoBuilder startDatetime(final LocalDateTime startDatetime) {
            this.startDatetime = startDatetime;
            return this;
        }

        public LessonViewDto build() {
            if (!isUsed) {
                isUsed = true;
                return new LessonViewDto(id, trainerFirstName, trainerLastName, danceStyleName, startDatetime);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
