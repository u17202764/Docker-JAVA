package com.example.demodockerfile.validation_error;

import com.example.demodockerfile.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.warn("Error de validación: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Solicitud mal formada: " + ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
        log.warn("Error de negocio: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        log.error("Error interno: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> ERROR_404(NoHandlerFoundException ex) {
        log.warn("Ruta no encontrada: {}", ex.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.NOT_FOUND.value(),
                        "error", "Ruta no encontrada",
                        "message", ex.getMessage(),
                        "path", ex.getRequestURL()
                )
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> Error400B(ValidationException ex) {
        ErrorResponseDTO errorResponseDTO = ex.getErrorResponseDTO();
        errorResponseDTO.setMessage(ex.getMessage());


        return ResponseResult.ofError(
                errorResponseDTO.getMessage(),
                errorResponseDTO,
                ex.getHttpStatus()
        );
    }


}
