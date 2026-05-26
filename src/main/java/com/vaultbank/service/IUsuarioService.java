package com.vaultbank.service;

import com.vaultbank.dto.response.UsuarioResponse;
import com.vaultbank.model.Usuario;

import java.util.List;

public interface IUsuarioService {
    List<UsuarioResponse> listarTodos();
    UsuarioResponse obtenerPorId(Long id);
    UsuarioResponse actualizar(Long id, String nombre, String apellido, String telefono);
    void eliminar(Long id);
    UsuarioResponse obtenerPorEmail(String email);
}
