package com.vaultbank.controller;

import com.vaultbank.calculadora.FilaAmortizacion;
import com.vaultbank.dto.request.*;
import com.vaultbank.dto.response.ProductoResponse;
import com.vaultbank.model.enums.EstadoProducto;
import com.vaultbank.service.IProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductoResponse>> listarTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<List<ProductoResponse>> porCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(productoService.obtenerPorCliente(clienteId));
    }

    @GetMapping("/cuentas-destino")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<List<ProductoResponse>> cuentasDestino() {
        return ResponseEntity.ok(productoService.obtenerCuentasActivas());
    }

    @PostMapping("/cuenta-ahorro")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<ProductoResponse> crearCuentaAhorro(@Valid @RequestBody CrearCuentaAhorroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearCuentaAhorro(request));
    }

    @PostMapping("/cuenta-corriente")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<ProductoResponse> crearCuentaCorriente(@Valid @RequestBody CrearCuentaCorrienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearCuentaCorriente(request));
    }

    @PostMapping("/cdt")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<ProductoResponse> crearCdt(@Valid @RequestBody CrearCdtRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearCdt(request));
    }

    @PostMapping("/prestamo-personal")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<ProductoResponse> crearPrestamoPersonal(@Valid @RequestBody CrearPrestamoPersonalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearPrestamoPersonal(request));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponse> cambiarEstado(@PathVariable Long id,
                                                           @RequestParam EstadoProducto estado) {
        return ResponseEntity.ok(productoService.cambiarEstado(id, estado));
    }

    @GetMapping("/{id}/extracto")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<String> extracto(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.generarExtracto(id));
    }

    @GetMapping("/{id}/tabla-amortizacion")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<List<FilaAmortizacion>> tablaAmortizacion(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.tablaAmortizacion(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
