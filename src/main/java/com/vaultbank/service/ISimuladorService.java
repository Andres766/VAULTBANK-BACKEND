package com.vaultbank.service;

import com.vaultbank.dto.request.AhorroProyectadoRequest;
import com.vaultbank.dto.request.SimuladorCdtRequest;
import com.vaultbank.dto.request.SimuladorPrestamoRequest;
import com.vaultbank.dto.response.AhorroProyectadoResponse;
import com.vaultbank.dto.response.SimuladorCdtResponse;
import com.vaultbank.dto.response.SimuladorPrestamoResponse;

public interface ISimuladorService {

    SimuladorPrestamoResponse simularPrestamo(SimuladorPrestamoRequest request);

    SimuladorCdtResponse simularCdt(SimuladorCdtRequest request);

    AhorroProyectadoResponse simularAhorro(AhorroProyectadoRequest request);
}
