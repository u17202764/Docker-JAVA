package com.example.demodockerfile.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "detalle_pedido")
public class DetallePedidoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_detalle_pedido")
	private Long idDetallePedido;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_pedido", nullable = false)
	private PedidoEntity pedido;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_producto", nullable = false)
	private ProductoEntity producto;

	private int cantidad;

	private BigDecimal precioUnitario;

	private BigDecimal subtotal;


}
