package com.example.demodockerfile.utils;

import com.example.demodockerfile.validation_error.ErrorResponseDTO;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ResponseResult<T> {
    private String message;
    private T data;
    private ErrorResponseDTO errorDetails;
    private HttpStatus status;

    public static <T> ResponseEntity<ResponseResult<T>> of(String message, T data, HttpStatus status, HttpHeaders headers) {
        ResponseResult<T> result = ResponseResult.<T>builder()
                .message(message)
                .data(data)
                .status(status)
                .build();

        return ResponseEntity.status(status)
                .headers(headers)
                .body(result);
    }

    public static <T> ResponseEntity<ResponseResult<T>> of(String message, T data, HttpStatus status) {
        ResponseResult<T> result = ResponseResult.<T>builder()
                .message(message)
                .data(data)
                .status(status)
                .build();

        return ResponseEntity.ok(result);
    }



    public static <T> ResponseEntity<ResponseResult<T>> ofError(String message, ErrorResponseDTO errorDetails, HttpStatus status) {
        ResponseResult<T> result = ResponseResult.<T>builder()
                .message(message)
                .errorDetails(errorDetails)
                .status(status)
                .build();

        return  ResponseEntity.status(status)
                .body(result);
    }




}
