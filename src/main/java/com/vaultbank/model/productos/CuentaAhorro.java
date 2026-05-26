package com.vaultbank.model.productos;

import com.vaultbank.model.enums.EstadoProducto;
import com.vaultbank.model.enums.TipoProducto;
import com.vaultbank.model.interfaces.Rentable;
import com.vaultbank.model.interfaces.Transferible;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "cuentas_ahorro")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CuentaAhorro extends ProductoFinanciero implements Transferible, Rentable {

    private static final double GMF = 0.004; // 4x1000

    @lombok.Builder.Default
    private double tasaInteresAnual = 0.05;

    @lombok.Builder.Default
    private int limiteRetirosDiarios = 5;

    @lombok.Builder.Default
    private boolean exentoGMF = false;

    // ===== Polimorfismo =====

    /** Interes simple mensual sobre el saldo. */
    @Override
    public double calcularInteres() {
        return getSaldo().doubleValue() * (tasaInteresAnual / 12.0);
    }

    @Override
    public double calcularComision(double monto) {
        return exentoGMF ? 0.0 : monto * GMF;
    }

    @Override
    public boolean validarOperacion(double monto) {
        // No se permite dejar el saldo negativo en cuentas de ahorro.
        if (monto <= 0) return false;
        return getSaldo().doubleValue() - monto >= 0;
    }

    @Override
    public TipoProducto getTipoProducto() {
        return TipoProducto.CUENTA_AHORRO;
    }

    @Override
    public String generarExtracto() {
        return "=== Extracto Cuenta de Ahorro ===\n"
                + "Producto: " + getNombre() + "\n"
                + "Saldo actual: $" + getSaldo() + "\n"
                + "Tasa interes anual: " + (tasaInteresAnual * 100) + "%\n"
                + "Interes mensual estimado: $" + String.format("%.2f", calcularInteres()) + "\n"
                + "Exento GMF: " + (exentoGMF ? "Si" : "No");
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
            throw new IllegalStateException("Transferencia no permitida");
        }
        java.math.BigDecimal valor = java.math.BigDecimal.valueOf(monto);
        this.debitar(valor);
        destino.acreditar(valor);
        registrarOperacion("Transferencia enviada por $" + monto + " a producto " + destino.getId());
    }

    // ===== Rentable =====

    @Override
    public double calcularRentabilidad() {
        return calcularInteres();
    }

    @Override
    public double proyectarRendimiento(int meses) {
        return getSaldo().doubleValue() * (tasaInteresAnual / 12.0) * meses;
    }

    @Override
    public double getTasaEfectiva() {
        return tasaInteresAnual;
    }
}
