package com.dataart.dancestudio.db.entity.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
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

        public LessonViewEntityBuilder id(Integer id){
            this.id = id;
            return this;
        }

        public LessonViewEntityBuilder trainerFirstName(String trainerFirstName){
            this.trainerFirstName = trainerFirstName;
            return this;
        }

        public LessonViewEntityBuilder trainerLastName(String trainerLastName){
            this.trainerLastName = trainerLastName;
            return this;
        }

        public LessonViewEntityBuilder danceStyleName(String danceStyleName){
            this.danceStyleName = danceStyleName;
            return this;
        }

        public LessonViewEntityBuilder startDatetime(LocalDateTime startDatetime){
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
