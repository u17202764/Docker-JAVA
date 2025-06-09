package com.example.demodockerfile.utils.jwt.services;

import com.example.demodockerfile.dto.CreateClienteDto;
import com.example.demodockerfile.dto.LoginDTO;
import com.example.demodockerfile.entity.ClienteEntity;
import com.example.demodockerfile.entity.LoginLogEntity;
import com.example.demodockerfile.entity.UserEntity;
import com.example.demodockerfile.entity.dto.EstadoCliente;
import com.example.demodockerfile.entity.dto.UserRole;
import com.example.demodockerfile.utils.jwt.CustomUserDetails;
import com.example.demodockerfile.utils.jwt.repository.ClienteRepository;
import com.example.demodockerfile.utils.jwt.repository.LoginLogRepository;
import com.example.demodockerfile.utils.jwt.repository.UserRepository;
import com.example.demodockerfile.validation_error.ErrorResponseDTO;
import com.example.demodockerfile.validation_error.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.demodockerfile.validation_error.ValidationException.lanzarError;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements UserDetailsService {

    private JwtServiceImpl jwtService;
    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final LoginLogRepository loginLogRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByCorreo(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }


    @Transactional
    public String session(LoginDTO loginDTO) {
        UserDetails user = loadUserByUsername(loginDTO.getCorreo());

        if (!new BCryptPasswordEncoder().matches(loginDTO.getClave(), user.getPassword())) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }

       /* ClienteEntity cliente = clienteRepository.findByUserCorreo(loginDTO.getCorreo()).get();

        if (cliente == null) {
            lanzarError(HttpStatus.NOT_FOUND, "Cliente no encontrado", "No se encontró un cliente asociado a este usuario.");
        }

        if (cliente.getEstado() != EstadoCliente.ACTIVO) {
            lanzarError(HttpStatus.FORBIDDEN, "Cuenta inactiva", "La cuenta está inactiva. Contacte al administrador.");
        }
        UserEntity userEntity = userRepository.findByCorreo(loginDTO.getCorreo())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + loginDTO.getCorreo()));

        log.info("Cliente autenticado: {} {}", cliente.getNombre(), cliente.getApellido());
        log.info("USER {} ", cliente.getUser());
        // Registrar log de sesión
        LoginLogEntity loginLog = LoginLogEntity.builder()
                .user(userEntity)
                .ip("123") // implementa esta utilidad con Spring
                .build();

        LoginLogEntity logss = loginLogRepository.save(loginLog);
        log.info("Login log guardado: {}", logss);

        */
        return jwtService.generateToken(user);
    }


    @Transactional
    public CreateClienteDto createCliente(CreateClienteDto request) {
        userRepository.findByCorreo(request.getCorreo())
                .ifPresent(user -> {
                    lanzarError(HttpStatus.BAD_REQUEST, "El correo ya está en uso", "El correo proporcionado ya está asociado a una cuenta existente.");
                });

        UserEntity userEntity = UserEntity.builder()
                .correo(request.getCorreo())
                .password(new BCryptPasswordEncoder().encode(request.getClave()))
                .role(UserRole.USER)
                .build();
        userRepository.save(userEntity);

        ClienteEntity clienteEntity = ClienteEntity.builder()
                .user(userEntity) // Asociar el usuario al cliente
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .estado(EstadoCliente.ACTIVO) // Asumiendo que el estado por defecto es ACTIVO
                .build();
        clienteRepository.save(clienteEntity); // Asociar el usuario al cliente


        CreateClienteDto response = new CreateClienteDto();
        response.setCorreo(userEntity.getCorreo());
        response.setNombre(clienteEntity.getNombre());
        response.setApellido(clienteEntity.getApellido());
        response.setClave("********"); // No devolver la clave por seguridad
        return response;
    }


}