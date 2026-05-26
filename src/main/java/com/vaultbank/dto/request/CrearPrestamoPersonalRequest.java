package com.vaultbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CrearPrestamoPersonalRequest {

    @NotNull
    private Long clienteId;

    @NotBlank
    private String nombre;

    @Positive
    private double montoAprobado;

    @Positive
    private double tasaMensual;

    @Positive
    private int numeroCuotas;

    private int diaCorte = 1;
}
