package com.vaultbank.service.impl;

import com.vaultbank.dto.request.ActualizarTasaRequest;
import com.vaultbank.exception.RecursoNoEncontradoException;
import com.vaultbank.model.TasaInteres;
import com.vaultbank.repository.TasaInteresRepository;
import com.vaultbank.service.ITasaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TasaServiceImpl implements ITasaService {

    private final TasaInteresRepository tasaRepository;

    public TasaServiceImpl(TasaInteresRepository tasaRepository) {
        this.tasaRepository = tasaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TasaInteres> listar() {
        return tasaRepository.findAll();
    }

    @Override
    public TasaInteres actualizar(Long id, ActualizarTasaRequest req) {
        TasaInteres tasa = tasaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tasa no encontrada: " + id));
        tasa.setValor(req.getValor());
        tasa.setDescripcion(req.getDescripcion());
        tasa.setVigente(req.isVigente());
        return tasaRepository.save(tasa);
    }
}
