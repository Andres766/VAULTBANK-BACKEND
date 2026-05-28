package com.vaultbank.service;

import com.vaultbank.dto.response.OpcionMenuDTO;
import com.vaultbank.model.Opcion;
import com.vaultbank.model.enums.Rol;

import java.util.List;

public interface IOpcionService {
    List<OpcionMenuDTO> obtenerMenu(Rol rol);
    Opcion obtenerOpcionPorId(Long id);
    Opcion guardarOpcion(Opcion opcion);
    void eliminarOpcion(Long id);
    Opcion buscarPorNombre(String nombre);
    int contarTotalOpciones();
    int profundidadMaxima();
}
