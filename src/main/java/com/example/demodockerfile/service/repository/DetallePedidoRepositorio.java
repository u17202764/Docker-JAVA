package com.example.demodockerfile.service.repository;


import com.example.demodockerfile.entity.DetallePedidoEntity;
import com.example.demodockerfile.entity.PedidoEntity;
import com.example.demodockerfile.resoponse.DetallePedidoResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DetallePedidoRepositorio extends JpaRepository<DetallePedidoEntity, Integer> {

    List<DetallePedidoEntity> findByPedido(PedidoEntity pedidoEntity);

}
