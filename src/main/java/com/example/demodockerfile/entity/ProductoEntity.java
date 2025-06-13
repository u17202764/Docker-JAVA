package com.example.demodockerfile.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "categoria")
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 255)
    private String imagen;

    @Column(nullable = false)
    private BigDecimal precio;

    private int stock;

    private boolean activo;

    @ManyToOne(fetch = FetchType.EAGER) // Relación Many-to-One con Categoria
    @JoinColumn(name = "categoria_id") // Columna en la tabla Producto que almacena la clave foránea
    private CategoriaEntity categoria;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;
}
