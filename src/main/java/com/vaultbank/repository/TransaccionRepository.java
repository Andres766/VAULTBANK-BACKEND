package com.vaultbank.repository;

import com.vaultbank.model.transacciones.Transaccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    List<Transaccion> findByProductoOrigenId(Long productoId);

    Page<Transaccion> findByProductoOrigenId(Long productoId, Pageable pageable);

    List<Transaccion> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);

    long countByFechaBetween(LocalDateTime desde, LocalDateTime hasta);

    @Query("SELECT COALESCE(SUM(t.monto), 0) FROM Transaccion t")
    BigDecimal sumMonto();
}
