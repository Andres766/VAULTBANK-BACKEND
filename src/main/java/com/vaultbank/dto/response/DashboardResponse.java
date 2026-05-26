package com.vaultbank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private long totalUsuarios;
    private long totalClientes;
    private long totalProductos;
    private long productosActivos;
    private long totalTransacciones;
    private BigDecimal volumenTransacciones;
    private long solicitudesPendientes;
    private long solicitudesAprobadas;
    private BigDecimal saldoTotalCuentas;
    private BigDecimal carteraPrestamos;
}
