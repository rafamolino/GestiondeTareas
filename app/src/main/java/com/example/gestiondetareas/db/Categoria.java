package com.example.gestiondetareas.db;

public class Categoria {
    private String nombre;
    private String contador;

    private int porcentaje;

    // Constructor
    public Categoria(String nombre, String contador, int porcentaje) {

        this.nombre = nombre;
        this.contador = contador;
        this.porcentaje = porcentaje;
    }

    // Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContador() {
        return contador;
    }

    public void setContador(String contador) {
        this.contador = contador;
    }
    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }
}