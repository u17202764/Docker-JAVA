package com.example.demodockerfile.controller;

import com.example.demodockerfile.entity.Categoria;
import com.example.demodockerfile.service.CategoriaService;
import com.example.demodockerfile.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/public")
public class ControllerPublic {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/listadoCategoria")
    public ResponseEntity listado() {
        log.info("Listando categorias");
        List<Categoria> listado = (List<Categoria>) categoriaService.listar();
        if (listado.isEmpty()) {
            return ResponseResult.success("No hay categorias", null, HttpStatus.NOT_FOUND);
        }
        return ResponseResult.success("Listado de categorias", listado, HttpStatus.OK);
    }

    @PostMapping("/crearCategoria")
    public ResponseEntity<?> crearCategoria(Categoria categoria) {
        log.info("Creando categoria: {}", categoria);
        if (categoria.getId() != null) {
            return ResponseResult.error("El id de la categoria no debe ser enviado", HttpStatus.BAD_REQUEST);
        }
        if (categoria.getNombre() == null || categoria.getNombre().isEmpty()) {
            return ResponseResult.error("El nombre de la categoria no debe ser vacio", HttpStatus.BAD_REQUEST);
        }
        Categoria nuevaCategoria = categoriaService.guardar(categoria);
        return ResponseResult.success("Categoria creada", nuevaCategoria, HttpStatus.CREATED);
    }


}
