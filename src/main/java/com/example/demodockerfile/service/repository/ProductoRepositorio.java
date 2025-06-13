package com.example.demodockerfile.service.repository;


import com.example.demodockerfile.entity.ProductoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface ProductoRepositorio extends CrudRepository<ProductoEntity, Integer>  {
    Optional<ProductoEntity> findByNombre(String nombre);

    Page<ProductoEntity> findAll(Specification<ProductoEntity> spec, Pageable pageable);
}
