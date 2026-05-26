package com.vaultbank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionResponse {
    private Long id;
    private String tipo;
    private BigDecimal monto;
    private String descripcion;
    private LocalDateTime fecha;
    private Long productoOrigenId;
    private String comprobante;
}
