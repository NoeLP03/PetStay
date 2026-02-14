package com.petstay.app;

public class Cuidador {
    private String id; // Nuevo: ID del documento de Firebase
    private String nombre;
    private String ciudad;
    private String acepta;
    private String capacidad;

    // Constructor vacío requerido por Firebase
    public Cuidador() {}

    public Cuidador(String id, String nombre, String ciudad, String acepta, String capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.acepta = acepta;
        this.capacidad = capacidad;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getAcepta() { return acepta; }
    public void setAcepta(String acepta) { this.acepta = acepta; }

    public String getCapacidad() { return capacidad; }
    public void setCapacidad(String capacidad) { this.capacidad = capacidad; }
}