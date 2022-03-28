package com.dataart.dancestudio.model.entity.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookingViewEntity {

    private final Integer id;
    private final String firstName;
    private final String lastName;
    private final String danceStyle;
    private final LocalDateTime startDatetime;

    public static BookingViewEntityBuilder builder(){
        return new BookingViewEntityBuilder();
    }

    public static class BookingViewEntityBuilder {

        private Integer id;
        private String firstName;
        private String lastName;
        private String danceStyle;
        private LocalDateTime startDatetime;

        public boolean isUsed = false;

        public BookingViewEntityBuilder() {
        }

        public BookingViewEntityBuilder id(final Integer id){
            this.id = id;
            return this;
        }

        public BookingViewEntityBuilder firstName(final String firstName){
            this.firstName = firstName;
            return this;
        }

        public BookingViewEntityBuilder lastName(final String lastName){
            this.lastName = lastName;
            return this;
        }

        public BookingViewEntityBuilder danceStyle(final String danceStyle){
            this.danceStyle = danceStyle;
            return this;
        }

        public BookingViewEntityBuilder startDatetime(final LocalDateTime startDatetime){
            this.startDatetime = startDatetime;
            return this;
        }

        public BookingViewEntity build(){
            if (!isUsed){
                isUsed = true;
                return new BookingViewEntity(id, firstName, lastName, danceStyle, startDatetime);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
