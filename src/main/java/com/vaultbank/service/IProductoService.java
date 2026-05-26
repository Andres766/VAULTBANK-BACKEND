package com.vaultbank.service;

import com.vaultbank.calculadora.FilaAmortizacion;
import com.vaultbank.dto.request.*;
import com.vaultbank.dto.response.ProductoResponse;
import com.vaultbank.model.enums.EstadoProducto;

import java.util.List;

public interface IProductoService {

    ProductoResponse crearCuentaAhorro(CrearCuentaAhorroRequest request);

    ProductoResponse crearCuentaCorriente(CrearCuentaCorrienteRequest request);

    ProductoResponse crearCdt(CrearCdtRequest request);

    ProductoResponse crearPrestamoPersonal(CrearPrestamoPersonalRequest request);

    ProductoResponse obtenerPorId(Long id);

    List<ProductoResponse> obtenerPorCliente(Long clienteId);

    List<ProductoResponse> obtenerTodos();

    /** Cuentas activas disponibles como destino de transferencias. */
    List<ProductoResponse> obtenerCuentasActivas();

    ProductoResponse cambiarEstado(Long id, EstadoProducto estado);

    void eliminar(Long id);

    String generarExtracto(Long id);

    List<FilaAmortizacion> tablaAmortizacion(Long id);
}
