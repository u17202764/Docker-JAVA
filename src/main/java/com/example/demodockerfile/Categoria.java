package com.example.demodockerfile;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Categoria {
    public Categoria( ) {

    }

    public Categoria(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Integer id;
    String nombre;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
