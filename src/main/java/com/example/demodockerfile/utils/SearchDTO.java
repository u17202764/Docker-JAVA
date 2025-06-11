package com.example.demodockerfile.utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar criterios de búsqueda dinámicos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {

    /**
     * Nombre del campo de la entidad a buscar (debe ser mapeable a la base de datos).
     */
    private String searchKey;

    /**
     * Operación de búsqueda (mayor, menor, igual, inexacto, etc.).
     */
    private SearchOperation searchOperation;

    /**
     * Valor que se utilizará para realizar la comparación.
     */
    private String searchValue;

}
