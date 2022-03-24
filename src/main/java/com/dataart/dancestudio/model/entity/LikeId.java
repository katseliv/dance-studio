package com.dataart.dancestudio.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class LikeId implements Serializable {

    private Integer postId;
    private Integer userId;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LikeId likeId = (LikeId) o;
        return postId.equals(likeId.postId) && userId.equals(likeId.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }

}
