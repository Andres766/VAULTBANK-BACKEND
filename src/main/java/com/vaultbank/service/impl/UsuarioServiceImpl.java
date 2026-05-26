package com.vaultbank.service.impl;

import com.vaultbank.dto.response.UsuarioResponse;
import com.vaultbank.exception.RecursoNoEncontradoException;
import com.vaultbank.model.Usuario;
import com.vaultbank.repository.UsuarioRepository;
import com.vaultbank.service.IUsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public UsuarioResponse actualizar(Long id, String nombre, String apellido, String telefono) {
        Usuario u = findById(id);
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setTelefono(telefono);
        return toResponse(usuarioRepository.save(u));
    }

    @Override
    public void eliminar(Long id) {
        findById(id);
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + email));
    }

    private Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
    }

    private UsuarioResponse toResponse(Usuario u) {
        return UsuarioResponse.builder()
                .id(u.getId())
                .nombre(u.getNombre())
                .apellido(u.getApellido())
                .username(u.getUsername())
                .email(u.getEmail())
                .cedula(u.getCedula())
                .telefono(u.getTelefono())
                .rol(u.getRol().name())
                .fechaCreacion(u.getFechaCreacion())
                .build();
    }
}
