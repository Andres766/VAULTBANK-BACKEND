package com.vaultbank.model.transacciones;

import com.vaultbank.model.enums.TipoTransaccion;
import com.vaultbank.model.productos.ProductoFinanciero;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("TRANSFERENCIA")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Transferencia extends Transaccion {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_destino_id")
    private ProductoFinanciero productoDestino;

    @Override
    public boolean validar() {
        return getMonto() != null
                && getProductoOrigen() != null
                && productoDestino != null
                && getProductoOrigen().validarOperacion(getMonto().doubleValue());
    }

    @Override
    public void ejecutar() {
        if (!validar()) {
            throw new IllegalStateException("Transferencia invalida");
        }
        getProductoOrigen().debitar(getMonto());
        productoDestino.acreditar(getMonto());
        getProductoOrigen().registrarOperacion("Transferencia enviada $" + getMonto()
                + " a producto " + productoDestino.getId());
        productoDestino.registrarOperacion("Transferencia recibida $" + getMonto()
                + " de producto " + getProductoOrigen().getId());
    }

    @Override
    public String generarComprobante() {
        return "COMPROBANTE TRANSFERENCIA\nMonto: $" + getMonto() + "\nFecha: " + getFecha()
                + "\nOrigen: " + (getProductoOrigen() != null ? getProductoOrigen().getId() : "N/A")
                + "\nDestino: " + (productoDestino != null ? productoDestino.getId() : "N/A");
    }

    @Override
    public TipoTransaccion getTipo() {
        return TipoTransaccion.TRANSFERENCIA;
    }
}
