package com.example.demodockerfile.dto;

import com.example.demodockerfile.entity.Producto;
import lombok.Data;

@Data
public class ProductoDTO {
    private Integer idProducto;
    private String nombre;
    private String imagen;
    private Double precio;
    private int stock;
    private String categoriaNombre;

    public ProductoDTO(Producto producto) {
        this.idProducto = producto.getIdProducto();
        this.nombre = producto.getNombre();
    z
        this.precio = producto.getPrecio();
        this.stock = producto.getStock();
        this.categoriaNombre = producto.getCategoria().getNombre(); // esto funciona dentro de la transacción
    }


}
