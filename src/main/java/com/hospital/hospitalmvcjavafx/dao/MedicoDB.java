package com.hospital.hospitalmvcjavafx.dao;

import com.hospital.hospitalmvcjavafx.modelo.Especialidad;
import com.hospital.hospitalmvcjavafx.modelo.Medico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.hospital.hospitalmvcjavafx.config.Conexion.conectar;
import static com.hospital.hospitalmvcjavafx.config.Conexion.desconectar;

import static com.hospital.hospitalmvcjavafx.Utilidades.mostrarError;

public class MedicoDB {
    public static List<Medico> listarMedicos() {
        List<Medico> medicosList = new ArrayList<>();

        String query = "SELECT * FROM medicos INNER JOIN especialidades ON medicos.especialidad_id = especialidades.id_especialidad";

        try (Statement st = conectar().createStatement()){

            ResultSet rs = st.executeQuery(query);

            while(rs.next()) {
                Medico medico = new Medico();
                medico.setIdMedico(rs.getInt(1));
                medico.setNombreMedico(rs.getString(2));
                medico.setNumeroColegiado(rs.getInt(3));
                medico.setEspecialidad(new Especialidad(rs.getInt(5), rs.getString(6)));

                medicosList.add(medico);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error de listado",
                    "Error al listar los médicos"
            );
        } finally {
            desconectar();
        }
        return medicosList;
    }

    public static int insertarMedico(Medico medico){
        int respuesta = 0;
        String sql = "INSERT INTO medicos VALUES (0, ?, ?, ?)";

        try (Connection conexion = conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);) {

            ps.setString(1, medico.getNombreMedico());
            ps.setInt(2, medico.getNumeroColegiado());
            ps.setInt(3, medico.getEspecialidad().getIdEspecialidad());

            respuesta = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Médico duplicado",
                    "Error al insertar el medico");
        }
        return respuesta;
    }

    public static int borrarMedico(Medico medico){
        int respuesta = 0;
        String query = "DELETE FROM medicos WHERE nombre_medico = ?";

        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setString(1, medico.getNombreMedico());

            respuesta = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    public static void actualizarMedico(int id, String nombre, int colegiado, Especialidad especialidad){
        String query = "UPDATE medicos SET nombre_medico = ?, numero_colegiado = ?, especialidad_id = ? WHERE id_medico = ?";

        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setString(1, nombre);
            ps.setInt(2, colegiado);
            ps.setInt(3, especialidad.getIdEspecialidad());
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
