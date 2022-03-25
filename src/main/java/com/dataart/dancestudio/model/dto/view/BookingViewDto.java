package com.dataart.dancestudio.service.model.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookingViewDto {

    private final Integer id;

    private final String firstName;

    private final String lastName;

    private final String danceStyle;

    private final LocalDateTime startDatetime;

    public static BookingViewDtoBuilder builder() {
        return new BookingViewDtoBuilder();
    }

    public static class BookingViewDtoBuilder {

        private Integer id;

        private String firstName;

        private String lastName;

        private String danceStyle;

        private LocalDateTime startDatetime;

        private boolean isUsed = false;

        public BookingViewDtoBuilder() {
        }

        public BookingViewDtoBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public BookingViewDtoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public BookingViewDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public BookingViewDtoBuilder danceStyle(String danceStyle) {
            this.danceStyle = danceStyle;
            return this;
        }

        public BookingViewDtoBuilder startDatetime(LocalDateTime startDatetime) {
            this.startDatetime = startDatetime;
            return this;
        }

        public BookingViewDto build(){
            if (!isUsed) {
                isUsed = true;
                return new BookingViewDto(id, firstName, lastName, danceStyle, startDatetime);
            }
            throw new RuntimeException("Builder already built");
        }

    }


}
