package com.vaultbank.service;

import com.vaultbank.dto.request.ActualizarTasaRequest;
import com.vaultbank.model.TasaInteres;

import java.util.List;

public interface ITasaService {

    List<TasaInteres> listar();

    TasaInteres actualizar(Long id, ActualizarTasaRequest request);
}
