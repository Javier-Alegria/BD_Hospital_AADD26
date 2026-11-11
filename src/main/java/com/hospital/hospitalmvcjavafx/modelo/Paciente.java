package com.hospital.hospitalmvcjavafx.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paciente {
    private int idPaciente;
    private String nombrePaciente;
    private String dniPaciente;
    private LocalDate fechaNacimiento;
    private List<Medico> medicosList;

    /**
     * Constructor vacío. Inicializa la lista de médicos como lista vacía.
     */
    public Paciente() {
        this.medicosList = new ArrayList<>();
    }

    /**
     * Crea un paciente con sus datos básicos y sin médicos asignados.
     *
     * @param idPaciente identificador del paciente
     * @param nombrePaciente nombre del paciente
     * @param dniPaciente DNI del paciente
     * @param fechaNacimiento fecha de nacimiento del paciente
     */
    public Paciente(int idPaciente, String nombrePaciente, String dniPaciente, LocalDate fechaNacimiento) {
        this.idPaciente = idPaciente;
        this.nombrePaciente = nombrePaciente;
        this.dniPaciente = dniPaciente;
        this.fechaNacimiento = fechaNacimiento;
        this.medicosList = new ArrayList<>();
    }

    /**
     * Crea un paciente con todos sus datos. Si la lista de médicos es null,
     * se inicializa como lista vacía.
     *
     * @param idPaciente identificador del paciente
     * @param nombrePaciente nombre del paciente
     * @param dniPaciente DNI del paciente
     * @param fechaNacimiento fecha de nacimiento del paciente
     * @param medicosList médicos asignados al paciente
     */
    public Paciente(int idPaciente, String nombrePaciente, String dniPaciente, LocalDate fechaNacimiento, List<Medico> medicosList) {
        this.idPaciente = idPaciente;
        this.nombrePaciente = nombrePaciente;
        this.dniPaciente = dniPaciente;
        this.fechaNacimiento = fechaNacimiento;
        this.medicosList = medicosList != null ? medicosList : new ArrayList<>();
    }

    // Métodos para gestionar los Médicos de un paciente

    /**
     * Añade un médico a la lista del paciente si no es null y no está ya en la lista.
     *
     * @param medico médico a añadir
     */
    public void agregarMedico(Medico medico) {
        if ( medico != null && !this.medicosList.contains(medico) ) {
            this.medicosList.add(medico);
        }
    }

    /**
     * Quita un médico de la lista del paciente.
     *
     * @param medico médico a quitar
     */
    public void eliminarMedico(Medico medico) {
        this.medicosList.remove(medico);
    }

    // --- GETTER & SETTER ---

    public int getIdPaciente() {
        return idPaciente;
    }
    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }
    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getDniPaciente() {
        return dniPaciente;
    }
    public void setDniPaciente(String dniPaciente) {
        this.dniPaciente = dniPaciente;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public List<Medico> getMedicosList() {
        return medicosList;
    }
    public void setMedicosList(List<Medico> medicosList) {
        this.medicosList = medicosList != null ? medicosList : new ArrayList<>();
    }

    /**
     * Devuelve el paciente como texto con sus datos (sin la lista de médicos).
     *
     * @return representación en texto del paciente
     */
    @Override
    public String toString() {
        return "Paciente{" +
                "idPaciente=" + idPaciente +
                ", nombrePaciente='" + nombrePaciente + '\'' +
                ", dniPaciente='" + dniPaciente + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                '}';
    }
}
