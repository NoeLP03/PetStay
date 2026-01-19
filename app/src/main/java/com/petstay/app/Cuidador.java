package com.petstay.app;

public class Cuidador {
    private String nombre;
    private String ciudad;
    private String acepta;
    private String capacidad;


    public Cuidador() {}

    public Cuidador(String nombre, String ciudad, String acepta, String capacidad) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.acepta = acepta;
        this.capacidad = capacidad;
    }

    // Getters (Importantes para que el Adapter lea los datos)
    public String getNombre() { return nombre; }
    public String getCiudad() { return ciudad; }
    public String getAcepta() { return acepta; }
    public String getCapacidad() { return capacidad; }
}