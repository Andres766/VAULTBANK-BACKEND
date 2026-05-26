package com.vaultbank.model.productos;

import com.vaultbank.model.enums.TipoProducto;
import com.vaultbank.model.interfaces.Rentable;
import com.vaultbank.model.interfaces.Simulable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "cdts")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CDT extends ProductoFinanciero implements Rentable, Simulable {

    @lombok.Builder.Default
    private int plazoMeses = 12;

    @lombok.Builder.Default
    private double tasaEfectivaAnual = 0.10;

    private LocalDate fechaVencimiento;

    @lombok.Builder.Default
    private double capitalInicial = 0.0;

    /** Convierte la tasa efectiva anual a su equivalente mensual. */
    private double tasaMensualEquivalente() {
        return Math.pow(1 + tasaEfectivaAnual, 1.0 / 12.0) - 1;
    }

    // ===== Polimorfismo =====

    /** Interes compuesto al vencimiento. */
    @Override
    public double calcularInteres() {
        double valorFinal = capitalInicial * Math.pow(1 + tasaMensualEquivalente(), plazoMeses);
        return valorFinal - capitalInicial;
    }

    @Override
    public double calcularComision(double monto) {
        return 0.0; // El CDT no cobra comision por operacion.
    }

    @Override
    public boolean validarOperacion(double monto) {
        // No se permite retiro antes de la fecha de vencimiento.
        return fechaVencimiento != null && !LocalDate.now().isBefore(fechaVencimiento);
    }

    @Override
    public TipoProducto getTipoProducto() {
        return TipoProducto.CDT;
    }

    @Override
    public String generarExtracto() {
        return "=== Extracto CDT ===\n"
                + "Producto: " + getNombre() + "\n"
                + "Capital inicial: $" + capitalInicial + "\n"
                + "Plazo: " + plazoMeses + " meses\n"
                + "Tasa efectiva anual: " + (tasaEfectivaAnual * 100) + "%\n"
                + "Vencimiento: " + fechaVencimiento + "\n"
                + "Intereses al vencimiento: $" + String.format("%.2f", calcularInteres());
    }

    // ===== Rentable =====

    @Override
    public double calcularRentabilidad() {
        return calcularInteres();
    }

    @Override
    public double proyectarRendimiento(int meses) {
        double valorFinal = capitalInicial * Math.pow(1 + tasaMensualEquivalente(), meses);
        return valorFinal - capitalInicial;
    }

    @Override
    public double getTasaEfectiva() {
        return tasaEfectivaAnual;
    }

    // ===== Simulable =====

    @Override
    public double simular(double monto, double tasaMensual, int numeroCuotas) {
        return monto * Math.pow(1 + tasaMensual, numeroCuotas);
    }

    @Override
    public String generarTablaAmortizacion() {
        return "El CDT no tiene tabla de amortizacion; genera interes compuesto al vencimiento.";
    }

    @Override
    public String getResumenSimulacion() {
        return "CDT capital=$" + capitalInicial + " plazo=" + plazoMeses
                + " meses, intereses=$" + String.format("%.2f", calcularInteres());
    }
}
