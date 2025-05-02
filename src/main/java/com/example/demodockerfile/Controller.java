package com.example.demodockerfile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {
    private final CategoriaRepositorio categoriaRepositorio;
    public Controller(CategoriaRepositorio categoriaRepositorio) {
        this.categoriaRepositorio = categoriaRepositorio;
    }
    @GetMapping("/listado")
    public ResponseEntity listado() {
        List<Categoria> listado = (List<Categoria>) categoriaRepositorio.findAll();
        return ResponseEntity.ok(listado);
    }



}
