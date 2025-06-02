package com.example.demodockerfile.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "categoriaXD1")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String nombre;

    private boolean activo;


}
