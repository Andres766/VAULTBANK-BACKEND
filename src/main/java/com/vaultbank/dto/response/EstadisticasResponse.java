package com.vaultbank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasResponse {
    private long totalUsuarios;
    private long totalClientes;
    private long totalProductos;
    private long productosActivos;
    private long productosEnMora;
    private long operacionesHoy;
    private double totalDepositosHoy;
}
