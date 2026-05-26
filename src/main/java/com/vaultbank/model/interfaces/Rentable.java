package com.vaultbank.model.interfaces;

/**
 * Implementada por los productos que generan rendimiento para el cliente
 * (CuentaAhorro, CDT).
 */
public interface Rentable {

    double calcularRentabilidad();

    double proyectarRendimiento(int meses);

    double getTasaEfectiva();
}
