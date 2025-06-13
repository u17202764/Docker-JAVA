package com.example.demodockerfile.entity;

import com.example.demodockerfile.common.EstadoPedido;
import com.example.demodockerfile.common.MetodoPago;
import com.example.demodockerfile.common.Moneda;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "pedido")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;

    @Column(name = "numero_pedido", nullable = false, unique = true)
    private String numeroPedido;

    @Column(name = "fecha_operacion", nullable = false)
    private LocalDateTime fechaOperacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pedido", nullable = false)
    private EstadoPedido estadoPedido;

    @Column(name = "cantidad_bultos_pedido", nullable = false)
    private int cantidadBultosPedido;

    @Column(name = "total_pedido", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalPedido;
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false, length = 3)
    private Moneda moneda;
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 20)
    private MetodoPago metodoPago;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente")

    private ClienteEntity cliente;

    @PrePersist
    public void prePersist() {
        this.numeroPedido = UUID.randomUUID().toString();
        this.fechaOperacion = LocalDateTime.now();
        this.estadoPedido = EstadoPedido.REGISTRO_INICIAL; //Registro inicial del pedido
        this.moneda = Moneda.PEN; // Asignar moneda por defecto
        this.metodoPago = MetodoPago.EFECTIVO; // Asignar método de pago por defecto
    }
}
