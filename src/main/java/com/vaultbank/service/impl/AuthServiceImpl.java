package com.vaultbank.service.impl;

import com.vaultbank.dto.request.LoginRequest;
import com.vaultbank.dto.request.RegisterRequest;
import com.vaultbank.dto.response.AuthResponse;
import com.vaultbank.model.*;
import com.vaultbank.model.enums.PerfilRiesgo;
import com.vaultbank.model.enums.Rol;
import com.vaultbank.repository.UsuarioRepository;
import com.vaultbank.security.JwtService;
import com.vaultbank.service.IAuthService;
import com.vaultbank.exception.OperacionInvalidaException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // Busca por email primero; si no encuentra, intenta por username
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .or(() -> usuarioRepository.findByUsername(request.getEmail()))
                .orElseThrow(() -> new OperacionInvalidaException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new OperacionInvalidaException("Credenciales incorrectas");
        }

        return construirRespuesta(usuario);
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new OperacionInvalidaException("El email ya esta registrado");
        }
        if (usuarioRepository.existsByCedula(request.getCedula())) {
            throw new OperacionInvalidaException("La cedula ya esta registrada");
        }
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new OperacionInvalidaException("El nombre de usuario ya esta en uso");
        }

        Rol rol = request.getRol() != null ? request.getRol() : Rol.CLIENTE;
        Usuario usuario = crearSegunRol(request, rol);
        usuarioRepository.save(usuario);

        return construirRespuesta(usuario);
    }

    @Override
    public AuthResponse refrescarToken(String refreshToken) {
        if (!jwtService.esRefreshTokenValido(refreshToken)) {
            throw new OperacionInvalidaException("Refresh token invalido o expirado");
        }
        String email = jwtService.obtenerEmail(refreshToken);
        Usuario usuario = usuarioRepository.findByEmail(Optional.ofNullable(email).orElse(""))
                .orElseThrow(() -> new OperacionInvalidaException("Usuario no encontrado"));
        return construirRespuesta(usuario);
    }

    /** Polimorfismo de creacion: instancia la subclase de Usuario correcta. */
    private Usuario crearSegunRol(RegisterRequest req, Rol rol) {
        String passwordCodificada = passwordEncoder.encode(req.getPassword());
        return switch (rol) {
            case ADMIN -> Administrador.builder()
                    .nombre(req.getNombre()).apellido(req.getApellido())
                    .email(req.getEmail()).username(req.getUsername())
                    .password(passwordCodificada)
                    .cedula(req.getCedula()).telefono(req.getTelefono()).rol(Rol.ADMIN)
                    .nivelAcceso(3).permisosSistema("TOTAL")
                    .build();
            case CLIENTE -> Cliente.builder()
                    .nombre(req.getNombre()).apellido(req.getApellido())
                    .email(req.getEmail()).username(req.getUsername())
                    .password(passwordCodificada)
                    .cedula(req.getCedula()).telefono(req.getTelefono()).rol(Rol.CLIENTE)
                    .ingresosMensuales(req.getIngresosMensuales() != null
                            ? BigDecimal.valueOf(req.getIngresosMensuales()) : BigDecimal.ZERO)
                    .ocupacion(req.getOcupacion())
                    .perfilRiesgo(PerfilRiesgo.MODERADO)
                    .build();
        };
    }

    private AuthResponse construirRespuesta(Usuario usuario) {
        String token = jwtService.generarToken(usuario.getEmail(), usuario.getRol().name());
        String refresh = jwtService.generarRefreshToken(usuario.getEmail());
        return AuthResponse.builder()
                .userId(usuario.getId())
                .accessToken(token)
                .refreshToken(refresh)
                .email(usuario.getEmail())
                .nombre(usuario.getNombreCompleto())
                .rol(usuario.getRol().name())
                .build();
    }
}
