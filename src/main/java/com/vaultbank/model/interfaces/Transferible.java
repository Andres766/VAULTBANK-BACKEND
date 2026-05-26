package com.vaultbank.model.interfaces;

import com.vaultbank.model.productos.ProductoFinanciero;

/**
 * Implementada por los productos que permiten enviar dinero a otra cuenta
 * (CuentaAhorro, CuentaCorriente).
 */
public interface Transferible {

    boolean validarTransferencia(ProductoFinanciero destino, double monto);

    void transferir(ProductoFinanciero destino, double monto);
}
