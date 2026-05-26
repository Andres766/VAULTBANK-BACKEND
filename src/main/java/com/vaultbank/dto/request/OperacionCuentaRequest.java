package com.vaultbank.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/** Request para deposito, retiro, pago de cuota y abono extraordinario. */
@Data
public class OperacionCuentaRequest {

    @NotNull
    private Long productoId;

    @NotNull
    @Positive
    private Double monto;

    private String descripcion;
}
