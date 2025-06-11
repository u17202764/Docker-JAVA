package com.example.demodockerfile.service.repository;


import com.example.demodockerfile.entity.Categoria;
import com.example.demodockerfile.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface ProductoRepositorio extends CrudRepository<Producto, Integer>  {
    Optional<Producto> findByNombre(String nombre);

    Page<Producto> findAll(Specification<Producto> spec, Pageable pageable);
}
