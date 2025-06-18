package com.example.demodockerfile.request;

import com.example.demodockerfile.common.MetodoPago;
import com.example.demodockerfile.common.Moneda;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PedidoRequest {
    private List<ProductoRequest> productos;
    private MetodoPago metodoPago;


}
