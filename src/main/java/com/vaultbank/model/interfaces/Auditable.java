package com.vaultbank.model.interfaces;

import java.util.List;

/**
 * Implementada por los productos cuyas operaciones deben quedar registradas
 * para auditoria.
 */
public interface Auditable {

    void registrarOperacion(String descripcion);

    List<String> getHistorialOperaciones();

    String generarLogAuditoria();
}
