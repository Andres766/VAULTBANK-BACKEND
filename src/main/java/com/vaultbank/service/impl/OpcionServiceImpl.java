package com.vaultbank.service.impl;

import com.vaultbank.dto.response.OpcionMenuDTO;
import com.vaultbank.model.Opcion;
import com.vaultbank.model.enums.Rol;
import com.vaultbank.repository.OpcionRepository;
import com.vaultbank.service.IOpcionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpcionServiceImpl implements IOpcionService {

    private final OpcionRepository opcionRepository;

    public OpcionServiceImpl(OpcionRepository opcionRepository) {
        this.opcionRepository = opcionRepository;
    }

    @Override
    public List<OpcionMenuDTO> obtenerMenu(Rol rol) {
        if (rol == null) throw new IllegalArgumentException("El rol es requerido");
        List<OpcionMenuDTO> arbol = construirArbol();
        return filtrarPorRol(arbol, rol);
    }

    private List<OpcionMenuDTO> construirArbol() {
        List<Opcion> opciones = opcionRepository.findAll();

        Map<Long, OpcionMenuDTO> map = new HashMap<>();
        for (Opcion o : opciones) {
            map.put(o.getId(), new OpcionMenuDTO(o.getId(), o.getNombre(), o.getRuta(), o.getIcono(), o.getOrden()));
        }

        List<OpcionMenuDTO> roots = new ArrayList<>();
        for (Opcion o : opciones) {
            OpcionMenuDTO dto = map.get(o.getId());
            if (o.getOpcionPadre() == null) {
                roots.add(dto);
            } else {
                OpcionMenuDTO parent = map.get(o.getOpcionPadre().getId());
                if (parent != null) parent.getHijos().add(dto);
            }
        }
        return roots;
    }

    // Recursivo: filtra el árbol según el rol del usuario
    // Caso base: lista vacía → retorna lista vacía
    // Caso recursivo: copia el item y filtra recursivamente sus hijos
    private List<OpcionMenuDTO> filtrarPorRol(List<OpcionMenuDTO> items, Rol rol) {
        List<OpcionMenuDTO> result = new ArrayList<>();
        for (OpcionMenuDTO item : items) {
            if (!esVisible(item, rol)) continue;

            OpcionMenuDTO copy = new OpcionMenuDTO(item.getId(), item.getNombre(), item.getRuta(), item.getIcono(), item.getOrden());
            copy.setHijos(filtrarPorRol(item.getHijos(), rol));

            if (copy.getRuta() == null && copy.getHijos().isEmpty()) continue;
            result.add(copy);
        }
        return result;
    }

    private boolean esVisible(OpcionMenuDTO item, Rol rol) {
        if (rol == Rol.ADMIN) return true;

        String ruta = item.getRuta();
        if (ruta == null) return true;

        // CLIENTE no ve rutas de administración
        if (ruta.startsWith("/admin")) return false;

        return true;
    }

    @Override
    public Opcion obtenerOpcionPorId(Long id) {
        return opcionRepository.findById(id).orElse(null);
    }

    @Override
    public Opcion guardarOpcion(Opcion opcion) {
        return opcionRepository.save(opcion);
    }

    @Override
    public void eliminarOpcion(Long id) {
        opcionRepository.deleteById(id);
    }

    @Override
    public Opcion buscarPorNombre(String nombre) {
        return opcionRepository.findByNombre(nombre).orElse(null);
    }

    @Override
    public int contarTotalOpciones() {
        return (int) opcionRepository.count();
    }

    @Override
    public int profundidadMaxima() {
        return opcionRepository.findAll().stream()
                .mapToInt(o -> o.getNivel())
                .max()
                .orElse(0);
    }
}
