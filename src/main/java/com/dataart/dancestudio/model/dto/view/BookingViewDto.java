package com.dataart.dancestudio.model.dto.view;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
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

        public BookingViewDtoBuilder id(final Integer id) {
            this.id = id;
            return this;
        }

        public BookingViewDtoBuilder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public BookingViewDtoBuilder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public BookingViewDtoBuilder danceStyle(final String danceStyle) {
            this.danceStyle = danceStyle;
            return this;
        }

        public BookingViewDtoBuilder startDatetime(final LocalDateTime startDatetime) {
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
