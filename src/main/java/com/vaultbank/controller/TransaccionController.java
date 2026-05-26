package com.vaultbank.controller;

import com.vaultbank.dto.request.OperacionCuentaRequest;
import com.vaultbank.dto.request.TransferenciaRequest;
import com.vaultbank.dto.response.TransaccionResponse;
import com.vaultbank.service.ITransaccionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final ITransaccionService transaccionService;

    public TransaccionController(ITransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping("/depositar")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<TransaccionResponse> depositar(@Valid @RequestBody OperacionCuentaRequest request) {
        return ResponseEntity.ok(transaccionService.depositar(request));
    }

    @PostMapping("/retirar")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<TransaccionResponse> retirar(@Valid @RequestBody OperacionCuentaRequest request) {
        return ResponseEntity.ok(transaccionService.retirar(request));
    }

    @PostMapping("/transferir")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<TransaccionResponse> transferir(@Valid @RequestBody TransferenciaRequest request) {
        return ResponseEntity.ok(transaccionService.transferir(request));
    }

    @GetMapping("/producto/{productoId}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<List<TransaccionResponse>> movimientos(@PathVariable Long productoId) {
        return ResponseEntity.ok(transaccionService.movimientos(productoId));
    }

    @GetMapping("/{id}/comprobante")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<String> comprobante(@PathVariable Long id) {
        return ResponseEntity.ok(transaccionService.comprobante(id));
    }
}
