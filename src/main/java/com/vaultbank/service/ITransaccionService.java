package com.vaultbank.service;

import com.vaultbank.dto.request.OperacionCuentaRequest;
import com.vaultbank.dto.request.TransferenciaRequest;
import com.vaultbank.dto.response.TransaccionResponse;

import java.util.List;

public interface ITransaccionService {

    TransaccionResponse depositar(OperacionCuentaRequest request);

    TransaccionResponse retirar(OperacionCuentaRequest request);

    TransaccionResponse transferir(TransferenciaRequest request);

    List<TransaccionResponse> movimientos(Long productoId);

    String comprobante(Long transaccionId);
}
