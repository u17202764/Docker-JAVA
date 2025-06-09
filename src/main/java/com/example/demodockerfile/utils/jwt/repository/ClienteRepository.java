package com.example.demodockerfile.utils.jwt.repository;


import com.example.demodockerfile.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    Optional<ClienteEntity> findByUserCorreo(String correo);
}
