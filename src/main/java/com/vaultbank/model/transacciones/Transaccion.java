package com.vaultbank.model.transacciones;

import com.vaultbank.model.enums.TipoTransaccion;
import com.vaultbank.model.productos.ProductoFinanciero;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Tercera jerarquia de herencia. Clase abstracta.
 * Estrategia SINGLE_TABLE con discriminador: todas las transacciones viven
 * en una sola tabla diferenciadas por la columna tipo_transaccion.
 */
@Entity
@Table(name = "transacciones")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_transaccion", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal monto;

    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_origen_id")
    private ProductoFinanciero productoOrigen;

    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }

    // ===================== CONTRATO POLIMORFICO =====================

    /** Aplica el efecto de la transaccion sobre el/los productos involucrados. */
    public abstract void ejecutar();

    /** Comprobante textual de la operacion. */
    public abstract String generarComprobante();

    /** Valida si la transaccion puede ejecutarse. */
    public abstract boolean validar();

    /** Tipo concreto de transaccion. */
    public abstract TipoTransaccion getTipo();
}
