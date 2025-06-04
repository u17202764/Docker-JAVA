package com.example.demodockerfile.service.repository;


import com.example.demodockerfile.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface CategoriaRepositorio extends JpaRepository<Categoria, Integer> {
    Optional<Categoria> findByNombre(String nombre);

    Page<Categoria> findAll(Specification<Categoria> spec, Pageable pageable);
}
