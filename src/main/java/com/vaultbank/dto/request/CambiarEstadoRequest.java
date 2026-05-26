package com.vaultbank.dto.request;

import com.vaultbank.model.enums.EstadoProducto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CambiarEstadoRequest {

    @NotNull
    private EstadoProducto estado;
}
