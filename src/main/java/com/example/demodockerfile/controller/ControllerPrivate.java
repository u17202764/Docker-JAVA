package com.example.demodockerfile.controller;

import com.example.demodockerfile.service.NotificationService;
import com.example.demodockerfile.entity.CategoriaEntity;
import com.example.demodockerfile.entity.ProductoEntity;
import com.example.demodockerfile.service.CategoriaService;
import com.example.demodockerfile.service.ProductoService;
import com.example.demodockerfile.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.demodockerfile.validation_error.ValidationException.lanzarError;

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
    public ResponseEntity<?> crearCategoria(@RequestBody CategoriaEntity categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            lanzarError(HttpStatus.BAD_REQUEST, "El nombre de la categoría no puede ser nulo o vacío",
                    "El nombre de la categoría es obligatorio y no puede estar vacío");
        }

        boolean esCreacion = categoria.getId() == null;
        Optional<CategoriaEntity> categoriaExistente = categoriaService.buscarPorNombre(categoria.getNombre());

        if (esCreacion) {
            if (categoriaExistente.isPresent()) {
                log.warn("Ya existe una categoría con el nombre: {}", categoria.getNombre());
                lanzarError(HttpStatus.BAD_REQUEST, " Ya existe una categoría con el nombre " + categoria.getNombre(),
                        "ya está en uso por otra categoría debe  elegir otro nombre");
            }
        } else {
            // Es actualización, verificamos si el nombre ya lo tiene otra categoría distinta
            Optional<CategoriaEntity> actual = categoriaService.buscarPorId(categoria.getId());
            if (actual.isEmpty()) {

                lanzarError(HttpStatus.NOT_FOUND, "No existe categoría con el id " + categoria.getId(),
                        "No se encontró la categoría con el id proporcionado");
            }

            boolean nombreCambio = !actual.get().getNombre().equalsIgnoreCase(categoria.getNombre());

            if (nombreCambio && categoriaExistente.isPresent() &&
                    !categoriaExistente.get().getId().equals(categoria.getId())) {
                log.warn("El nombre '{}' ya está en uso por otra categoría", categoria.getNombre());
                lanzarError(HttpStatus.BAD_REQUEST, "El nombre '" + categoria.getNombre() + "' ya está en uso por otra categoría",
                        "Debe elegir otro nombre para la categoría");
            }

            log.info("Actualizando categoría ID {} con datos: {}", categoria.getId(), categoria);
        }

        CategoriaEntity guardada = categoriaService.guardar(categoria);

        return ResponseResult.of("Categoría " + (esCreacion ? "creada" : "actualizada"), guardada,
                esCreacion ? HttpStatus.CREATED : HttpStatus.OK);
    }

    @GetMapping("/buscarCategoria/{id}")
    public ResponseEntity<?> buscarCategoria(@PathVariable Integer id) {
        log.info("Buscando categoria con id: {}", id);
        Optional<CategoriaEntity> categoria = categoriaService.buscarPorId(id);
        if (categoria.isEmpty()) {
            lanzarError(HttpStatus.NOT_FOUND, "No existe categoria con el id " + id,
                    "No se encontró la categoría con el id proporcionado");
        }
        return ResponseResult.of("Categoria encontrada", categoria.get(), HttpStatus.OK);
    }

    @DeleteMapping("/eliminarCategoria/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Integer id) {
        log.info("Eliminando categoria con id: {}", id);
        Optional<CategoriaEntity> categoria = categoriaService.buscarPorId(id);
        if (categoria.isEmpty()) {
            lanzarError(HttpStatus.NOT_FOUND, "No existe categoria con el id " + id,
                    "No se encontró la categoría con el id proporcionado");
        }
      /*  if(productoService.existeCategoria(id)) {
          categoriaService.eliminar(id);
          return ResponseResult.of("Se eliminar la categoría y productos asociados", HttpStatus.BAD_REQUEST);
        }

       */
        categoriaService.eliminar(id);
        return ResponseResult.of("Categoria eliminada", null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listarCategoria")
    public ResponseEntity<?> listarCategorias() {
        log.info("Listando todas las categorías");
        List<CategoriaEntity> categorias = (List<CategoriaEntity>) categoriaService.listar();
        if (categorias.isEmpty()) {
            return ResponseResult.of("No hay categorías disponibles", null, HttpStatus.NOT_FOUND);
        }
        log.info("Categorías encontradas: {}", categorias);
        return ResponseResult.of("Listado de categorías", categorias, HttpStatus.OK);
    }

    @GetMapping("/listadoCategorias")
    public ResponseEntity<?> listarCategorias(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortCampo", defaultValue = "id") String sortCampo,
            @RequestParam(value = "sortOrden", required = false) boolean sortOrden,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchValue", required = false) String searchValue,
            @RequestParam(value = "searchValueExact", required = false) boolean searchValueExact) {

        log.info("Listando categorías - página: {}, tamaño: {}", page, size);

        Page<CategoriaEntity> categorias = categoriaService.listarPaginado(page, size, sortCampo, sortOrden, searchType, searchValue, searchValueExact);

        if (categorias == null || categorias.isEmpty()) {
            log.warn("No se encontraron categorías en la página {} con tamaño {}", page, size);
            return ResponseResult.of("No hay categorías disponibles", null, HttpStatus.NOT_FOUND);
        }
        log.info("Categorías encontradas: {}", categorias.getContent());

        return ResponseResult.of("Listado de categorías", categorias, HttpStatus.OK);
    }


    @Value("${url.imagenes}") // configurable desde application.properties
    private String urlImagenes;

    @GetMapping("/listarProducto")
    public ResponseEntity<?> listarProducto() {
        log.info("Listando productos");

        List<ProductoEntity> listado = productoService.listarProductos();
        if (listado == null || listado.isEmpty()) {
            log.warn("No se encontraron productos");
            return ResponseResult.of("No hay productos disponibles", null, HttpStatus.NOT_FOUND);
        }
        return ResponseResult.of("Listado de producto", listado, HttpStatus.OK);
    }


    @PostMapping("/guardarProducto")
    public ResponseEntity<?> guardar(@ModelAttribute ProductoEntity producto) {
        log.info("Guardando producto: {}", producto);
        if (producto.getIdProducto() != null) {
            lanzarError(HttpStatus.BAD_REQUEST, "El id del producto no debe ser enviado",
                    "El id del producto no debe ser enviado al crear un nuevo producto");
        }

        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            lanzarError(HttpStatus.BAD_REQUEST, "La categoría del producto es obligatoria",
                    "Debe enviar una categoría válida para el producto");
        }

        if (categoriaService.buscarPorId(producto.getCategoria().getId()).isEmpty()) {
            lanzarError(HttpStatus.NOT_FOUND, "No existe categoría con el id " + producto.getCategoria().getId(),
                    "No se encontró la categoría con el id proporcionado");
        }


        if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
            lanzarError(HttpStatus.BAD_REQUEST, "El nombre del producto es obligatorio",
                    "Debe proporcionar un nombre para el producto");
        }
        if (productoService.buscarProductoPorNombre(producto.getNombre()).isPresent()) {
            lanzarError(HttpStatus.BAD_REQUEST, "El nombre del producto ya existe",
                    "Debe elegir un nombre diferente para el producto");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            lanzarError(HttpStatus.BAD_REQUEST, "El precio del producto debe ser mayor que cero",
                    "Debe proporcionar un precio válido para el producto");
        }
        ProductoEntity productoGuardado = productoService.guardar(producto);
        if (productoGuardado == null) {
            lanzarError(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el producto",
                    "No se pudo guardar el producto debido a un error interno");
        }
        return ResponseResult.of("ProductoEntity guardado", productoGuardado, HttpStatus.CREATED);
    }

    @GetMapping("/buscarProducto/{id}")
    public ResponseEntity<?> buscarProducto(@PathVariable Integer id) {
        log.info("Buscando producto con id: {}", id);
        ProductoEntity producto = productoService.buscarPorId(id);
        if (producto == null) {
            lanzarError(HttpStatus.NOT_FOUND, "No existe producto con el id " + id,
                    "No se encontró el producto con el id proporcionado");
        }
        return ResponseResult.of("ProductoEntity encontrado", producto, HttpStatus.OK);
    }

    @DeleteMapping("/eliminarProducto/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Integer id) {
        log.info("Eliminando producto con id: {}", id);
        ProductoEntity producto = productoService.buscarPorId(id);
        if (producto == null) {
            lanzarError(HttpStatus.NOT_FOUND, "No existe producto con el id " + id,
                    "No se encontró el producto con el id proporcionado");
        }
        productoService.eliminar(id);
        return ResponseResult.of("ProductoEntity eliminado", null, HttpStatus.NO_CONTENT);
    }


}
