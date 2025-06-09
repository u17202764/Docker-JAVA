package com.example.demodockerfile.validation_error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ValidationException extends RuntimeException {


    private final ErrorResponseDTO errorResponseDTO;
    private final HttpStatus httpStatus;

    public ValidationException(HttpStatus httpStatus, ErrorResponseDTO errorResponseDTO) {
        super(errorResponseDTO.getMessage());
        this.httpStatus = httpStatus;
        this.errorResponseDTO = errorResponseDTO;
    }




    public static void lanzarError(HttpStatus httpStatus,  String message,String messageDeveloper) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .message(message)
                .developerMessage(messageDeveloper)
                .build();
        throw new ValidationException(httpStatus, errorResponseDTO);
    }

}
