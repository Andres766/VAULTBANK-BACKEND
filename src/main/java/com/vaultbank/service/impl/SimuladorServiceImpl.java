package com.vaultbank.service.impl;

import com.vaultbank.calculadora.CalculadoraFinanciera;
import com.vaultbank.calculadora.FilaAmortizacion;
import com.vaultbank.dto.request.AhorroProyectadoRequest;
import com.vaultbank.dto.request.SimuladorCdtRequest;
import com.vaultbank.dto.request.SimuladorPrestamoRequest;
import com.vaultbank.dto.response.AhorroProyectadoResponse;
import com.vaultbank.dto.response.SimuladorCdtResponse;
import com.vaultbank.dto.response.SimuladorPrestamoResponse;
import com.vaultbank.service.ISimuladorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimuladorServiceImpl implements ISimuladorService {

    private final CalculadoraFinanciera calculadora;

    public SimuladorServiceImpl(CalculadoraFinanciera calculadora) {
        this.calculadora = calculadora;
    }

    @Override
    public SimuladorPrestamoResponse simularPrestamo(SimuladorPrestamoRequest request) {
        double tasaMensual = request.getTasaMensual() != null
                ? request.getTasaMensual() : 0.02;

        double cuotaMensual = calculadora.calcularCuotaFrances(
                request.getMonto(), tasaMensual, request.getPlazoMeses());

        List<FilaAmortizacion> tabla = calculadora.generarTablaAmortizacion(
                request.getMonto(), tasaMensual, request.getPlazoMeses(), 0, new ArrayList<>());

        double totalIntereses = tabla.stream().mapToDouble(FilaAmortizacion::getAbonoInteres).sum();
        double totalPagar = cuotaMensual * request.getPlazoMeses();

        return SimuladorPrestamoResponse.builder()
                .cuotaMensual(redondear(cuotaMensual))
                .totalIntereses(redondear(totalIntereses))
                .totalPagar(redondear(totalPagar))
                .monto(request.getMonto())
                .plazoMeses(request.getPlazoMeses())
                .tablaAmortizacion(tabla)
                .build();
    }

    @Override
    public SimuladorCdtResponse simularCdt(SimuladorCdtRequest request) {
        double tasaMensual = Math.pow(1 + request.getTasaEfectivaAnual(), 1.0 / 12.0) - 1;

        double valorVencimiento = calculadora.calcularInteresCompuesto(
                request.getCapital(), tasaMensual, request.getPlazoMeses());
        double intereses = valorVencimiento - request.getCapital();

        List<Double> crecimiento = new ArrayList<>();
        for (int m = 1; m <= request.getPlazoMeses(); m++) {
            crecimiento.add(redondear(
                    calculadora.calcularInteresCompuesto(request.getCapital(), tasaMensual, m)));
        }

        return SimuladorCdtResponse.builder()
                .capital(request.getCapital())
                .plazoMeses(request.getPlazoMeses())
                .valorVencimiento(redondear(valorVencimiento))
                .interesesGanados(redondear(intereses))
                .rentabilidadEfectiva(redondear(intereses / request.getCapital() * 100))
                .crecimientoMensual(crecimiento)
                .build();
    }

    @Override
    public AhorroProyectadoResponse simularAhorro(AhorroProyectadoRequest request) {
        double saldoFinal = calculadora.proyectarAhorro(
                request.getSaldoInicial(), request.getDepositoMensual(),
                request.getTasaMensual(), request.getMeses(), 0);

        double totalDepositado = request.getSaldoInicial()
                + request.getDepositoMensual() * request.getMeses();

        List<Double> saldoMensual = new ArrayList<>();
        for (int m = 1; m <= request.getMeses(); m++) {
            saldoMensual.add(redondear(calculadora.proyectarAhorro(
                    request.getSaldoInicial(), request.getDepositoMensual(),
                    request.getTasaMensual(), m, 0)));
        }

        return AhorroProyectadoResponse.builder()
                .saldoFinal(redondear(saldoFinal))
                .totalDepositado(redondear(totalDepositado))
                .interesesGanados(redondear(saldoFinal - totalDepositado))
                .saldoMensual(saldoMensual)
                .build();
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
