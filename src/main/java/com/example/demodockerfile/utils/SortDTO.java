package com.example.demodockerfile.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa los parámetros de ordenamiento.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortDTO {

    /**
     * Campo sobre el cual se aplicará el ordenamiento.
     */
    private String sortField;

    /**
     * Dirección del ordenamiento: ASC (ascendente) o DESC (descendente).
     */
    private boolean sortDirection;

}
