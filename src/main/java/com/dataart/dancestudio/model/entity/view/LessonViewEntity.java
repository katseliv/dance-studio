package com.dataart.dancestudio.model.entity.view;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class LessonViewEntity {

    private final Integer id;
    private final String trainerFirstName;
    private final String trainerLastName;
    private final String danceStyleName;
    private final LocalDateTime startDatetime;

    public static LessonViewEntityBuilder builder(){
        return new LessonViewEntityBuilder();
    }

    public static class LessonViewEntityBuilder {

        private Integer id;
        private String trainerFirstName;
        private String trainerLastName;
        private String danceStyleName;
        private LocalDateTime startDatetime;

        public boolean isUsed = false;

        public LessonViewEntityBuilder() {
        }

        public LessonViewEntityBuilder id(final Integer id){
            this.id = id;
            return this;
        }

        public LessonViewEntityBuilder trainerFirstName(final String trainerFirstName){
            this.trainerFirstName = trainerFirstName;
            return this;
        }

        public LessonViewEntityBuilder trainerLastName(final String trainerLastName){
            this.trainerLastName = trainerLastName;
            return this;
        }

        public LessonViewEntityBuilder danceStyleName(final String danceStyleName){
            this.danceStyleName = danceStyleName;
            return this;
        }

        public LessonViewEntityBuilder startDatetime(final LocalDateTime startDatetime){
            this.startDatetime = startDatetime;
            return this;
        }

        public LessonViewEntity build(){
            if (!isUsed){
                isUsed = true;
                return new LessonViewEntity(id, trainerFirstName, trainerLastName, danceStyleName, startDatetime);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
