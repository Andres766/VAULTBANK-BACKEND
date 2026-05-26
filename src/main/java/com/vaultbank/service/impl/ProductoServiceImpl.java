package com.vaultbank.service.impl;

import com.vaultbank.calculadora.CalculadoraFinanciera;
import com.vaultbank.calculadora.FilaAmortizacion;
import com.vaultbank.dto.request.*;
import com.vaultbank.dto.response.ProductoResponse;
import com.vaultbank.exception.OperacionInvalidaException;
import com.vaultbank.exception.RecursoNoEncontradoException;
import com.vaultbank.model.Cliente;
import com.vaultbank.model.enums.EstadoProducto;
import com.vaultbank.model.enums.TipoProducto;
import com.vaultbank.model.productos.*;
import com.vaultbank.repository.ClienteRepository;
import com.vaultbank.repository.ProductoFinancieroRepository;
import com.vaultbank.service.IProductoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductoServiceImpl implements IProductoService {

    private final ProductoFinancieroRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final CalculadoraFinanciera calculadora;

    public ProductoServiceImpl(ProductoFinancieroRepository productoRepository,
                               ClienteRepository clienteRepository,
                               CalculadoraFinanciera calculadora) {
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
        this.calculadora = calculadora;
    }

    private Cliente buscarCliente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + clienteId));
    }

    /**
     * El admin crea productos ya activos; un cliente solo los solicita,
     * por lo que quedan en estado SOLICITADO a la espera de aprobacion.
     */
    private EstadoProducto estadoInicial() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean esAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return esAdmin ? EstadoProducto.ACTIVO : EstadoProducto.SOLICITADO;
    }

    @Override
    public ProductoResponse crearCuentaAhorro(CrearCuentaAhorroRequest req) {
        CuentaAhorro cuenta = CuentaAhorro.builder()
                .nombre(req.getNombre())
                .cliente(buscarCliente(req.getClienteId()))
                .estado(estadoInicial())
                .tasaInteresAnual(req.getTasaInteresAnual())
                .limiteRetirosDiarios(req.getLimiteRetirosDiarios())
                .exentoGMF(req.isExentoGMF())
                .build();
        if (req.getSaldoInicial() > 0) {
            cuenta.acreditar(BigDecimal.valueOf(req.getSaldoInicial()));
        }
        return toResponse(productoRepository.save(cuenta));
    }

    @Override
    public ProductoResponse crearCuentaCorriente(CrearCuentaCorrienteRequest req) {
        CuentaCorriente cuenta = CuentaCorriente.builder()
                .nombre(req.getNombre())
                .cliente(buscarCliente(req.getClienteId()))
                .estado(estadoInicial())
                .cupoSobregiro(req.getCupoSobregiro())
                .comisionMantenimiento(req.getComisionMantenimiento())
                .chequera(req.isChequera())
                .build();
        if (req.getSaldoInicial() > 0) {
            cuenta.acreditar(BigDecimal.valueOf(req.getSaldoInicial()));
        }
        return toResponse(productoRepository.save(cuenta));
    }

    @Override
    public ProductoResponse crearCdt(CrearCdtRequest req) {
        CDT cdt = CDT.builder()
                .nombre(req.getNombre())
                .cliente(buscarCliente(req.getClienteId()))
                .estado(estadoInicial())
                .capitalInicial(req.getCapitalInicial())
                .plazoMeses(req.getPlazoMeses())
                .tasaEfectivaAnual(req.getTasaEfectivaAnual())
                .fechaVencimiento(LocalDate.now().plusMonths(req.getPlazoMeses()))
                .build();
        cdt.acreditar(BigDecimal.valueOf(req.getCapitalInicial()));
        return toResponse(productoRepository.save(cdt));
    }

    @Override
    public ProductoResponse crearPrestamoPersonal(CrearPrestamoPersonalRequest req) {
        PrestamoPersonal prestamo = PrestamoPersonal.builder()
                .nombre(req.getNombre())
                .cliente(buscarCliente(req.getClienteId()))
                .estado(estadoInicial())
                .montoAprobado(req.getMontoAprobado())
                .tasaMensual(req.getTasaMensual())
                .numeroCuotas(req.getNumeroCuotas())
                .diaCorte(req.getDiaCorte())
                .build();
        // El saldo de un prestamo representa la deuda (saldo insoluto inicial).
        prestamo.acreditar(BigDecimal.valueOf(req.getMontoAprobado()));
        return toResponse(productoRepository.save(prestamo));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return toResponse(buscarProducto(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> obtenerPorCliente(Long clienteId) {
        return productoRepository.findByClienteId(clienteId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> obtenerTodos() {
        return productoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> obtenerCuentasActivas() {
        return productoRepository.findAll().stream()
                .filter(p -> p instanceof CuentaAhorro || p instanceof CuentaCorriente)
                .filter(p -> p.getEstado() == EstadoProducto.ACTIVO)
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductoResponse cambiarEstado(Long id, EstadoProducto estado) {
        ProductoFinanciero producto = buscarProducto(id);
        producto.setEstado(estado);
        producto.registrarOperacion("Cambio de estado a " + estado);
        return toResponse(productoRepository.save(producto));
    }

    @Override
    public void eliminar(Long id) {
        ProductoFinanciero producto = buscarProducto(id);
        productoRepository.delete(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public String generarExtracto(Long id) {
        // Invocacion polimorfica: cada producto genera su extracto distinto.
        return buscarProducto(id).generarExtracto();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilaAmortizacion> tablaAmortizacion(Long id) {
        ProductoFinanciero producto = buscarProducto(id);
        double monto;
        double tasa;
        int cuotas;
        if (producto instanceof PrestamoPersonal p) {
            monto = p.getMontoAprobado();
            tasa = p.getTasaMensual();
            cuotas = p.getNumeroCuotas();
        } else {
            throw new OperacionInvalidaException("El producto no tiene tabla de amortizacion");
        }
        return calculadora.generarTablaAmortizacion(monto, tasa, cuotas, 0, new ArrayList<>());
    }

    private ProductoFinanciero buscarProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + id));
    }

    private ProductoResponse toResponse(ProductoFinanciero p) {
        Cliente cliente = p.getCliente();
        return ProductoResponse.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .tipoProducto(p.getTipoProducto().name())
                .estado(p.getEstado().name())
                .saldo(p.getSaldo())
                .clienteId(cliente != null ? cliente.getId() : null)
                .clienteNombre(cliente != null ? cliente.getNombreCompleto() : null)
                .interesCalculado(redondear(p.calcularInteres()))
                .build();
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
