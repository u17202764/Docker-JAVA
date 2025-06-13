package com.example.demodockerfile.service;

import com.example.demodockerfile.entity.CategoriaEntity;
import com.example.demodockerfile.service.repository.CategoriaRepositorio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepositorio categoriaRepositorio;
    @Transactional(readOnly = true)
    public Iterable<CategoriaEntity> listar() {
        return categoriaRepositorio.findAll();
    }

    public Optional<CategoriaEntity> buscarPorId(Integer id) {
        return categoriaRepositorio.findById(id);
    }


    public CategoriaEntity guardar(CategoriaEntity categoria) {
        CategoriaEntity c = categoriaRepositorio.save(categoria);
        return c;
    }

    public void eliminar(Integer id) {
        categoriaRepositorio.deleteById(id);
    }

    public boolean existePorId(Integer id) {
        return categoriaRepositorio.existsById(id);
    }

    public Optional<CategoriaEntity> buscarPorNombre(String nombre) {
        return categoriaRepositorio.findByNombre(nombre);
    }

    public Page<CategoriaEntity> listarPaginado(int page, int size, String sortCampo, boolean sortOrden, String searchType, String searchValue, boolean searchValueExact) {
        Pageable pageable = PageRequest.of(page, size,
                sortOrden ? org.springframework.data.domain.Sort.by(sortCampo).ascending() : org.springframework.data.domain.Sort.by(sortCampo).descending());

        Specification<CategoriaEntity> spec = buildSpecification(searchType, searchValue, searchValueExact);
        return categoriaRepositorio.findAll(spec, pageable);

    }

    private static final List<String> CAMPOS_FILTRABLES = List.of("id", "nombre", "activo");

    private Specification<CategoriaEntity> buildSpecification(String searchType, String searchValue, boolean searchValueExact) {
        return (root, query, cb) -> {
            if (isBlank(searchType) || isBlank(searchValue)) {
                return cb.conjunction();
            }

            if (!CAMPOS_FILTRABLES.contains(searchType)) {
                log.warn("Campo '{}' no permitido para filtrado", searchType);
                return cb.conjunction();
            }
            if (searchValueExact) {
                return cb.equal(cb.lower(root.get(searchType).as(String.class)), searchValue.toLowerCase());
            }
            String pattern = "%" + searchValue.toLowerCase() + "%";
            return cb.like(cb.lower(root.get(searchType).as(String.class)), pattern);
        };
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }


}
