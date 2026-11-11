package com.hospital.hospitalmvcjavafx.modelo;

public class Especialidad {
    private int idEspecialidad;
    private String nombreEspecialidad;
    private String descripcionEspecialidad;

    public Especialidad() {
    }

    public Especialidad(int idEspecialidad, String nombreEspecialidad) {
        this(idEspecialidad, nombreEspecialidad, "");
    }

    public Especialidad(int idEspecialidad, String nombreEspecialidad, String descripcionEspecialidad) {
        this.idEspecialidad = idEspecialidad;
        this.nombreEspecialidad = nombreEspecialidad;
        this.descripcionEspecialidad = descripcionEspecialidad;
    }

    public int getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(int idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getNombreEspecialidad() {
        return nombreEspecialidad;
    }

    public void setNombreEspecialidad(String nombreEspecialidad) {
        this.nombreEspecialidad = nombreEspecialidad;
    }

    public String getDescripcionEspecialidad() {
        return descripcionEspecialidad;
    }

    public void setDescripcionEspecialidad(String descripcionEspecialidad) {
        this.descripcionEspecialidad = descripcionEspecialidad;
    }

    @Override
    public String toString() {
        return  idEspecialidad + " - " + nombreEspecialidad;
    }
}
