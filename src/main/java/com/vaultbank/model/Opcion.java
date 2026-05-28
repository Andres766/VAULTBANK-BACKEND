package com.vaultbank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "opcion_padre_id")
    @JsonIgnore
    private Opcion opcionPadre;

    @OneToMany(mappedBy = "opcionPadre", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Opcion> hijos = new ArrayList<>();

    @Column(columnDefinition = "boolean default true")
    private Boolean activo = true;

    // Recursivo: nivel de profundidad (raíz = 0)
    public int getNivel() {
        if (opcionPadre == null) return 0;
        return 1 + opcionPadre.getNivel();
    }

    // Recursivo: ruta completa desde raíz hasta este nodo
    public String getRutaCompleta() {
        if (opcionPadre == null) return nombre;
        return opcionPadre.getRutaCompleta() + " > " + nombre;
    }

    // Recursivo: cuenta nodos activos en el subárbol
    public int contarNodosActivos() {
        if (hijos == null || hijos.isEmpty()) return 1;
        int total = 1;
        for (Opcion hijo : hijos) {
            if (Boolean.TRUE.equals(hijo.getActivo())) {
                total += hijo.contarNodosActivos();
            }
        }
        return total;
    }
}
