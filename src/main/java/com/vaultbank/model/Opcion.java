package com.vaultbank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "opciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Opcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String ruta;

    private String icono;

    private Integer orden;

    // AUTO-REFERENCIA: apunta a otro registro de la misma tabla
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opcion_padre_id")
    private Opcion opcionPadre;

    // LISTA DE HIJOS: recursividad
    @OneToMany(mappedBy = "opcionPadre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Opcion> hijos = new ArrayList<>();

    @Column(columnDefinition = "boolean default true")
    private Boolean activo = true;
}
