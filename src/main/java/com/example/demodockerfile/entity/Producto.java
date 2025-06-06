package com.example.demodockerfile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "categoria") // Evita problemas de recursión al imprimir la categoría
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 255)
    private String imagen;

    @Column(nullable = false)
    private Double precio;

    private int stock;

    @ManyToOne(fetch = FetchType.LAZY ) // Relación Many-to-One con Categoria
    @JoinColumn(name = "categoria_id") // Columna en la tabla Producto que almacena la clave foránea
    private Categoria categoria;
}
