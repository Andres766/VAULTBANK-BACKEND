package com.vaultbank.model.productos;

import com.vaultbank.model.enums.EstadoProducto;
import com.vaultbank.model.enums.TipoProducto;
import com.vaultbank.model.interfaces.Simulable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "prestamos_personales")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class PrestamoPersonal extends ProductoFinanciero implements Simulable {

    @lombok.Builder.Default
    private double montoAprobado = 0.0;

    @lombok.Builder.Default
    private double tasaMensual = 0.02;

    @lombok.Builder.Default
    private int numeroCuotas = 12;

    @lombok.Builder.Default
    private int cuotasPagadas = 0;

    @lombok.Builder.Default
    private int diaCorte = 1;

    /** Cuota fija por sistema frances. */
    protected double cuotaFrancesa(double capital, double i, int n) {
        if (n <= 0) return 0.0;
        if (i == 0) return capital / n;
        double factor = Math.pow(1 + i, n);
        return capital * (i * factor) / (factor - 1);
    }

    // ===== Polimorfismo =====

    /** Para un prestamo, "calcularInteres" devuelve la cuota fija mensual (sistema frances). */
    @Override
    public double calcularInteres() {
        return cuotaFrancesa(montoAprobado, tasaMensual, numeroCuotas);
    }

    @Override
    public double calcularComision(double monto) {
        return 0.0;
    }

    @Override
    public boolean validarOperacion(double monto) {
        // Solo se permiten pagos de cuota o abonos sobre prestamos vigentes.
        if (monto <= 0) return false;
        return getEstado() == EstadoProducto.ACTIVO || getEstado() == EstadoProducto.EN_MORA;
    }

    @Override
    public TipoProducto getTipoProducto() {
        return TipoProducto.PRESTAMO_PERSONAL;
    }

    @Override
    public String generarExtracto() {
        return "=== Extracto Prestamo Personal ===\n"
                + "Producto: " + getNombre() + "\n"
                + "Monto aprobado: $" + montoAprobado + "\n"
                + "Tasa mensual: " + (tasaMensual * 100) + "%\n"
                + "Cuotas: " + cuotasPagadas + "/" + numeroCuotas + "\n"
                + "Cuota mensual: $" + String.format("%.2f", calcularInteres());
    }

    // ===== Simulable =====

    @Override
    public double simular(double monto, double tasaMensualSim, int cuotas) {
        return cuotaFrancesa(monto, tasaMensualSim, cuotas);
    }

    @Override
    public String generarTablaAmortizacion() {
        StringBuilder sb = new StringBuilder("Cuota | Valor | Capital | Interes | Saldo\n");
        double cuota = calcularInteres();
        double saldo = montoAprobado;
        for (int k = 1; k <= numeroCuotas; k++) {
            double interes = saldo * tasaMensual;
            double abonoCapital = cuota - interes;
            saldo -= abonoCapital;
            sb.append(String.format("%d | %.2f | %.2f | %.2f | %.2f%n",
                    k, cuota, abonoCapital, interes, Math.max(saldo, 0)));
        }
        return sb.toString();
    }

    @Override
    public String getResumenSimulacion() {
        double cuota = calcularInteres();
        double totalPagar = cuota * numeroCuotas;
        return "Prestamo personal $" + montoAprobado + " a " + numeroCuotas
                + " cuotas. Cuota=$" + String.format("%.2f", cuota)
                + " Total=$" + String.format("%.2f", totalPagar);
    }
}
