package com.example.demodockerfile.service.repository;


import com.example.demodockerfile.entity.Producto;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface ProductoRepositorio extends CrudRepository<Producto, Integer> {
    Optional<Producto> findByNombre(String nombre);
    boolean existsByCategoria_Id(Integer categoriaId);
}
