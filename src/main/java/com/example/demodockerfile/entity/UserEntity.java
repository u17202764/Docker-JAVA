package com.example.demodockerfile.entity;

import com.example.demodockerfile.common.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@ToString(exclude = "password")

public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String correo;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
