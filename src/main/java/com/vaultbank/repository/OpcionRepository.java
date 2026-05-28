package com.vaultbank.repository;

import com.vaultbank.model.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcionRepository extends JpaRepository<Opcion, Long> {
    List<Opcion> findByOpcionPadreIsNull();
    List<Opcion> findByOpcionPadreId(Long padreId);
    List<Opcion> findByActivo(Boolean activo);
}
