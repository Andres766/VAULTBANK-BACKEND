package com.vaultbank.dto.request;

import com.vaultbank.model.enums.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String cedula;

    private String telefono;

    /** Si no se envia, se registra como CLIENTE. */
    private Rol rol;

    // Datos opcionales para perfil de cliente
    private Double ingresosMensuales;
    private String ocupacion;
}
