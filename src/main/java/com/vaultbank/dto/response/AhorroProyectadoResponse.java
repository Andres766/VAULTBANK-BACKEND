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
public class AhorroProyectadoResponse {
    private double saldoFinal;
    private double totalDepositado;
    private double interesesGanados;
    private List<Double> saldoMensual;
}
