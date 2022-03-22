package com.dataart.dancestudio.service.model.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DanceStyleViewDto {

    private final Integer id;

    private final String name;

    private final String description;

    public static DanceStyleViewDtoBuilder builder() {
        return new DanceStyleViewDtoBuilder();
    }

    public static class DanceStyleViewDtoBuilder {

        private Integer id;

        private String name;

        private String description;

        private boolean isUsed = false;

        public DanceStyleViewDtoBuilder() {
        }

        public DanceStyleViewDtoBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public DanceStyleViewDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DanceStyleViewDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public DanceStyleViewDto build() {
            if (!isUsed) {
                isUsed = true;
                return new DanceStyleViewDto(id, name, description);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
