package com.example.demodockerfile.controller;

import com.example.demodockerfile.entity.ClienteEntity;
import com.example.demodockerfile.entity.DetallePedidoEntity;
import com.example.demodockerfile.entity.PedidoEntity;
import com.example.demodockerfile.entity.ProductoEntity;
import com.example.demodockerfile.request.PedidoRequest;
import com.example.demodockerfile.resoponse.DetallePedidoResponse;
import com.example.demodockerfile.resoponse.PedidoResponse;
import com.example.demodockerfile.service.ClienteService;
import com.example.demodockerfile.service.DetallePedidoService;
import com.example.demodockerfile.service.PedidoService;
import com.example.demodockerfile.service.ProductoService;
import com.example.demodockerfile.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demodockerfile.validation_error.ValidationException.lanzarError;

@Slf4j
@RestController
@RequestMapping("/api/cliente")
public class ControllerClient {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private DetallePedidoService detallePedidoService;

    @GetMapping("/v1/cliente/token")
    public ResponseEntity<?> verPerfilUser() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Consultando perfil para el correo: {}", correo);
        Optional<ClienteEntity> clienteOpt = clienteService.optionalClienteEntity(correo);
        log.info("Cliente encontrado: {}", clienteOpt.isPresent() ? clienteOpt.get().getNombre() : "No encontrado");
        if (clienteOpt.isEmpty()) {
            lanzarError(HttpStatus.NOT_FOUND, "No existe cliente con el correo " + correo,
                    "No se encontró el cliente con el correo proporcionado");
        }
        clienteOpt.get().getUser().setPassword("*******"); // No exponer la contraseña
        return ResponseResult.of("Perfil del usuario", clienteOpt, HttpStatus.OK);
    }

    @PostMapping("/addCarrito")
    public ResponseEntity<?> addCarrito(@RequestBody PedidoRequest pedidoRequest) {
        log.info("Añadiendo productos al carrito: {}", pedidoRequest.getProductos());
        if (pedidoRequest.getProductos() == null || pedidoRequest.getProductos().isEmpty()) {
            lanzarError(HttpStatus.BAD_REQUEST, "La lista de productos no puede estar vacía", "productosId");
        }

        // Validar y calcular pedido
        List<DetallePedidoResponse> detalles = new ArrayList<>();
        BigDecimal total = calcularTotalYDetalles(pedidoRequest, detalles);

        Optional<ClienteEntity> clienteEntityOptional = clienteService.optionalClienteEntity(SecurityContextHolder.getContext().getAuthentication().getName());
        if (clienteEntityOptional.isEmpty()) {
            lanzarError(HttpStatus.NOT_FOUND, "Cliente no encontrado", "cliente");
        }
        if (clienteEntityOptional.get().getUser() == null) {
            lanzarError(HttpStatus.UNAUTHORIZED, "Usuario no autenticado", "usuario");
        }
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(clienteEntityOptional.get().getUser().getId());


        PedidoEntity pedidoGenerado = pedidoService.crearPedido(total, pedidoRequest.getProductos().size(), cliente);
        log.info("Pedido creado con ID: {}", pedidoGenerado.getIdPedido());


        PedidoResponse pedidoResponse = buildPedidoResponse(pedidoGenerado, clienteEntityOptional.get(), detalles);

        return ResponseResult.of("Pedido Generado ", pedidoResponse, HttpStatus.OK);
    }


  @GetMapping("/pedidos/detalis/{numeroPedido}")
  private ResponseEntity<?> verDetallesPedido(@PathVariable String numeroPedido) {
        log.info("Consultando detalles del pedido con número: {}", numeroPedido);
      Optional<PedidoEntity> pedidoEntity = pedidoService.optionalPedidoEntity(numeroPedido);
        if (pedidoEntity.isEmpty()) {
            lanzarError(HttpStatus.NOT_FOUND, "Pedido no encontrado con el número: " + numeroPedido);
        }
        List<DetallePedidoEntity> detalles = detallePedidoService.buscarPorIdPedido(pedidoEntity.get());
        if (detalles.isEmpty()) {
            lanzarError(HttpStatus.NOT_FOUND, "No se encontraron detalles para el pedido con número: " + numeroPedido);
        }


        return ResponseResult.of("Detalles del Pedido", detalles, HttpStatus.OK);
    }

    private PedidoResponse buildPedidoResponse(PedidoEntity pedidoGenerado, ClienteEntity clienteEntity, List<DetallePedidoResponse> detalles) {
        PedidoResponse pedidoResponse = new PedidoResponse();
        pedidoResponse.setNumeroPedido(pedidoGenerado.getNumeroPedido());
        pedidoResponse.setTotalPagar(pedidoGenerado.getTotalPedido());
        pedidoResponse.setCliente(clienteEntity.getNombre() + "  " + clienteEntity.getApellido());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        pedidoResponse.setFechaPedido(pedidoGenerado.getFechaOperacion().format(formatter));
        pedidoResponse.setMoneda(pedidoGenerado.getMoneda());
        pedidoResponse.setMetodoPago(pedidoGenerado.getMetodoPago());
        pedidoResponse.setDetalles(detalles);
        DetallePedidoEntity detallePedidoEntity = new DetallePedidoEntity();
        for (DetallePedidoResponse detalle : detalles) {
            log.info("Detalle: {} - Cantidad: {} - Subtotal: {}", detalle.getNombreProducto(), detalle.getCantidad(), detalle.getSubTotal());
            detallePedidoEntity.setPedido(pedidoGenerado);
            detallePedidoEntity.setProducto(productoService.buscarProductoPorNombre(detalle.getNombreProducto()).get());
            detallePedidoEntity.setCantidad(detalle.getCantidad());
            detallePedidoEntity.setPrecioUnitario(detalle.getPrecioUnitario());
            detallePedidoEntity.setSubtotal(detalle.getSubTotal());
            detallePedidoService.guardar(detallePedidoEntity);
        }
        return pedidoResponse;
    }

    private BigDecimal calcularTotalYDetalles(PedidoRequest pedidoRequest, List<DetallePedidoResponse> detalles) {
        return pedidoRequest.getProductos().stream()
                .map(prod -> {
                    ProductoEntity productoEncontrado = productoService.buscarPorId(prod.getIdProducto());

                    if (productoEncontrado == null) {
                        lanzarError(HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + prod.getIdProducto(), "productosId");
                    }

                    if (productoEncontrado.getStock() < prod.getCantidadEscogida()) {
                        lanzarError(HttpStatus.BAD_REQUEST, "No hay suficiente stock para el producto: " + productoEncontrado.getNombre(), "productosId");
                    }

                    DetallePedidoResponse detalle = DetallePedidoResponse.builder()
                            .cantidad(prod.getCantidadEscogida())
                            .nombreProducto(productoEncontrado.getNombre())
                            .precioUnitario(productoEncontrado.getPrecio())
                            .subTotal(productoEncontrado.getPrecio()
                                    .multiply(BigDecimal.valueOf(prod.getCantidadEscogida())))
                            .build();

                    detalles.add(detalle);

                    return detalle.getSubTotal();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
