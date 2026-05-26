package com.vaultbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CrearCuentaCorrienteRequest {

    @NotNull
    private Long clienteId;

    @NotBlank
    private String nombre;

    @PositiveOrZero
    private double saldoInicial;

    private double cupoSobregiro = 1_000_000.0;

    private double comisionMantenimiento = 12000.0;

    private boolean chequera = false;
}
