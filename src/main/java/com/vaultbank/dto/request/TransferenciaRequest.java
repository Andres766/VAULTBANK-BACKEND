package com.vaultbank.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransferenciaRequest {

    @NotNull
    private Long productoOrigenId;

    @NotNull
    private Long productoDestinoId;

    @NotNull
    @Positive
    private Double monto;

    private String descripcion;
}
