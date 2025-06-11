package com.example.demodockerfile.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum SearchOperation {
    EXACTO,
    MAYOR,
    MENOR,
    MAYOR_IGUAL,
    MENOR_IGUAL,
    INEXACTO;


    public static SearchOperation from(String value) {
        if (value == null || value.trim().isEmpty()) {
            log.warn("Operación de búsqueda no proporcionada, se considerará como 'null'");
            return null; // No se envió operación, retorna null
        }
        try {
            return SearchOperation.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            log.warn("Operación de búsqueda '{}' no válida, se considerará como 'null'", value);
            return null; // Si no coincide con ninguna, retorna null
        }

    }
}