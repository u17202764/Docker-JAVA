package com.example.demodockerfile.controller;

import com.example.demodockerfile.dto.AuthResponse;
import com.example.demodockerfile.dto.CreateClienteDto;
import com.example.demodockerfile.dto.LoginDTO;
import com.example.demodockerfile.entity.Categoria;
import com.example.demodockerfile.service.CategoriaService;
import com.example.demodockerfile.utils.ResponseResult;
import com.example.demodockerfile.utils.jwt.JwtService;
import com.example.demodockerfile.utils.jwt.services.AuthService;
import com.example.demodockerfile.utils.jwt.services.JwtServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demodockerfile.validation_error.ValidationException.lanzarError;

@Slf4j
@RestController
@RequestMapping("/api/public")
public class ControllerPublic {

    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private AuthService authService;


    @PostMapping("/registrarCliente")
    public ResponseEntity<?> registrarCliente(@RequestBody CreateClienteDto request) {
        log.info("Intentando registrar cliente con correo: {}", request.getCorreo());
        CreateClienteDto res = authService.createCliente(request);
        return ResponseResult.of("Cliente registrado exitosamente", res, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        log.info("Intentando iniciar sesión con usuario: {}", loginDTO.getCorreo());

        try {
            String token = authService.session(loginDTO);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            return ResponseResult.of("Inicio de sesión exitoso", null, HttpStatus.OK, headers);

        } catch (BadCredentialsException e) {
            log.warn("Credenciales incorrectas para usuario: {}", loginDTO.getCorreo());
            lanzarError(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas", "Por favor, verifica tu correo y contraseña.");
          return ResponseResult.of("Credenciales incorrectas", null, HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping("/listadoCategoria")
    public ResponseEntity listado() {
        log.info("Listando categorias");
        List<Categoria> listado = (List<Categoria>) categoriaService.listar();
        if (listado.isEmpty()) {
            return ResponseResult.of("No hay categorias", null, HttpStatus.NOT_FOUND);
        }
        return ResponseResult.of("Listado de categorias", listado, HttpStatus.OK);
    }


}
