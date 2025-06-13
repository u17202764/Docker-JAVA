package com.example.demodockerfile.resoponse;

import com.example.demodockerfile.common.MetodoPago;
import com.example.demodockerfile.common.Moneda;
import com.example.demodockerfile.request.ProductoRequest;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PedidoResponse {
    private String numeroPedido;
    private String fechaPedido;
    private String Cliente;
    private List<DetallePedidoResponse> detalles;
    private MetodoPago metodoPago;
    private BigDecimal totalPagar;
    private Moneda moneda;

}
