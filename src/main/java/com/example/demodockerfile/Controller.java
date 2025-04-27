package com.example.demodockerfile;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    // Aquí puedes definir tus métodos y endpoints
    // Por ejemplo, un endpoint de prueba
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Docker V1!";
    }

}
