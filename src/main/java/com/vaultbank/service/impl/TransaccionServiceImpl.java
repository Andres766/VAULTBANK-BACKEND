package com.vaultbank.service.impl;

import com.vaultbank.dto.request.OperacionCuentaRequest;
import com.vaultbank.dto.request.TransferenciaRequest;
import com.vaultbank.dto.response.TransaccionResponse;
import com.vaultbank.exception.OperacionInvalidaException;
import com.vaultbank.exception.RecursoNoEncontradoException;
import com.vaultbank.model.productos.CuentaAhorro;
import com.vaultbank.model.productos.ProductoFinanciero;
import com.vaultbank.model.transacciones.*;
import com.vaultbank.repository.ProductoFinancieroRepository;
import com.vaultbank.repository.TransaccionRepository;
import com.vaultbank.service.ITransaccionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class TransaccionServiceImpl implements ITransaccionService {

    private final ProductoFinancieroRepository productoRepository;
    private final TransaccionRepository transaccionRepository;

    @Value("${banco.gmf-tasa:0.004}")
    private double gmfTasa;

    public TransaccionServiceImpl(ProductoFinancieroRepository productoRepository,
                                  TransaccionRepository transaccionRepository) {
        this.productoRepository = productoRepository;
        this.transaccionRepository = transaccionRepository;
    }

    private ProductoFinanciero buscar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + id));
    }

    @Override
    public TransaccionResponse depositar(OperacionCuentaRequest req) {
        ProductoFinanciero producto = buscar(req.getProductoId());
        Deposito dep = Deposito.builder()
                .productoOrigen(producto)
                .monto(BigDecimal.valueOf(req.getMonto()))
                .descripcion(req.getDescripcion() != null ? req.getDescripcion() : "Deposito")
                .build();
        ejecutarYGuardar(dep, producto);
        return toResponse(dep);
    }

    @Override
    public TransaccionResponse retirar(OperacionCuentaRequest req) {
        ProductoFinanciero producto = buscar(req.getProductoId());
        Retiro retiro = Retiro.builder()
                .productoOrigen(producto)
                .monto(BigDecimal.valueOf(req.getMonto()))
                .descripcion(req.getDescripcion() != null ? req.getDescripcion() : "Retiro")
                .build();
        ejecutarYGuardar(retiro, producto);
        aplicarGMF(producto, req.getMonto());
        return toResponse(retiro);
    }

    @Override
    public TransaccionResponse transferir(TransferenciaRequest req) {
        if (req.getProductoOrigenId().equals(req.getProductoDestinoId())) {
            throw new OperacionInvalidaException("La cuenta origen y destino no pueden ser la misma");
        }
        ProductoFinanciero origen = buscar(req.getProductoOrigenId());
        ProductoFinanciero destino = buscar(req.getProductoDestinoId());

        Transferencia transferencia = Transferencia.builder()
                .productoOrigen(origen)
                .productoDestino(destino)
                .monto(BigDecimal.valueOf(req.getMonto()))
                .descripcion(req.getDescripcion() != null ? req.getDescripcion() : "Transferencia")
                .build();

        if (!transferencia.validar()) {
            throw new OperacionInvalidaException("Transferencia no permitida (fondos o estado destino)");
        }
        transferencia.ejecutar();
        transaccionRepository.save(transferencia);
        productoRepository.save(origen);
        productoRepository.save(destino);
        aplicarGMF(origen, req.getMonto());
        return toResponse(transferencia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransaccionResponse> movimientos(Long productoId) {
        return transaccionRepository.findByProductoOrigenId(productoId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public String comprobante(Long transaccionId) {
        Transaccion t = transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Transaccion no encontrada"));
        return t.generarComprobante();
    }

    private void ejecutarYGuardar(Transaccion transaccion, ProductoFinanciero producto) {
        if (!transaccion.validar()) {
            throw new OperacionInvalidaException("Operacion no permitida sobre el producto");
        }
        transaccion.ejecutar();
        transaccionRepository.save(transaccion);
        productoRepository.save(producto);
    }

    /** Aplica el GMF (4x1000) en retiros y transferencias. */
    private void aplicarGMF(ProductoFinanciero producto, double monto) {
        if (producto instanceof CuentaAhorro ca && ca.isExentoGMF()) {
            return;
        }
        double gmf = monto * gmfTasa;
        if (gmf > 0 && producto.getSaldo().doubleValue() >= gmf) {
            producto.debitar(BigDecimal.valueOf(gmf));
            producto.registrarOperacion("GMF (4x1000) por $" + String.format("%.2f", gmf));
            productoRepository.save(producto);
        }
    }

    private TransaccionResponse toResponse(Transaccion t) {
        return TransaccionResponse.builder()
                .id(t.getId())
                .tipo(t.getTipo().name())
                .monto(t.getMonto())
                .descripcion(t.getDescripcion())
                .fecha(t.getFecha())
                .productoOrigenId(t.getProductoOrigen() != null ? t.getProductoOrigen().getId() : null)
                .comprobante(t.generarComprobante())
                .build();
    }
}
