package com.example.demodockerfile.service;

import com.example.demodockerfile.entity.ClienteEntity;
import com.example.demodockerfile.entity.PedidoEntity;
import com.example.demodockerfile.service.repository.PedidoRepositorio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class PedidoService {
    @Autowired
    private PedidoRepositorio pedidoRepositorio;
    PedidoEntity pedidoEntity = new PedidoEntity();

    public PedidoEntity crearPedido(BigDecimal total, Integer itemsPedido , ClienteEntity cliente) {
        log.info("Creando un nuevo pedido");
        pedidoEntity.setCliente(cliente);
        pedidoEntity.setCantidadBultosPedido(itemsPedido);
        pedidoEntity.setTotalPedido(total);
        return pedidoRepositorio.save(pedidoEntity);
    }


    public Optional<PedidoEntity> optionalPedidoEntity(Integer idPedido) {
        log.info("Buscando pedido con ID: {}", idPedido);
        return Optional.ofNullable(pedidoRepositorio.findById(idPedido).orElse(null));
    }
}
