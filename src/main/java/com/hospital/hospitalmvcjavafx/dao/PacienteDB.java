package com.hospital.hospitalmvcjavafx.dao;

import com.hospital.hospitalmvcjavafx.modelo.Especialidad;
import com.hospital.hospitalmvcjavafx.modelo.Medico;
import com.hospital.hospitalmvcjavafx.modelo.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.hospital.hospitalmvcjavafx.Utilidades.mostrarError;
import static com.hospital.hospitalmvcjavafx.config.Conexion.conectar;
import static com.hospital.hospitalmvcjavafx.config.Conexion.desconectar;

public class PacienteDB {
    public static List<Paciente> consultarPacientes() {
        List<Paciente> pacientesList = new ArrayList<>();
        String sql = "SELECT id_paciente, nombre_paciente, dni_paciente, fecna_paciente FROM pacientes";

        try(Connection conexion = conectar();
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();) {

            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setIdPaciente(rs.getInt("id_paciente"));
                paciente.setNombrePaciente(rs.getString("nombre_paciente"));
                paciente.setDniPaciente(rs.getString("dni_paciente"));
                paciente.setFechaNacimiento(LocalDate.parse(rs.getString("fecna_paciente")));

                // Recuperar y asignar los médicos que contiene este paciente
                paciente.setMedicosList(consultarMedicosPorPaciente(paciente.getIdPaciente(), conexion));

                pacientesList.add(paciente);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pacientesList;
    }

    public static int insertarPaciente(Paciente paciente) {
        int resultado = 0;
        String sql = "INSERT INTO pacientes (id_paciente, nombre_paciente, dni_paciente, fecna_paciente) VALUES (0, ?, ?, ?)";
        Connection conexion = null;

        try {
            conexion = conectar();

            conexion.setAutoCommit(false);

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, paciente.getNombrePaciente());
                ps.setString(2, paciente.getDniPaciente());
                ps.setObject(3, paciente.getFechaNacimiento());

                resultado = ps.executeUpdate();
            }

            // Recuperamos el ID del paciente que la BD acaba de generar con la misma conexión
            paciente.setIdPaciente(obtenerIdPorDni(paciente.getDniPaciente(), conexion));

            actualizarGestiones(paciente.getIdPaciente(), paciente.getMedicosList(), conexion);

            conexion.commit();

        } catch (SQLException e) {
            mostrarError("Error de transacción", "Error en la transacción, aplicando rollback : " + e.getMessage());
            if (conexion != null) {
                try {
                    // Deshacemos cualquier inserción o borrado parcial si algo falló
                    conexion.rollback();
                } catch (SQLException ex) {
                    mostrarError("Error de rollback","Error al aplicar el rollback: " + ex.getMessage());
                }
            }
        } finally {
            desconectar();
        }
        /*
        int idGenerado = 0;
        try (Connection conexion = conectar();
            PreparedStatement ps = conexion.prepareStatement(sql)){

            ps.setString(1, paciente.getNombrePaciente());
            ps.setString(2, paciente.getDniPaciente());
            ps.setObject(3, paciente.getFechaNacimiento());

            resultado = ps.executeUpdate();

            actualizarGestiones(paciente.getIdPaciente(), paciente.getMedicosList(), conexion);

        } catch (SQLException e) {
            mostrarError("Error de inserción","Error al insertar el paciente: " + e.getMessage());
        }
        */

        return resultado;
    }

    private static int obtenerIdPorDni(String dniPaciente, Connection conexion) throws SQLException {
        String sql = "SELECT id_paciente FROM pacientes WHERE dni_paciente = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, dniPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_paciente");
                }
            }
        }
        return 0;
    }

    private static List<Medico> consultarMedicosPorPaciente(int idPaciente, Connection conexion) throws SQLException {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT m.id_medico, m.nombre_medico, m.especialidad_id, e.nombre_especialidad FROM gestiones gest INNER JOIN medicos m ON gest.medico_id = m.id_medico INNER JOIN especialidades e ON m.especialidad_id = e.id_especialidad WHERE gest.paciente_id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1,idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Medico medico = new Medico();
                    medico.setIdMedico(rs.getInt(1));
                    medico.setNombreMedico(rs.getString(2));
                    medico.setNumeroColegiado(rs.getInt(3));
                    medico.setEspecialidad(new Especialidad(rs.getInt(3), rs.getString(4)));

                    medicos.add(medico);
                }
            }
        }
        return medicos;
    }

    public static int actualizarPaciente(Paciente paciente) {
        int resultado = 0;
        String sql = "UPDATE pacientes SET nombre_paciente = ?, dni_paciente = ?, fecna_paciente = ? WHERE id_paciente = ?";
        Connection conexion = null;

        try {
            conexion = conectar();

            conexion.setAutoCommit(false);

            try (PreparedStatement ps = conexion.prepareStatement(sql)){
                ps.setString(1, paciente.getNombrePaciente());
                ps.setString(2, paciente.getDniPaciente());
                ps.setObject(3, paciente.getFechaNacimiento());
                ps.setInt(4, paciente.getIdPaciente());

                resultado = ps.executeUpdate();
            }

            // 3. Modificamos los médicos asociados compartiendo la misma transacción activa
            actualizarGestiones(paciente.getIdPaciente(), paciente.getMedicosList(), conexion);

            // 4. Si todo ha ido bien y no ha saltado ninguna excepción, consolidamos los cambios
            conexion.commit();
        } catch (SQLException e) {
            mostrarError("Error de transacción", "Error en la transacción, aplicando rollback : " + e.getMessage());
            if (conexion != null) {
                try {
                    // Deshacemos cualquier inserción o borrado parcial si algo falló
                    conexion.rollback();
                } catch (SQLException ex) {
                    mostrarError("Error de rollback","Error al aplicar el rollback: " + ex.getMessage());
                }
            }
        } finally {
            // Nos aseguramos siempre de cerrar la conexión de manera segura al terminar
            desconectar();
        }
        return resultado;
    }

    private static void actualizarGestiones(int idPaciente, List<Medico> medicos, Connection conexion) throws SQLException {
        String sqlDelete = "DELETE FROM gestiones WHERE paciente_id = ?";
        try (PreparedStatement psDelete = conexion.prepareStatement(sqlDelete)) {
            psDelete.setInt(1, idPaciente);
            psDelete.executeUpdate();
        }

        if (medicos != null && !medicos.isEmpty()) {
            String sqlInsert = "INSERT INTO gestiones (medico_id, paciente_id) VALUES (?, ?)";
            try (PreparedStatement psInsert = conexion.prepareStatement(sqlInsert)) {
                for (Medico medico : medicos) {
                    psInsert.setInt(1, medico.getIdMedico());
                    psInsert.setInt(2, idPaciente);
                    psInsert.executeUpdate();
                }
            }
        }
    }

    public static int eliminarPaciente(int idPaciente) {
        int resultado = 0;

        String sqlDeleteGestiones = "DELETE FROM gestiones WHERE paciente_id = ?";
        String sqlDeletePaciente = "DELETE FROM pacientes WHERE id_paciente = ?";

        try (Connection conexion = conectar()) {

            conexion.setAutoCommit(false);

            try {
                // Primero eliminamos las gestiones
                try (PreparedStatement ps = conexion.prepareStatement(sqlDeleteGestiones)) {
                    ps.setInt(1, idPaciente);
                    ps.executeUpdate();
                }

                // Después eliminamos el paciente
                try (PreparedStatement ps = conexion.prepareStatement(sqlDeletePaciente)) {
                    ps.setInt(1, idPaciente);
                    resultado = ps.executeUpdate();
                }

                conexion.commit();

            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            mostrarError("Error","Error al eliminar el paciente " + e.getMessage());
        }

        return resultado;
    }
}
