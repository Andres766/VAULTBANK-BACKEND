package com.vaultbank.repository;

import com.vaultbank.model.enums.EstadoProducto;
import com.vaultbank.model.productos.ProductoFinanciero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoFinancieroRepository extends JpaRepository<ProductoFinanciero, Long> {

    List<ProductoFinanciero> findByClienteId(Long clienteId);

    List<ProductoFinanciero> findByEstado(EstadoProducto estado);

    long countByEstado(EstadoProducto estado);
}
