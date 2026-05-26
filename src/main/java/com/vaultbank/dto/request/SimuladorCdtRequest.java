package com.vaultbank.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SimuladorCdtRequest {

    @Positive
    private double capital;

    @Positive
    private int plazoMeses;

    /** Tasa efectiva anual en decimal (ej: 0.10). */
    @Positive
    private double tasaEfectivaAnual;
}
