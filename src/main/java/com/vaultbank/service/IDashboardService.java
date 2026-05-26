package com.vaultbank.service;

import com.vaultbank.dto.response.DashboardResponse;

public interface IDashboardService {
    DashboardResponse estadisticasGlobales();
    DashboardResponse estadisticasCliente(Long clienteId);
}
