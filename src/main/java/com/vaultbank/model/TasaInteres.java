package com.vaultbank.model;

import com.vaultbank.model.enums.TipoTasa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasas_interes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TasaInteres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private TipoTasa tipoTasa;

    /** Valor de la tasa en formato decimal (ej: 0.05 = 5%). */
    @Column(nullable = false)
    private double valor;

    private String descripcion;

    @Builder.Default
    private boolean vigente = true;
}
