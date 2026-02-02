package com.test.identity_access_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, unique = true)
    private String token;

    private LocalDateTime createDateTime;

    private LocalDateTime expires;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
