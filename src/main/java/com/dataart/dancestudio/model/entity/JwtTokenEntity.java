package com.dataart.dancestudio.model.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "jwt_tokens")
@Where(clause = "is_deleted = false")
@Table(name = "jwt_tokens", schema = "dancestudio")
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

}
