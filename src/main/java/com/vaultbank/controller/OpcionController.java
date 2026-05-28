package com.vaultbank.controller;

import com.vaultbank.model.Opcion;
import com.vaultbank.repository.OpcionRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opciones")
@PreAuthorize("hasRole('ADMIN')")
public class OpcionController {

    private final OpcionRepository opcionRepository;

    public OpcionController(OpcionRepository opcionRepository) {
        this.opcionRepository = opcionRepository;
    }

    @GetMapping
    public ResponseEntity<List<Opcion>> obtenerTodas() {
        return ResponseEntity.ok(opcionRepository.findAll());
    }

    @GetMapping("/raiz")
    public ResponseEntity<List<Opcion>> obtenerOpcionesRaiz() {
        return ResponseEntity.ok(opcionRepository.findByOpcionPadreIsNull());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Opcion> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(opcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opcion no encontrada")));
    }

    @GetMapping("/{id}/hijos")
    public ResponseEntity<List<Opcion>> obtenerHijos(@PathVariable Long id) {
        return ResponseEntity.ok(opcionRepository.findByOpcionPadreId(id));
    }

    @PostMapping
    public ResponseEntity<Opcion> crear(@Valid @RequestBody Opcion opcion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(opcionRepository.save(opcion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Opcion> actualizar(@PathVariable Long id, @Valid @RequestBody Opcion opcion) {
        Opcion existente = opcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opcion no encontrada"));

        existente.setNombre(opcion.getNombre());
        existente.setRuta(opcion.getRuta());
        existente.setIcono(opcion.getIcono());
        existente.setOrden(opcion.getOrden());
        existente.setActivo(opcion.getActivo());

        return ResponseEntity.ok(opcionRepository.save(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        opcionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
