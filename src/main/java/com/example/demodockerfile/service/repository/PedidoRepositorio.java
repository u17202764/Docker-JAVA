package com.example.demodockerfile.service.repository;


import com.example.demodockerfile.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PedidoRepositorio extends JpaRepository<PedidoEntity, Integer> {

    Optional<PedidoEntity> findByNumeroPedido(String numeroPedido);

}
