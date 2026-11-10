package com.hospital.hospitalmvcjavafx.dao;

import com.hospital.hospitalmvcjavafx.modelo.Especialidad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.hospital.hospitalmvcjavafx.config.Conexion.conectar;
import static com.hospital.hospitalmvcjavafx.config.Conexion.desconectar;

import static com.hospital.hospitalmvcjavafx.Utilidades.mostrarError;

public class EspecialidadDB {
    public static List<Especialidad> listarEspecialidades() {
        List<Especialidad> especialidades = new ArrayList<>();
        ResultSet rs;
        String query = "SELECT * FROM especialidades";
        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(query);) {

            rs = ps.executeQuery();

            while (rs.next()) {
                Especialidad especialidad = new Especialidad();
                especialidad.setIdEspecialidad(rs.getInt(1));
                especialidad.setNombreEspecialidad(rs.getString(2));
                especialidad.setDescripcionEspecialidad(rs.getString(3));
                especialidades.add(especialidad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error de listado",
                    "Error al listar las especialidades"
            );
        }
        return especialidades;
    }

    public static int insertarEspecialidad(Especialidad especialidad) {
        int resultado = 0;
        String query = "INSERT INTO especialidades VALUES (0, ?, ?)";
        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setString(1, especialidad.getNombreEspecialidad());
            ps.setString(2, especialidad.getDescripcionEspecialidad());

            resultado = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Especialidad duplicada",
                    "Error al insertar la especialidad"
            );
        }
        return resultado;
    }

    public static int borrarEspecialidad(Especialidad especialidad) {
        int respuesta = 0;
        String query = "DELETE FROM especialidades WHERE nombre_especialidad = ?";

        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setString(1, especialidad.getNombreEspecialidad());

            respuesta = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    /**
     * Comprueba que una especialidad existe en la base de datos
     * @param nombre_especialidad nombre de la especialidad a buscar
     * @return True si el nombre de la especialidad existe | False si el nombre de la especialidad no existe
     */
    public static boolean comprobarEspecialidad(String nombre_especialidad) {
        boolean respuesta = false;
        String query = "SELECT COUNT(*) FROM especialidades WHERE nombre_especialidad = ?";

        try (Connection conexion = conectar();
            PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setString(1, nombre_especialidad);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    respuesta = rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    /**
     * Devuelve un objeto especialidad basandose en el nombre de la especialidad insertado
     * @param nombre nombre de la especialidad
     * @return Objeto especialidad
     */
    public static Especialidad buscarEspecialidad(String nombre) {
        Especialidad especialidad = null;
        String query = "SELECT id_especialidad, nombre_especialidad, descripcion_especialidad "
                + "FROM especialidades WHERE nombre_especialidad = ?";

        try (Connection conexion = conectar();
             PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    especialidad = new Especialidad(
                            rs.getInt("id_especialidad"),
                            rs.getString("nombre_especialidad"),
                            rs.getString("descripcion_especialidad")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return especialidad;
    }

    public static void actualizarEspecialidad(int id, String nombre, String descripcion) {
        String sql = "UPDATE especialidades SET nombre_especialidad = ?, descripcion_especialidad = ? WHERE id_especialidad = ?";

        try (Connection conexion = conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setInt(3, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
