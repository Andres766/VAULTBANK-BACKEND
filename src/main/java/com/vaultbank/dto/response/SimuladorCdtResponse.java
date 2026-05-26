package com.vaultbank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimuladorCdtResponse {
    private double capital;
    private int plazoMeses;
    private double valorVencimiento;
    private double interesesGanados;
    private double rentabilidadEfectiva;
    /** Crecimiento mes a mes (para grafica de barras). */
    private List<Double> crecimientoMensual;
}
