package com.vaultbank.dto.response;

import com.vaultbank.calculadora.FilaAmortizacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimuladorPrestamoResponse {
    private double cuotaMensual;
    private double totalIntereses;
    private double totalPagar;
    private double monto;
    private int plazoMeses;
    private List<FilaAmortizacion> tablaAmortizacion;
}
