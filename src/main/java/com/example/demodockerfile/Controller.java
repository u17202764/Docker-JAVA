package com.example.demodockerfile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {
    // Aquí puedes definir tus métodos y endpoints
    // Por ejemplo, un endpoint de prueba
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Docker V1!";
    }

    @GetMapping("/")
    public String helloV() {
        return "Hello, Docker!";
    }

    @GetMapping("/listado")
    public ResponseEntity listado() {
        List<Categoria> listado = List.of(
                new Categoria(1, "Categoria 1"),
                new Categoria(2, "Categoria 2"),
                new Categoria(3, "Categoria 3")
        );
        return ResponseEntity.ok(listado);
    }



}
