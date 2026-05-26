package com.vaultbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CrearCdtRequest {

    @NotNull
    private Long clienteId;

    @NotBlank
    private String nombre;

    @Positive
    private double capitalInicial;

    @Positive
    private int plazoMeses;

    @Positive
    private double tasaEfectivaAnual;
}
