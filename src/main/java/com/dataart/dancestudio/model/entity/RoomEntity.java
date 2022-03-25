package com.dataart.dancestudio.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomEntity {

    private final Integer id;
    private final String name;
    private final String description;
    private final Integer studioId;
    private final Boolean isDeleted;

    public static RoomEntityBuilder builder() {
        return new RoomEntityBuilder();
    }

    public static class RoomEntityBuilder {

        private Integer id;
        private String name;
        private String description;
        private Integer studioId;
        private Boolean isDeleted;

        public boolean isUsed = false;

        public RoomEntityBuilder id(final Integer id) {
            this.id = id;
            return this;
        }

        public RoomEntityBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public RoomEntityBuilder description(final String description) {
            this.description = description;
            return this;
        }

        public RoomEntityBuilder studioId(final Integer studioId) {
            this.studioId = studioId;
            return this;
        }

        public RoomEntityBuilder isDeleted(final Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public RoomEntity build(){
            if (!isUsed){
                isUsed = true;
                return new RoomEntity(id, name, description, studioId, isDeleted);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
