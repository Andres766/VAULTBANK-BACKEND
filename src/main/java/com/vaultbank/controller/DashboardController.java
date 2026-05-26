package com.vaultbank.controller;

import com.vaultbank.dto.response.DashboardResponse;
import com.vaultbank.service.IDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final IDashboardService dashboardService;

    public DashboardController(IDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardResponse> estadisticasGlobales() {
        return ResponseEntity.ok(dashboardService.estadisticasGlobales());
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<DashboardResponse> estadisticasCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(dashboardService.estadisticasCliente(clienteId));
    }
}
