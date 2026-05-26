package com.vaultbank.repository;

import com.vaultbank.model.TasaInteres;
import com.vaultbank.model.enums.TipoTasa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TasaInteresRepository extends JpaRepository<TasaInteres, Long> {

    Optional<TasaInteres> findByTipoTasa(TipoTasa tipoTasa);

    boolean existsByTipoTasa(TipoTasa tipoTasa);
}
