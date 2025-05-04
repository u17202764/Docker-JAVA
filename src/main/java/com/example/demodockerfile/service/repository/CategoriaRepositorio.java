package com.example.demodockerfile.service.repository;


import com.example.demodockerfile.entity.Categoria;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface CategoriaRepositorio extends CrudRepository<Categoria, Integer> {
    Optional<Categoria> findByNombre(String nombre);
}
