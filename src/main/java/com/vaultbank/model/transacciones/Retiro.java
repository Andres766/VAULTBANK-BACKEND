package com.vaultbank.model.transacciones;

import com.vaultbank.model.enums.TipoTransaccion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("RETIRO")
@NoArgsConstructor
@SuperBuilder
public class Retiro extends Transaccion {

    @Override
    public boolean validar() {
        return getMonto() != null
                && getProductoOrigen() != null
                && getProductoOrigen().validarOperacion(getMonto().doubleValue());
    }

    @Override
    public void ejecutar() {
        if (!validar()) {
            throw new IllegalStateException("Retiro invalido: fondos insuficientes o no permitido");
        }
        getProductoOrigen().debitar(getMonto());
        getProductoOrigen().registrarOperacion("Retiro por $" + getMonto());
    }

    @Override
    public String generarComprobante() {
        return "COMPROBANTE RETIRO\nMonto: $" + getMonto() + "\nFecha: " + getFecha()
                + "\nProducto: " + (getProductoOrigen() != null ? getProductoOrigen().getId() : "N/A");
    }

    @Override
    public TipoTransaccion getTipo() {
        return TipoTransaccion.RETIRO;
    }
}
