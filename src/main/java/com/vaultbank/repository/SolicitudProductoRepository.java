package com.vaultbank.repository;

import com.vaultbank.model.SolicitudProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudProductoRepository extends JpaRepository<SolicitudProducto, Long> {
    List<SolicitudProducto> findByClienteId(Long clienteId);
    List<SolicitudProducto> findByEstado(SolicitudProducto.EstadoSolicitud estado);
    List<SolicitudProducto> findByProductoId(Long productoId);
}
