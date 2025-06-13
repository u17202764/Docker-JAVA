package com.example.demodockerfile.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String correo;
    private String clave;
}
