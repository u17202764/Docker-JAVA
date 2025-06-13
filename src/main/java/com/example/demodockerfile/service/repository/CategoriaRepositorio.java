package com.example.demodockerfile.service.repository;


import com.example.demodockerfile.entity.CategoriaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CategoriaRepositorio extends JpaRepository<CategoriaEntity, Integer> {
    Optional<CategoriaEntity> findByNombre(String nombre);

    Page<CategoriaEntity> findAll(Specification<CategoriaEntity> spec, Pageable pageable);


}
