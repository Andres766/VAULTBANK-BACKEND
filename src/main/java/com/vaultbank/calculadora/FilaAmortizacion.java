package com.vaultbank.calculadora;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Una fila de la tabla de amortizacion (resultado de simulacion, no persistido). */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilaAmortizacion {
    private int numeroCuota;
    private double valorCuota;
    private double abonoCapital;
    private double abonoInteres;
    private double saldoRestante;
}
