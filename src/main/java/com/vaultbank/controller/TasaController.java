package com.vaultbank.controller;

import com.vaultbank.dto.request.ActualizarTasaRequest;
import com.vaultbank.model.TasaInteres;
import com.vaultbank.service.ITasaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasas")
public class TasaController {

    private final ITasaService tasaService;

    public TasaController(ITasaService tasaService) {
        this.tasaService = tasaService;
    }

    @GetMapping
    public ResponseEntity<List<TasaInteres>> listar() {
        return ResponseEntity.ok(tasaService.listar());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TasaInteres> actualizar(@PathVariable Long id,
                                                   @Valid @RequestBody ActualizarTasaRequest request) {
        return ResponseEntity.ok(tasaService.actualizar(id, request));
    }
}
