package com.vaultbank.dto.request;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class ActualizarTasaRequest {

    @DecimalMin("0.0")
    private double valor;

    private String descripcion;

    private boolean vigente = true;
}
