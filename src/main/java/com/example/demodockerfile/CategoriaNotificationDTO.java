package com.example.demodockerfile;



import com.example.demodockerfile.entity.CategoriaEntity;

import com.example.demodockerfile.common.TipoAccion;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoriaNotificationDTO {
    private CategoriaEntity categoria;
    private TipoAccion tipoAccion;

}
