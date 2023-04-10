package com.example.gestiondetareas.db;

public class Usuario {
    private String nombre;
    private String apellidos;

    private String correo;

    // Constructor
    public Usuario(String nombre, String apellidos,  String correo) {

        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;

    }

    // Getters y Setters

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
