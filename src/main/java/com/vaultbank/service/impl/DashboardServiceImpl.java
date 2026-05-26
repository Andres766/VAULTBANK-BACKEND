package com.vaultbank.service.impl;

import com.vaultbank.dto.response.DashboardResponse;
import com.vaultbank.exception.RecursoNoEncontradoException;
import com.vaultbank.model.enums.EstadoProducto;
import com.vaultbank.model.productos.CuentaAhorro;
import com.vaultbank.model.productos.CuentaCorriente;
import com.vaultbank.model.productos.PrestamoPersonal;
import com.vaultbank.repository.ClienteRepository;
import com.vaultbank.repository.ProductoFinancieroRepository;
import com.vaultbank.repository.TransaccionRepository;
import com.vaultbank.repository.UsuarioRepository;
import com.vaultbank.service.IDashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements IDashboardService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoFinancieroRepository productoRepository;
    private final TransaccionRepository transaccionRepository;

    public DashboardServiceImpl(UsuarioRepository usuarioRepository,
                                 ClienteRepository clienteRepository,
                                 ProductoFinancieroRepository productoRepository,
                                 TransaccionRepository transaccionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.transaccionRepository = transaccionRepository;
    }

    @Override
    public DashboardResponse estadisticasGlobales() {
        long totalUsuarios = usuarioRepository.count();
        long totalClientes = clienteRepository.count();
        long totalProductos = productoRepository.count();
        long productosActivos = productoRepository.countByEstado(EstadoProducto.ACTIVO);
        long totalTransacciones = transaccionRepository.count();

        BigDecimal volumen = transaccionRepository.sumMonto() != null
                ? transaccionRepository.sumMonto() : BigDecimal.ZERO;

        BigDecimal saldoCuentas = productoRepository.findAll().stream()
                .filter(p -> p instanceof CuentaAhorro || p instanceof CuentaCorriente)
                .map(p -> p.getSaldo())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal carteraPrestamos = productoRepository.findAll().stream()
                .filter(p -> p instanceof PrestamoPersonal)
                .map(p -> p.getSaldo())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return DashboardResponse.builder()
                .totalUsuarios(totalUsuarios)
                .totalClientes(totalClientes)
                .totalProductos(totalProductos)
                .productosActivos(productosActivos)
                .totalTransacciones(totalTransacciones)
                .volumenTransacciones(volumen)
                .solicitudesPendientes(0L)
                .solicitudesAprobadas(0L)
                .saldoTotalCuentas(saldoCuentas)
                .carteraPrestamos(carteraPrestamos)
                .build();
    }

    @Override
    public DashboardResponse estadisticasCliente(Long clienteId) {
        var cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + clienteId));

        long totalProductos = cliente.getProductos().size();
        long productosActivos = cliente.getProductos().stream()
                .filter(p -> p.getEstado() == EstadoProducto.ACTIVO).count();

        BigDecimal saldoCuentas = cliente.getProductos().stream()
                .filter(p -> p instanceof CuentaAhorro || p instanceof CuentaCorriente)
                .map(p -> p.getSaldo())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal carteraPrestamos = cliente.getProductos().stream()
                .filter(p -> p instanceof PrestamoPersonal)
                .map(p -> p.getSaldo())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalTransacciones = cliente.getProductos().stream()
                .mapToLong(p -> p.getTransacciones().size())
                .sum();

        return DashboardResponse.builder()
                .totalProductos(totalProductos)
                .productosActivos(productosActivos)
                .totalTransacciones(totalTransacciones)
                .saldoTotalCuentas(saldoCuentas)
                .carteraPrestamos(carteraPrestamos)
                .build();
    }
}
