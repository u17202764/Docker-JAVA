package com.example.demodockerfile.validation_error;

import com.example.demodockerfile.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


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
