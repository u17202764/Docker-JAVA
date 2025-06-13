package com.example.demodockerfile.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateClienteDto {

    private String nombre;


    private String apellido;


    private String correo;


    private String clave;
}
