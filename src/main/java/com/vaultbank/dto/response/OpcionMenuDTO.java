package com.vaultbank.dto.response;

import java.util.ArrayList;
import java.util.List;

public class OpcionMenuDTO {

    private Long id;
    private String nombre;
    private String ruta;
    private String icono;
    private Integer orden;
    private List<OpcionMenuDTO> hijos = new ArrayList<>();

    public OpcionMenuDTO(Long id, String nombre, String ruta, String icono, Integer orden) {
        this.id = id;
        this.nombre = nombre;
        this.ruta = ruta;
        this.icono = icono;
        this.orden = orden;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRuta() { return ruta; }
    public String getIcono() { return icono; }
    public Integer getOrden() { return orden; }
    public List<OpcionMenuDTO> getHijos() { return hijos; }
    public void setHijos(List<OpcionMenuDTO> hijos) { this.hijos = hijos; }
}
