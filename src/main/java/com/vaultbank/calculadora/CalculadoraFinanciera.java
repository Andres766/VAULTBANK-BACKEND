package com.vaultbank.calculadora;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Motor de calculo financiero desacoplado (Abstraccion / SOLID).
 * Todos los algoritmos clave estan implementados con RECURSIVIDAD.
 */
@Service
public class CalculadoraFinanciera {

    /**
     * Potencia recursiva: base^exp.
     * Caso base: exp == 0 -> 1
     * Caso recursivo: base * potencia(base, exp - 1)
     */
    public double potencia(double base, int exp) {
        if (exp == 0) {
            return 1.0;
        }
        return base * potencia(base, exp - 1);
    }

    /**
     * 1) Cuota fija por sistema frances.
     * Caso base: cuotasRestantes == 0 -> retorna 0
     * Caso recursivo: usa potencia recursiva para (1 + i)^n.
     *
     *   cuota = capital * (i * (1+i)^n) / ((1+i)^n - 1)
     */
    public double calcularCuotaFrances(double capital, double tasaMensual, int cuotasRestantes) {
        if (cuotasRestantes == 0) {
            return 0.0;
        }
        if (tasaMensual == 0) {
            return capital / cuotasRestantes;
        }
        double factor = potencia(1 + tasaMensual, cuotasRestantes);
        return capital * (tasaMensual * factor) / (factor - 1);
    }

    /**
     * 2) Saldo insoluto (lo que se debe) despues de N cuotas pagadas.
     * Caso base: cuotasPagadas == 0 -> retorna capital
     * Caso recursivo: saldo = saldoAnterior - (cuotaFija - saldoAnterior * tasaMensual)
     */
    public double calcularSaldoInsoluto(double capital, double tasaMensual,
                                        double cuotaFija, int cuotasPagadas) {
        if (cuotasPagadas == 0) {
            return capital;
        }
        double saldoAnterior = calcularSaldoInsoluto(capital, tasaMensual, cuotaFija, cuotasPagadas - 1);
        return saldoAnterior - (cuotaFija - saldoAnterior * tasaMensual);
    }

    /**
     * 3) Genera la tabla de amortizacion completa de forma recursiva.
     * Caso base: indice == cuotas -> retorna la tabla completa
     * Caso recursivo: calcula la fila 'indice' y se llama con indice + 1.
     */
    public List<FilaAmortizacion> generarTablaAmortizacion(double capital, double tasa,
                                                           int cuotas, int indice,
                                                           List<FilaAmortizacion> tabla) {
        if (indice == cuotas) {
            return tabla;
        }
        double cuota = calcularCuotaFrances(capital, tasa, cuotas);
        double saldoAnterior = calcularSaldoInsoluto(capital, tasa, cuota, indice);
        double interes = saldoAnterior * tasa;
        double abonoCapital = cuota - interes;
        double saldoRestante = saldoAnterior - abonoCapital;

        tabla.add(new FilaAmortizacion(
                indice + 1,
                redondear(cuota),
                redondear(abonoCapital),
                redondear(interes),
                redondear(Math.max(saldoRestante, 0))
        ));
        return generarTablaAmortizacion(capital, tasa, cuotas, indice + 1, tabla);
    }

    /**
     * 4) Interes compuesto: capital * (1 + tasa) aplicado mesesRestantes veces.
     * Caso base: mesesRestantes == 0 -> retorna capital
     * Caso recursivo: (1 + tasa) * calcularInteresCompuesto(..., mesesRestantes - 1)
     */
    public double calcularInteresCompuesto(double capital, double tasaMensual, int mesesRestantes) {
        if (mesesRestantes == 0) {
            return capital;
        }
        return (1 + tasaMensual) * calcularInteresCompuesto(capital, tasaMensual, mesesRestantes - 1);
    }

    /**
     * 5) Proyeccion de ahorro mes a mes acumulando deposito + interes.
     * Caso base: i == meses -> retorna saldo
     * Caso recursivo: nuevoSaldo = saldo * (1 + tasa) + deposito
     */
    public double proyectarAhorro(double saldo, double deposito, double tasa, int meses, int i) {
        if (i == meses) {
            return saldo;
        }
        double nuevoSaldo = saldo * (1 + tasa) + deposito;
        return proyectarAhorro(nuevoSaldo, deposito, tasa, meses, i + 1);
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
