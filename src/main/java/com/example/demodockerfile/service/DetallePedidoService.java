package com.example.demodockerfile.service;

import com.example.demodockerfile.entity.DetallePedidoEntity;

import com.example.demodockerfile.entity.PedidoEntity;
import com.example.demodockerfile.service.repository.DetallePedidoRepositorio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DetallePedidoService {
    @Autowired
    private DetallePedidoRepositorio detallePedidoRepositorio;

    public DetallePedidoEntity guardar(DetallePedidoEntity detallePedido) {
        log.info("Guardando detalle de pedido: {}", detallePedido);
        return detallePedidoRepositorio.save(detallePedido);
    }

    public List<DetallePedidoEntity> buscarPorIdPedido(PedidoEntity pedidoEntity) {
        log.info("Buscando detalle de pedido con ID: {}", pedidoEntity);
        List<DetallePedidoEntity> detalles = detallePedidoRepositorio.findByPedido(pedidoEntity);
        if (detalles.isEmpty()) {
            log.warn("No se encontraron detalles para el pedido con ID: {}", pedidoEntity);
        } else {
            log.info("Detalles encontrados: {}", detalles.size());
        }
        return detalles;
    }
}
