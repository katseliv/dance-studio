package com.dataart.dancestudio.model.dto.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomViewDto {

    private final Integer id;
    private final String name;
    private final String description;
    private final Integer studioId;
    private final Boolean isDeleted;

    public static RoomViewDtoBuilder builder() {
        return new RoomViewDtoBuilder();
    }

    public static class RoomViewDtoBuilder {

        private Integer id;
        private String name;
        private String description;
        private Integer studioId;
        private Boolean isDeleted;

        public boolean isUsed = false;

        public RoomViewDtoBuilder id(final Integer id) {
            this.id = id;
            return this;
        }

        public RoomViewDtoBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public RoomViewDtoBuilder description(final String description) {
            this.description = description;
            return this;
        }

        public RoomViewDtoBuilder studioId(final Integer studioId) {
            this.studioId = studioId;
            return this;
        }

        public RoomViewDtoBuilder isDeleted(final Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public RoomViewDto build(){
            if (!isUsed){
                isUsed = true;
                return new RoomViewDto(id, name, description, studioId, isDeleted);
            }
            throw new RuntimeException("Builder already built");
        }

    }


}
