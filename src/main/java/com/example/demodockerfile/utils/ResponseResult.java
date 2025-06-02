package com.example.demodockerfile.utils;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ResponseResult<T> {
    private String message;
    private T data;
    private HttpStatus status;

    public static <T> ResponseEntity<ResponseResult<T>> success(String message, T data, HttpStatus status) {
        ResponseResult<T> result = ResponseResult.<T>builder()
                .message(message)
                .data(data)
                .status(status)
                .build();

        return ResponseEntity.ok(result);
    }

    public static <T> ResponseEntity<ResponseResult<T>> error(String message, HttpStatus status) {
        ResponseResult<T> result = ResponseResult.<T>builder()
                .message(message)
                .status(status)
                .build();

        return ResponseEntity.status(status).body(result);
    }




}
