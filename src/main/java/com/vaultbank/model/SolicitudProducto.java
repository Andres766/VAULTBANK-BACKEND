package com.vaultbank.model;

import com.vaultbank.model.productos.ProductoFinanciero;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoFinanciero producto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    private LocalDateTime fechaRespuesta;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    public enum EstadoSolicitud {
        PENDIENTE,
        APROBADA,
        RECHAZADA
    }
}
