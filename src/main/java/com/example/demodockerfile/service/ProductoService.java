package com.example.demodockerfile.service;

import com.example.demodockerfile.entity.ProductoEntity;
import com.example.demodockerfile.service.repository.ProductoRepositorio;
import com.example.demodockerfile.utils.SearchDTO;
import com.example.demodockerfile.utils.SearchOperation;
import com.example.demodockerfile.utils.SortDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.demodockerfile.validation_error.ValidationException.lanzarError;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@Service
public class ProductoService {
    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Transactional(readOnly = true)
    public List<ProductoEntity> listarProductos() {
        List<ProductoEntity> productos = (List<ProductoEntity>) productoRepositorio.findAll();
        return productos.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    public ProductoEntity guardar(ProductoEntity producto) {
        return productoRepositorio.save(producto);
    }


    public ProductoEntity buscarPorId(Integer id) {
        return productoRepositorio.findById(id).orElse(null);
    }

    public String imagenNombreUnico(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        return fileName;
    }

    public Optional<ProductoEntity> buscarProductoPorNombre(String nombre) {
        return productoRepositorio.findByNombre(nombre);
    }

    public void eliminar(Integer id) {
        try {
            productoRepositorio.deleteById(id);
        } catch (Exception e) {
            log.error("Error al eliminar el producto", e);
            throw new RuntimeException("Error al eliminar el producto", e);
        }
    }

    public List<ProductoEntity> litadoProducto() {
        return (List<ProductoEntity>) productoRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Page<ProductoEntity> listarPaginado(int page, int size, SortDTO sortDTO, SearchDTO searchDTO) {

        Pageable pageable = construirPageable(page, size, sortDTO);

        Specification<ProductoEntity> spec = buildSpecification(searchDTO);

        Page<ProductoEntity> productos = productoRepositorio.findAll(spec, pageable);


        return new PageImpl<>(productos.getContent(), pageable, productos.getTotalElements());
    }

    private String obtenerCampoOrdenamiento(SortDTO sortDTO) {
        log.info("Obteniendo campo de ordenamiento desde SortDTO: {}", sortDTO);
        final String CAMPO_POR_DEFECTO = "nombre";
        String sortField = Optional.ofNullable(sortDTO)
                .map(SortDTO::getSortField)
                .filter(field -> !field.isBlank())
                .orElse(CAMPO_POR_DEFECTO);

        if (!CAMPOS_FILTRABLES.contains(sortField)) {
            log.warn("Campo '{}' no permitido para ordenamiento.", sortField);
            lanzarError(HttpStatus.BAD_REQUEST, "Campo no permitido para ordenamiento",
                    "Los campos permitidos son: " + CAMPOS_FILTRABLES);
        }
        return sortField;
    }


    private Pageable construirPageable(int page, int size, SortDTO sortDTO) {
        String sortField = obtenerCampoOrdenamiento(sortDTO);
        return PageRequest.of(page, size, (sortDTO != null && Boolean.TRUE.equals(sortDTO.isSortDirection()))
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending());
    }


    private static final List<String> CAMPOS_FILTRABLES = List.of("nombre", "precio", "stock");


    private Specification<ProductoEntity> buildSpecification(SearchDTO searchDTO) {
        log.info("Construyendo especificación para búsqueda: {}", searchDTO);
        if (searchDTO == null || isBlank(searchDTO.getSearchKey()) || isBlank(searchDTO.getSearchValue()) || searchDTO.getSearchOperation() == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            if (!CAMPOS_FILTRABLES.contains(searchDTO.getSearchKey())) {
                log.warn("Campo '{}' no permitido  para búsqueda", searchDTO.getSearchKey());
                lanzarError(HttpStatus.BAD_REQUEST, " Campo no permitido para búsqueda",
                        "Los campos permitidos son: " + CAMPOS_FILTRABLES);
            }
            if (searchDTO.getSearchOperation() == null) {
                log.warn("Operación de búsqueda no especificada  para el campo '{}'", searchDTO.getSearchKey());
                lanzarError(HttpStatus.BAD_REQUEST, "Operación de búsqueda no especificada",
                        "Debes enviar searchOperation, los valores permitidos son: " + SearchOperation.values());
            }
            if (isBlank(searchDTO.getSearchValue())) {
                log.warn("Valor de búsqueda no especificado  para el campo '{}'", searchDTO.getSearchKey());
                lanzarError(HttpStatus.BAD_REQUEST, "Valor de búsqueda no especificado",
                        "Debes enviar searchValue, el valor no puede estar vacío");
            }

            switch (searchDTO.getSearchOperation()) {
                case INEXACTO:// para texto
                    return cb.like(root.get(searchDTO.getSearchKey()), "%" + searchDTO.getSearchValue() + "%");
                case EXACTO: // para texto y números
                    return cb.equal(root.get(searchDTO.getSearchKey()), searchDTO.getSearchValue());
                case MAYOR:
                    return cb.greaterThan(root.get(searchDTO.getSearchKey()), searchDTO.getSearchValue());
                case MENOR:
                    return cb.lessThan(root.get(searchDTO.getSearchKey()), searchDTO.getSearchValue());
                case MAYOR_IGUAL:
                    return cb.greaterThanOrEqualTo(root.get(searchDTO.getSearchKey()), searchDTO.getSearchValue());
                case MENOR_IGUAL:
                    return cb.lessThanOrEqualTo(root.get(searchDTO.getSearchKey()), searchDTO.getSearchValue());

                default:
                    log.info("Operación de búsqueda '{}' no válida, se usará INEXACTO por defecto", searchDTO.getSearchOperation());
                    lanzarError(HttpStatus.BAD_REQUEST, "Operación de búsqueda no válida",
                            "Los valores permitidos son: " + SearchOperation.values());
            }
            return null;
        };
    }


    public Optional<ProductoEntity> obtenerPorId(Integer id) {
        log.info("Obteniendo producto por ID: {}", id);
        return Optional.ofNullable(productoRepositorio.findById(id)).orElse(null);
    }

}
