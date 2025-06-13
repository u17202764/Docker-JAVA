package com.example.demodockerfile.service;

import com.example.demodockerfile.entity.ClienteEntity;
import com.example.demodockerfile.utils.jwt.repository.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public Optional<ClienteEntity> optionalClienteEntity(String correo) {
        return Optional.ofNullable(clienteRepository.findByUserCorreoFetchUser(correo))
                .orElse(null);
    }
}
