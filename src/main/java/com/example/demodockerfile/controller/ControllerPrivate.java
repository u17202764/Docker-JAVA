package com.example.demodockerfile.controller;

import com.example.demodockerfile.service.NotificationService;
import com.example.demodockerfile.entity.Categoria;
import com.example.demodockerfile.entity.Producto;
import com.example.demodockerfile.service.CategoriaService;
import com.example.demodockerfile.service.ProductoService;
import com.example.demodockerfile.service.TipoAccion;
import com.example.demodockerfile.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/private")
public class ControllerPrivate {

    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/crearCategoria")
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria categoria) {
        log.info("Creando categoria: {}", categoria);
        if (categoria.getId() != null) {
            return ResponseResult.error("El id de la categoria no debe ser enviado", HttpStatus.BAD_REQUEST);
        }
        if (categoria.getNombre() == null || categoria.getNombre().isEmpty()) {
            return ResponseResult.error("El nombre de la categoria no debe ser vacio", HttpStatus.BAD_REQUEST);
        }
        Categoria nuevaCategoria = categoriaService.guardar(categoria);
        notificationService.sentNotificationSocket(nuevaCategoria, TipoAccion.AGREGAR);

        return ResponseResult.success("Categoria creada", nuevaCategoria, HttpStatus.CREATED);
    }

     @DeleteMapping("/eliminarCategoria/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Integer id) {
         log.info("Eliminando categoria con id: {}", id);
         Optional<Categoria> categoria = categoriaService.buscarPorId(id);
         if (categoria.isEmpty()) {
             return ResponseResult.error("No existe categoria con el id " + id, HttpStatus.NOT_FOUND);
         }
         categoriaService.eliminar(id);
        // notificationService.sentNotificationSocket(categoria.orElse(null), TipoAccion.ELIMINAR);
         return ResponseResult.success("Categoria eliminada", null, HttpStatus.NO_CONTENT);
     }

    @GetMapping("/listadoCategoria")
    public ResponseEntity listado() {
        log.info("Listando categorias");
        List<Categoria> listado = (List<Categoria>) categoriaService.listar();

        if (listado.isEmpty()) {
            return ResponseResult.success("No hay categorias", null, HttpStatus.NOT_FOUND);
        }
        return ResponseResult.success("Listado de categorias", listado, HttpStatus.OK);
    }
    @Value("${url.imagenes}") // configurable desde application.properties
    private String urlImagenes;

    @GetMapping("/listarProducto")
    public ResponseEntity<?> listarProducto() {
        log.info("Listando productos");

        List<Producto> listado = (List<Producto>) productoService.listar();

        listado.forEach(p -> {
            // Si la imagen no está seteada, asigna la imagen por defecto
            if (p.getImagen() == null || p.getImagen().isBlank()) {
                p.setImagen(urlImagenes + "default.png");
            }
        });

        if (listado.isEmpty()) {
            return ResponseResult.success("No hay producto", null, HttpStatus.NOT_FOUND);
        }

        return ResponseResult.success("Listado de producto", listado, HttpStatus.OK);
    }


    @PostMapping("/guardarProducto")
    public ResponseEntity<?> guardar(@ModelAttribute Producto producto,
                                     @RequestParam(value = "img", required = false) MultipartFile img) {
        log.info("Guardando producto: {}", producto);
        if (producto.getIdProducto() != null) {
            return ResponseResult.error("El id del producto no debe ser enviado", HttpStatus.BAD_REQUEST);
        }

        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            return ResponseResult.error("El id de la categoría no debe ser nulo", HttpStatus.BAD_REQUEST);
        }

        if (categoriaService.buscarPorId(producto.getCategoria().getId()).isEmpty()) {
            return ResponseResult.error("No existe categoría con el id enviado: " + producto.getCategoria().getId(), HttpStatus.BAD_REQUEST);
        }


        if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
            return ResponseResult.error("nombre obligatorio", HttpStatus.BAD_REQUEST);
        }
        if (productoService.buscarProductoPorNombre(producto.getNombre()).isPresent()) {
            return ResponseResult.error("Ya existe un producto con el nombre " + producto.getNombre(), HttpStatus.BAD_REQUEST);
        }
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            return ResponseResult.error("Precio obligatorio", HttpStatus.BAD_REQUEST);
        }
        Producto productoGuardado = productoService.guardar(producto, img);
        if (productoGuardado == null) {
            return ResponseResult.error("Error al guardar el producto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseResult.success("Producto guardado", productoGuardado, HttpStatus.CREATED);
    }

    @GetMapping("/buscarProducto/{id}")
    public ResponseEntity<?> buscarProducto(@PathVariable Integer id) {
        log.info("Buscando producto con id: {}", id);
        Producto producto = productoService.buscarPorId(id);
        if (producto == null) {
            return ResponseResult.error("No existe producto con el id " + id, HttpStatus.NOT_FOUND);
        }
        return ResponseResult.success("Producto encontrado", producto, HttpStatus.OK);
    }

    @DeleteMapping("/eliminarProducto/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Integer id) {
        log.info("Eliminando producto con id: {}", id);
        Producto producto = productoService.buscarPorId(id);
        if (producto == null) {
            return ResponseResult.error("No existe producto con el id " + id, HttpStatus.NOT_FOUND);
        }
        productoService.eliminar(id);
        return ResponseResult.success("Producto eliminado", null, HttpStatus.NO_CONTENT);
    }


}
