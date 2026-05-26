package com.vaultbank.model.productos;

import com.vaultbank.model.Cliente;
import com.vaultbank.model.enums.EstadoProducto;
import com.vaultbank.model.enums.TipoProducto;
import com.vaultbank.model.interfaces.Auditable;
import com.vaultbank.model.transacciones.Transaccion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Segunda jerarquia de herencia (la central del dominio).
 * Clase abstracta: define el contrato polimorfico que cada producto
 * implementa de forma diferente. Estrategia JOINED.
 *
 * Implementa Auditable: toda operacion sobre el producto queda registrada.
 */
@Entity
@Table(name = "productos_financieros")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class ProductoFinanciero implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private LocalDate fechaApertura;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @lombok.Builder.Default
    private EstadoProducto estado = EstadoProducto.SOLICITADO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    /**
     * Encapsulamiento: el saldo no se puede asignar libremente desde fuera.
     * Solo se modifica mediante operaciones controladas/auditadas del servicio.
     */
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, precision = 18, scale = 2)
    @lombok.Builder.Default
    private BigDecimal saldo = BigDecimal.ZERO;

    @OneToMany(mappedBy = "productoOrigen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @lombok.Builder.Default
    private List<Transaccion> transacciones = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "producto_historial", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "operacion", length = 500)
    @lombok.Builder.Default
    private List<String> historialOperaciones = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (fechaApertura == null) {
            fechaApertura = LocalDate.now();
        }
    }

    // ===================== CONTRATO POLIMORFICO (abstracto) =====================

    /** Cada producto calcula su interes de forma distinta. */
    public abstract double calcularInteres();

    /** Comision por operacion segun el tipo de producto. */
    public abstract double calcularComision(double monto);

    /** Representacion textual del extracto del producto. */
    public abstract String generarExtracto();

    /** Valida si una operacion por cierto monto es permitida en este producto. */
    public abstract boolean validarOperacion(double monto);

    /** Discriminador de tipo de producto. */
    public abstract TipoProducto getTipoProducto();

    // ===================== ENCAPSULAMIENTO DEL SALDO =====================

    /**
     * Aumenta el saldo. Metodo controlado: la unica forma de incrementar saldo.
     */
    public void acreditar(BigDecimal monto) {
        if (monto == null || monto.signum() <= 0) {
            throw new IllegalArgumentException("El monto a acreditar debe ser positivo");
        }
        this.saldo = this.saldo.add(monto);
    }

    /**
     * Disminuye el saldo. Las subclases que permiten sobregiro o lo bloquean
     * deben validar antes mediante validarOperacion().
     */
    public void debitar(BigDecimal monto) {
        if (monto == null || monto.signum() <= 0) {
            throw new IllegalArgumentException("El monto a debitar debe ser positivo");
        }
        this.saldo = this.saldo.subtract(monto);
    }

    // ===================== IMPLEMENTACION DE Auditable =====================

    @Override
    public void registrarOperacion(String descripcion) {
        this.historialOperaciones.add(LocalDateTime.now() + " | " + descripcion);
    }

    @Override
    public List<String> getHistorialOperaciones() {
        return this.historialOperaciones;
    }

    @Override
    public String generarLogAuditoria() {
        return "Producto[" + getTipoProducto() + "] id=" + id
                + " operaciones registradas=" + historialOperaciones.size();
    }
}
