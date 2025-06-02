package com.example.demodockerfile.service;

import aj.org.objectweb.asm.Opcodes;
import com.example.demodockerfile.entity.Categoria;
import com.example.demodockerfile.service.repository.CategoriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    public Iterable<Categoria> listar() {
        return categoriaRepositorio.findAll();
    }

    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaRepositorio.findById(id);
    }

    public Categoria guardar(Categoria categoria) {
        Categoria c = categoriaRepositorio.save(categoria);
        return c;
    }

    public void eliminar(Integer id) {
        categoriaRepositorio.deleteById(id);
    }

    public boolean existePorId(Integer id) {
        return categoriaRepositorio.existsById(id);
    }

    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepositorio.findByNombre(nombre);
    }

    public Pageable obtenerPaginacion(int page, int size) {
        return PageRequest.of(page, size);
    }


}
