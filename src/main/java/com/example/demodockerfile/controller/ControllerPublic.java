package com.example.demodockerfile.controller;

import com.example.demodockerfile.entity.CategoriaEntity;
import com.example.demodockerfile.entity.ProductoEntity;
import com.example.demodockerfile.request.CreateClienteDto;
import com.example.demodockerfile.request.LoginRequest;
import com.example.demodockerfile.service.CategoriaService;
import com.example.demodockerfile.service.ProductoService;
import com.example.demodockerfile.utils.ResponseResult;
import com.example.demodockerfile.utils.SearchDTO;
import com.example.demodockerfile.utils.SearchOperation;
import com.example.demodockerfile.utils.SortDTO;
import com.example.demodockerfile.utils.jwt.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    private ProductoService productoService;
    @Autowired
    private AuthService authService;
    private SearchDTO searchDTO;
    private SortDTO sortDTO;

    @PostMapping("/registrarCliente")
    public ResponseEntity<?> registrarCliente(@RequestBody CreateClienteDto request) {
        log.info("Intentando registrar cliente con correo: {}", request.getCorreo());
        CreateClienteDto res = authService.createCliente(request);
        return ResponseResult.of("Cliente registrado exitosamente", res, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginDTO) {
        log.info("Intentando iniciar sesión con usuario: {}", loginDTO.getCorreo());

        try {
            String token = authService.session(loginDTO);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            return ResponseResult.of("Inicio de sesión exitoso", null, HttpStatus.OK, headers);
        } catch (BadCredentialsException e) {
            log.error("Error de credenciales: {}", e.getMessage());
            return ResponseResult.of("Credenciales inválidas", null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Error al iniciar sesión: {}", e.getMessage());
            return ResponseResult.of("Error al iniciar sesión", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/categoria")
    public ResponseEntity listado() {
        log.info("Listando categorias");
        List<CategoriaEntity> listado = (List<CategoriaEntity>) categoriaService.listar();
        if (listado.isEmpty()) {
            return ResponseResult.of("No hay categorias", null, HttpStatus.NOT_FOUND);
        }
        log.info("Categorias encontradas: {}", listado.size());
        listado.forEach(categoria -> log.info("Categoria: {}", categoria.getNombre()));
        listado = listado.stream().filter(categoria -> categoria.isActivo()).toList();
        if (listado.isEmpty()) {
            return ResponseResult.of("No hay categorias activas", null, HttpStatus.NOT_FOUND);
        }
        log.info("Categorias activas encontradas: {}", listado.size());
        return ResponseResult.of("Listado de categorias", listado, HttpStatus.OK);
    }


    @GetMapping("/listadoProductos")
    public ResponseEntity<?> listarProductos(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortField", required = false) String sortField,
            @RequestParam(value = "sortDirection", required = false) boolean sortDirection,
            @RequestParam(value = "searchOperation", required = false) SearchOperation searchOperation,
            @RequestParam(value = "searchKey", required = false) String searchKey,
            @RequestParam(value = "searchValue", required = false) String searchValue) {
        log.info("Listando productos con paginación: página {}, tamaño {}, campo de ordenamiento {}, dirección de ordenamiento {}, operación de búsqueda {}, clave de búsqueda {}, valor de búsqueda {}",
                page, size, sortField, sortDirection, searchOperation, searchKey, searchValue);
        if (searchKey != null) {
            if (searchOperation == null) {
                lanzarError(HttpStatus.BAD_REQUEST, " La operación de búsqueda es obligatoria cuando se proporciona una clave de búsqueda", "searchOperation");
            }
            if (searchValue == null || searchValue.isBlank()) {
                lanzarError(HttpStatus.BAD_REQUEST, " El valor de búsqueda es obligatorio cuando se proporciona una clave de búsqueda", "searchValue");
            }
            searchDTO = new SearchDTO(searchKey, searchOperation, searchValue);
        }
        if (sortField != null) {
            sortDTO = new SortDTO(sortField, sortDirection);
        }
        Page<ProductoEntity> productos = productoService.listarPaginado(page, size, sortDTO, searchDTO);

        if (productos == null || productos.isEmpty()) {
            log.warn("No se encontraron productos en la página {} con tamaño {}", page, size);
            return ResponseResult.of("No hay categorías disponibles", null, HttpStatus.NOT_FOUND);
        }
        log.info("Categorías encontradas: {}", productos.getContent());
        return ResponseResult.of("Listado de productos", productos, HttpStatus.OK);
    }



}
