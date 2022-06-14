package com.dataart.dancestudio.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Builder
@ToString
public class LikeId implements Serializable {

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "user_id")
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
