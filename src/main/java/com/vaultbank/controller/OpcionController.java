package com.vaultbank.controller;

import com.vaultbank.dto.response.OpcionMenuDTO;
import com.vaultbank.model.Opcion;
import com.vaultbank.model.enums.Rol;
import com.vaultbank.service.IOpcionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/opciones")
public class OpcionController {

    private final IOpcionService opcionService;

    public OpcionController(IOpcionService opcionService) {
        this.opcionService = opcionService;
    }

    @GetMapping
    public ResponseEntity<List<OpcionMenuDTO>> obtenerMenu() {
        Rol rol = getRolAutenticado();
        return ResponseEntity.ok(opcionService.obtenerMenu(rol));
    }

    private Rol getRolAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return Rol.CLIENTE;
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return esAdmin ? Rol.ADMIN : Rol.CLIENTE;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Opcion> obtenerPorId(@PathVariable Long id) {
        Opcion opcion = opcionService.obtenerOpcionPorId(id);
        if (opcion == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(opcion);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Opcion> crear(@RequestBody Opcion opcion) {
        return ResponseEntity.status(201).body(opcionService.guardarOpcion(opcion));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Opcion> actualizar(@PathVariable Long id, @RequestBody Opcion opcion) {
        Opcion existente = opcionService.obtenerOpcionPorId(id);
        if (existente == null) return ResponseEntity.notFound().build();
        existente.setNombre(opcion.getNombre());
        existente.setRuta(opcion.getRuta());
        existente.setIcono(opcion.getIcono());
        existente.setOrden(opcion.getOrden());
        existente.setActivo(opcion.getActivo());
        return ResponseEntity.ok(opcionService.guardarOpcion(existente));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        opcionService.eliminarOpcion(id);
        return ResponseEntity.noContent().build();
    }

    // Búsqueda recursiva DFS en el árbol completo de opciones
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre) {
        Opcion opcion = opcionService.buscarPorNombre(nombre);
        if (opcion == null) {
            return ResponseEntity.ok(Map.of("mensaje", "No encontrada", "nombre", nombre));
        }
        return ResponseEntity.ok(Map.of(
            "id",           opcion.getId(),
            "nombre",       opcion.getNombre(),
            "nivel",        opcion.getNivel(),
            "rutaCompleta", opcion.getRutaCompleta(),
            "ruta",         opcion.getRuta() != null ? opcion.getRuta() : ""
        ));
    }

    // Estadísticas del árbol calculadas con métodos recursivos
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> estadisticas() {
        return ResponseEntity.ok(Map.of(
            "totalOpciones",  opcionService.contarTotalOpciones(),
            "profundidadMax", opcionService.profundidadMaxima()
        ));
    }
}
