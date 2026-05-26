package com.vaultbank.model.productos;

import com.vaultbank.model.enums.EstadoProducto;
import com.vaultbank.model.enums.TipoProducto;
import com.vaultbank.model.interfaces.Transferible;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "cuentas_corriente")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CuentaCorriente extends ProductoFinanciero implements Transferible {

    @lombok.Builder.Default
    private double cupoSobregiro = 1_000_000.0;

    @lombok.Builder.Default
    private double comisionMantenimiento = 12000.0;

    @lombok.Builder.Default
    private boolean chequera = false;

    // ===== Polimorfismo =====

    /** La cuenta corriente no genera interes; cobra comision fija mensual. */
    @Override
    public double calcularInteres() {
        return 0.0;
    }

    @Override
    public double calcularComision(double monto) {
        return comisionMantenimiento;
    }

    @Override
    public boolean validarOperacion(double monto) {
        if (monto <= 0) return false;
        // Permite saldo negativo hasta el cupo de sobregiro.
        return getSaldo().doubleValue() + cupoSobregiro - monto >= 0;
    }

    @Override
    public TipoProducto getTipoProducto() {
        return TipoProducto.CUENTA_CORRIENTE;
    }

    @Override
    public String generarExtracto() {
        return "=== Extracto Cuenta Corriente ===\n"
                + "Producto: " + getNombre() + "\n"
                + "Saldo actual: $" + getSaldo() + "\n"
                + "Cupo sobregiro: $" + cupoSobregiro + "\n"
                + "Comision mantenimiento: $" + comisionMantenimiento + "\n"
                + "Chequera: " + (chequera ? "Si" : "No");
    }

    // ===== Transferible =====

    @Override
    public boolean validarTransferencia(ProductoFinanciero destino, double monto) {
        return destino != null
                && destino.getEstado() == EstadoProducto.ACTIVO
                && validarOperacion(monto);
    }

    @Override
    public void transferir(ProductoFinanciero destino, double monto) {
        if (!validarTransferencia(destino, monto)) {
            throw new IllegalStateException("Transferencia no permitida (excede cupo de sobregiro)");
        }
        java.math.BigDecimal valor = java.math.BigDecimal.valueOf(monto);
        this.debitar(valor);
        destino.acreditar(valor);
        registrarOperacion("Transferencia enviada por $" + monto + " a producto " + destino.getId());
    }
}
