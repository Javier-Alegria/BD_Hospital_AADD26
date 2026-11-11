package com.hospital.hospitalmvcjavafx.modelo;

public class Medico {
    private int idMedico;
    private String nombreMedico;
    private int numeroColegiado;
    private Especialidad especialidad;

    private String especialidadNombre;

    /**
     * Constructor vacío.
     */
    public Medico() {
    }

    /**
     * Crea un médico con todos sus datos y guarda también el nombre de su especialidad.
     *
     * @param idMedico identificador del médico
     * @param nombreMedico nombre del médico
     * @param numeroColegiado número de colegiado del médico
     * @param especialidad especialidad del médico
     */
    public Medico(int idMedico, String nombreMedico, int numeroColegiado, Especialidad especialidad) {
        this.idMedico = idMedico;
        this.nombreMedico = nombreMedico;
        this.numeroColegiado = numeroColegiado;

        /* Se usa el setter en vez de asignar directamente porque además de guardar la
           especialidad calcula especialidadNombre y controla el caso null (evita NullPointerException) */
        setEspecialidad(especialidad);
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

    /**
     * Devuelve el médico como texto con todos sus datos.
     *
     * @return representación en texto del médico
     */
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
