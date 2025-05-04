package com.example.demodockerfile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productoXD1")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = "categoria")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "imagen", length = 255)
    private String imagen;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "stock")
    private int stock;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;
}
