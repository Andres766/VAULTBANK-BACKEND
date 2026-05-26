package com.vaultbank.model;

import com.vaultbank.model.enums.PerfilRiesgo;
import com.vaultbank.model.productos.ProductoFinanciero;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Cliente extends Usuario {

    @Column(precision = 15, scale = 2)
    private BigDecimal ingresosMensuales;

    private String ocupacion;

    @Enumerated(EnumType.STRING)
    private PerfilRiesgo perfilRiesgo;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @lombok.Builder.Default
    private List<ProductoFinanciero> productos = new ArrayList<>();
}
