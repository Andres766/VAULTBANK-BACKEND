package com.vaultbank.controller;

import com.vaultbank.dto.request.AhorroProyectadoRequest;
import com.vaultbank.dto.request.SimuladorCdtRequest;
import com.vaultbank.dto.request.SimuladorPrestamoRequest;
import com.vaultbank.dto.response.AhorroProyectadoResponse;
import com.vaultbank.dto.response.SimuladorCdtResponse;
import com.vaultbank.dto.response.SimuladorPrestamoResponse;
import com.vaultbank.service.ISimuladorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulador")
public class SimuladorController {

    private final ISimuladorService simuladorService;

    public SimuladorController(ISimuladorService simuladorService) {
        this.simuladorService = simuladorService;
    }

    @PostMapping("/prestamo")
    public ResponseEntity<SimuladorPrestamoResponse> simularPrestamo(@Valid @RequestBody SimuladorPrestamoRequest request) {
        return ResponseEntity.ok(simuladorService.simularPrestamo(request));
    }

    @PostMapping("/cdt")
    public ResponseEntity<SimuladorCdtResponse> simularCdt(@Valid @RequestBody SimuladorCdtRequest request) {
        return ResponseEntity.ok(simuladorService.simularCdt(request));
    }

    @PostMapping("/ahorro")
    public ResponseEntity<AhorroProyectadoResponse> simularAhorro(@Valid @RequestBody AhorroProyectadoRequest request) {
        return ResponseEntity.ok(simuladorService.simularAhorro(request));
    }
}
