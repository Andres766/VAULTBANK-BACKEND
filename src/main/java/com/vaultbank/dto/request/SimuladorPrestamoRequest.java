package com.vaultbank.dto.request;

import com.vaultbank.model.enums.TipoProducto;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SimuladorPrestamoRequest {

    @Positive
    private double monto;

    @Positive
    private int plazoMeses;

    /** Tasa mensual en decimal (ej: 0.02). Si no se envia se toma de la tabla de tasas. */
    private Double tasaMensual;

    /** PRESTAMO_PERSONAL o PRESTAMO_HIPOTECARIO. */
    private TipoProducto tipoProducto = TipoProducto.PRESTAMO_PERSONAL;

    /** Seguro de vida mensual (solo hipotecario). */
    private double seguroVida = 0.0;
}
