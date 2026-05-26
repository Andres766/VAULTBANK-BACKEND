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
public class ProductoResponse {
    private Long id;
    private String nombre;
    private String tipoProducto;
    private String estado;
    private BigDecimal saldo;
    private Long clienteId;
    private String clienteNombre;
    private double interesCalculado;
}
