package com.vaultbank.model.interfaces;

/**
 * Implementada por los productos que se pueden simular antes de solicitarse
 * (PrestamoPersonal, PrestamoHipotecario, CDT).
 */
public interface Simulable {

    double simular(double monto, double tasaMensual, int numeroCuotas);

    String generarTablaAmortizacion();

    String getResumenSimulacion();
}
