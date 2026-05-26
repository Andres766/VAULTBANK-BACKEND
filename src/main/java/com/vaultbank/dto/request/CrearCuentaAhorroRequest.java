package com.vaultbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CrearCuentaAhorroRequest {

    @NotNull
    private Long clienteId;

    @NotBlank
    private String nombre;

    @PositiveOrZero
    private double saldoInicial;

    private double tasaInteresAnual = 0.05;

    private int limiteRetirosDiarios = 5;

    private boolean exentoGMF = false;
}
