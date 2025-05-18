package com.example.demodockerfile;



import com.example.demodockerfile.entity.Categoria;

import com.example.demodockerfile.service.TipoAccion;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoriaNotificationDTO {
    private Categoria categoria;
    private TipoAccion tipoAccion;

}
