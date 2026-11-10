package com.hospital.hospitalmvcjavafx.modelo;

public class Medico {
    private int idMedico;
    private String nombreMedico;
    private int numeroColegiado;
    private Especialidad especialidad;

    private String especialidadNombre;

    public Medico() {
    }

    public Medico(int idMedico, String nombreMedico, int numeroColegiado, Especialidad especialidad) {
        this.idMedico = idMedico;
        this.nombreMedico = nombreMedico;
        this.numeroColegiado = numeroColegiado;
        this.especialidad = especialidad;
        this.especialidadNombre = especialidad.getNombreEspecialidad();
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public int getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(int numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
        this.especialidadNombre = (especialidad != null) ? especialidad.getNombreEspecialidad() : "";
    }

    public String getEspecialidadNombre() {
        return especialidadNombre;
    }

    @Override
    public String toString() {
        return "Medico{" +
                "idMedico=" + idMedico +
                ", nombre='" + nombreMedico + '\'' +
                ", numeroColegiado=" + numeroColegiado +
                ", especialidad=" + especialidad +
                '}';
    }
}
