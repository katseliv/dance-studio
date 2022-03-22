package com.dataart.dancestudio.service.model.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
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

        public LessonViewDtoBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public LessonViewDtoBuilder trainerFirstName(String trainerFirstName) {
            this.trainerFirstName = trainerFirstName;
            return this;
        }

        public LessonViewDtoBuilder trainerLastName(String trainerLastName) {
            this.trainerLastName = trainerLastName;
            return this;
        }

        public LessonViewDtoBuilder danceStyleName(String danceStyleName) {
            this.danceStyleName = danceStyleName;
            return this;
        }

        public LessonViewDtoBuilder startDatetime(LocalDateTime startDatetime) {
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
