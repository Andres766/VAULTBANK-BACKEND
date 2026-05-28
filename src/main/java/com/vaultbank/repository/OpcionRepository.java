package com.vaultbank.repository;

import com.vaultbank.model.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpcionRepository extends JpaRepository<Opcion, Long> {
    List<Opcion> findByOpcionPadreIsNull();
    List<Opcion> findByOpcionPadreIsNullAndActivoTrueOrderByOrden();
    List<Opcion> findByOpcionPadreId(Long padreId);
    List<Opcion> findByActivo(Boolean activo);
    Optional<Opcion> findByNombre(String nombre);
}
