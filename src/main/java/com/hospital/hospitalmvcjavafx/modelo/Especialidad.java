package com.hospital.hospitalmvcjavafx.modelo;

public class Especialidad {
    private int idEspecialidad;
    private String nombreEspecialidad;
    private String descripcionEspecialidad;

    /**
     * Constructor vacío.
     */
    public Especialidad() {
    }

    /**
     * Crea una especialidad con id y nombre; la descripción queda vacía.
     *
     * @param idEspecialidad identificador de la especialidad
     * @param nombreEspecialidad nombre de la especialidad
     */
    public Especialidad(int idEspecialidad, String nombreEspecialidad) {
        this(idEspecialidad, nombreEspecialidad, "");
    }

    /**
     * Crea una especialidad con todos sus datos.
     *
     * @param idEspecialidad identificador de la especialidad
     * @param nombreEspecialidad nombre de la especialidad
     * @param descripcionEspecialidad descripción de la especialidad
     */
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

    /**
     * Devuelve la especialidad como texto con el formato "id - nombre".
     *
     * @return representación en texto de la especialidad
     */
    @Override
    public String toString() {
        return  idEspecialidad + " - " + nombreEspecialidad;
    }
}
