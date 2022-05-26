package com.dataart.dancestudio.model.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "jwt_tokens")
@Where(clause = "is_deleted = false")
@Table(name = "jwt_tokens")
public class JwtTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "token")
    private String token;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private JwtTokenType type;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "is_deleted")
    private boolean deleted;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final JwtTokenEntity that = (JwtTokenEntity) o;
        return deleted == that.deleted && id.equals(that.id) && token.equals(that.token) && type == that.type && user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, type, user, deleted);
    }

}
