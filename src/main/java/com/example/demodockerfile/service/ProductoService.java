package com.example.demodockerfile.service;

import com.example.demodockerfile.dto.ProductoDTO;
import com.example.demodockerfile.entity.Producto;
import com.example.demodockerfile.service.repository.ProductoRepositorio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductoService {
    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Transactional(readOnly = true)
    public List<ProductoDTO> listarProductos() {
        List<Producto> productos = (List<Producto>) productoRepositorio.findAll();
        return productos.stream()
                .map(ProductoDTO::new)
                .collect(Collectors.toList());
    }



//    public boolean existeCategoria(Integer categoriaId) {
//        return productoRepositorio.existsByCategoria_Id(categoriaId);
//    }

    @Transactional
    public Producto guardar(Producto producto) {
        try {

            return productoRepositorio.save(producto);

        } catch (Exception e) {
            log.error("Error al guardar el producto", e);
            throw new RuntimeException("Error al guardar el producto", e);
        }
    }


    public Producto buscarPorId(Integer id) {
        return productoRepositorio.findById(id).orElse(null);
    }

    public String imagenNombreUnico(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        return fileName;
    }

    public Optional<Producto> buscarProductoPorNombre(String nombre) {
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
}
