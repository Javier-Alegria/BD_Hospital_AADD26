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

    /**
     * Obtiene todos los médicos de la base de datos junto con su especialidad.
     *
     * @return lista de médicos (vacía si no hay ninguno o si falla la consulta)
     */
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

    /**
     * Inserta un nuevo médico en la base de datos. Si falla (por ejemplo, por un
     * número de colegiado duplicado) muestra un mensaje de error.
     *
     * @param medico médico a insertar
     * @return número de filas insertadas (1 si se inserta, 0 si falla)
     */
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

    /**
     * Borra de la base de datos el médico con el mismo número de colegiado que el recibido.
     *
     * @param medico médico a borrar
     * @throws RuntimeException si falla la operación en la base de datos
     * @return número de filas borradas (0 si no existe o si falla el borrado)
     */
    public static int borrarMedico(Medico medico){
        int respuesta = 0;
        String query = "DELETE FROM medicos WHERE numero_colegiado = ?";

        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setInt(1, medico.getNumeroColegiado());

            respuesta = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return respuesta;
    }

    /**
     * Actualiza el nombre, el número de colegiado y la especialidad del médico con el id indicado.
     *
     * @param id identificador del médico a modificar
     * @param nombre nuevo nombre del médico
     * @param colegiado nuevo número de colegiado
     * @param especialidad nueva especialidad del médico
     *
     * @throws RuntimeException si falla la operación en la base de datos
     */
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
            throw new RuntimeException(e);
        }
    }
}
