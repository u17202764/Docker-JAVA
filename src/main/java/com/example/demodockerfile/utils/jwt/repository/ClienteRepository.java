package com.example.demodockerfile.utils.jwt.repository;


import com.example.demodockerfile.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    // Para evitar LazyInitializationException, con join fetch:
    @Query("SELECT c FROM ClienteEntity c JOIN FETCH c.user u WHERE u.correo = :correo")
    Optional<ClienteEntity> findByUserCorreoFetchUser(@Param("correo") String correo);
}
