package com.example.demodockerfile.controller;

import com.example.demodockerfile.ProductoDTO;
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
import org.springframework.data.domain.Page;
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
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            return ResponseResult.error("El nombre de la categoría no debe estar vacío", HttpStatus.BAD_REQUEST);
        }

        boolean esCreacion = categoria.getId() == null;
        Optional<Categoria> categoriaExistente = categoriaService.buscarPorNombre(categoria.getNombre());

        if (esCreacion) {
            if (categoriaExistente.isPresent()) {
                log.warn("Ya existe una categoría con el nombre: {}", categoria.getNombre());
                return ResponseResult.error("Ya existe una categoría con el nombre " + categoria.getNombre(), HttpStatus.BAD_REQUEST);
            }
        } else {
            // Es actualización, verificamos si el nombre ya lo tiene otra categoría distinta
            Optional<Categoria> actual = categoriaService.buscarPorId(categoria.getId());
            if (actual.isEmpty()) {
                return ResponseResult.error("No se encontró la categoría con ID " + categoria.getId(), HttpStatus.NOT_FOUND);
            }

            boolean nombreCambio = !actual.get().getNombre().equalsIgnoreCase(categoria.getNombre());

            if (nombreCambio && categoriaExistente.isPresent() &&
                    !categoriaExistente.get().getId().equals(categoria.getId())) {
                log.warn("El nombre '{}' ya está en uso por otra categoría", categoria.getNombre());
                return ResponseResult.error("Ya existe otra categoría con el nombre " + categoria.getNombre(), HttpStatus.BAD_REQUEST);
            }

            log.info("Actualizando categoría ID {} con datos: {}", categoria.getId(), categoria);
        }

        Categoria guardada = categoriaService.guardar(categoria);

        return ResponseResult.success("Categoría " + (esCreacion ? "creada" : "actualizada"), guardada,
                esCreacion ? HttpStatus.CREATED : HttpStatus.OK);
    }

    @GetMapping("/buscarCategoria/{id}")
    public ResponseEntity<?> buscarCategoria(@PathVariable Integer id) {
        log.info("Buscando categoria con id: {}", id);
        Optional<Categoria> categoria = categoriaService.buscarPorId(id);
        if (categoria.isEmpty()) {
            return ResponseResult.error("No existe categoria con el id " + id, HttpStatus.NOT_FOUND);
        }
        return ResponseResult.success("Categoria encontrada", categoria.get(), HttpStatus.OK);
    }

    @DeleteMapping("/eliminarCategoria/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Integer id) {
        log.info("Eliminando categoria con id: {}", id);
        Optional<Categoria> categoria = categoriaService.buscarPorId(id);
        if (categoria.isEmpty()) {
            return ResponseResult.error("No existe categoria con el id " + id, HttpStatus.NOT_FOUND);
        }
      /*  if(productoService.existeCategoria(id)) {
          categoriaService.eliminar(id);
          return ResponseResult.error("Se eliminar la categoría y productos asociados", HttpStatus.BAD_REQUEST);
        }

       */
        categoriaService.eliminar(id);
        return ResponseResult.success("Categoria eliminada", null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listarCategoria")
    public ResponseEntity<?> listarCategorias() {
        log.info("Listando todas las categorías");
        List<Categoria> categorias = (List<Categoria>) categoriaService.listar();
        if (categorias.isEmpty()) {
            return ResponseResult.success("No hay categorías disponibles", null, HttpStatus.NOT_FOUND);
        }
        log.info("Categorías encontradas: {}", categorias);
        return ResponseResult.success("Listado de categorías", categorias, HttpStatus.OK);
    }

    @GetMapping("/listadoCategorias")
    public ResponseEntity<?> listarCategorias(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortCampo", defaultValue = "id") String sortCampo,
            @RequestParam(value = "sortOrden", required = false ) boolean sortOrden,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchValue", required = false) String searchValue,
            @RequestParam(value = "searchValueExact", required = false ) boolean searchValueExact) {

        log.info("Listando categorías - página: {}, tamaño: {}", page, size);

        Page<Categoria> categorias = categoriaService.listarPaginado(page, size, sortCampo, sortOrden, searchType, searchValue, searchValueExact);

        if (categorias == null || categorias.isEmpty()) {
            log.warn("No se encontraron categorías en la página {} con tamaño {}", page, size);
            return ResponseResult.success("No hay categorías disponibles", null, HttpStatus.NOT_FOUND);
        }
        log.info("Categorías encontradas: {}", categorias.getContent());

        return ResponseResult.success("Listado de categorías", categorias, HttpStatus.OK);
    }


    @Value("${url.imagenes}") // configurable desde application.properties
    private String urlImagenes;

    @GetMapping("/listarProducto")
    public ResponseEntity<?> listarProducto() {
        log.info("Listando productos");

        List<ProductoDTO> listado =   productoService.listarProductos();
        if (listado == null || listado.isEmpty()) {
            log.warn("No se encontraron productos");
            return ResponseResult.success("No hay productos disponibles", null, HttpStatus.NOT_FOUND);
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
        Producto productoGuardado = productoService.guardar(producto);
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
