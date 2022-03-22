package com.dataart.dancestudio.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DanceStyleEntity {

    private final Integer id;

    private final String name;

    private final String description;

    public static DanceStyleEntityBuilder builder() {
        return new DanceStyleEntityBuilder();
    }

    public static class DanceStyleEntityBuilder {

        private Integer id;

        private String name;

        private String description;

        private boolean isUsed = false;

        public DanceStyleEntityBuilder() {
        }

        public DanceStyleEntityBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public DanceStyleEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DanceStyleEntityBuilder description(String description) {
            this.description = description;
            return this;
        }

        public DanceStyleEntity build() {
            if (!isUsed) {
                isUsed = true;
                return new DanceStyleEntity(id, name, description);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}