package com.vaultbank.model.transacciones;

import com.vaultbank.model.enums.TipoTransaccion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("DEPOSITO")
@NoArgsConstructor
@SuperBuilder
public class Deposito extends Transaccion {

    @Override
    public boolean validar() {
        return getMonto() != null && getMonto().signum() > 0 && getProductoOrigen() != null;
    }

    @Override
    public void ejecutar() {
        if (!validar()) {
            throw new IllegalStateException("Deposito invalido");
        }
        getProductoOrigen().acreditar(getMonto());
        getProductoOrigen().registrarOperacion("Deposito por $" + getMonto());
    }

    @Override
    public String generarComprobante() {
        return "COMPROBANTE DEPOSITO\nMonto: $" + getMonto() + "\nFecha: " + getFecha()
                + "\nProducto: " + (getProductoOrigen() != null ? getProductoOrigen().getId() : "N/A");
    }

    @Override
    public TipoTransaccion getTipo() {
        return TipoTransaccion.DEPOSITO;
    }
}
