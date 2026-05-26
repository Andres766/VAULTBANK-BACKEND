package com.vaultbank.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class AhorroProyectadoRequest {

    @PositiveOrZero
    private double saldoInicial;

    @PositiveOrZero
    private double depositoMensual;

    @Positive
    private double tasaMensual;

    @Positive
    private int meses;
}
