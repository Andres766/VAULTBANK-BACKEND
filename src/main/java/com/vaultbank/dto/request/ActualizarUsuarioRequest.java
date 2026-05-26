package com.vaultbank.dto.request;

import lombok.Data;

@Data
public class ActualizarUsuarioRequest {
    private String nombre;
    private String apellido;
    private String telefono;
}
