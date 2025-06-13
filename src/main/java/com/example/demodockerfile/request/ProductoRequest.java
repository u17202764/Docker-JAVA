package com.example.demodockerfile.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductoRequest {
    int idProducto;
    int cantidadEscogida;
}
