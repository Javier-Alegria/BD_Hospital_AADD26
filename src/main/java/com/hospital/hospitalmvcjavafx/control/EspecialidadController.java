package com.hospital.hospitalmvcjavafx.control;

import com.hospital.hospitalmvcjavafx.dao.EspecialidadDB;
import com.hospital.hospitalmvcjavafx.modelo.Especialidad;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import static com.hospital.hospitalmvcjavafx.dao.EspecialidadDB.*;

import static com.hospital.hospitalmvcjavafx.Utilidades.mostrarError;

public class EspecialidadController {

    // Menu de insert
    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;

    // Tabla de datos
    @FXML private TableView<Especialidad> tablaEspecialidades;

    // Id's de tabla de datos
    @FXML private TableColumn<Especialidad, Integer> colId;
    @FXML private TableColumn<Especialidad, String> colNombre;
    @FXML private TableColumn<Especialidad, String> colDescripcion;


    /**
     * Inicializa el controlador: asocia cada columna de la tabla con su atributo
     * de Especialidad y carga los datos.
     */
    @FXML public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEspecialidad"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEspecialidad"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcionEspecialidad"));
        cargarDatosTabla();
    }

    /**
     * Obtiene las especialidades de la base de datos y las muestra en la tabla.
     */
    private void cargarDatosTabla() {
        ObservableList<Especialidad> especialidades = FXCollections.observableArrayList(EspecialidadDB.listarEspecialidades());
        tablaEspecialidades.setItems(especialidades);
    }

    /**
     * Inserta una nueva especialidad con los datos del formulario. El nombre es
     * obligatorio; la descripción es opcional. Después recarga la tabla y limpia el formulario.
     *
     * @param actionEvent evento generado al pulsar el botón de guardar
     */
    public void guardar(ActionEvent actionEvent) {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("Datos incompletos",
                    "El nombre de la especialidad es obligatorio"
            );
        } else {
            if (txtDescripcion.getText().trim().isEmpty())
                insertarEspecialidad(new Especialidad(0, txtNombre.getText().trim()));
            else
                insertarEspecialidad(new Especialidad(0, txtNombre.getText().trim(), txtDescripcion.getText().trim()));

            cargarDatosTabla();
            limpiarFormulario(null);
        }
    }

    /**
     * Actualiza la especialidad seleccionada en la tabla con los datos del formulario.
     * Muestra un error si no hay fila seleccionada, si el nombre está vacío o si
     * falla la actualización (RuntimeException).
     *
     * @param actionEvent evento generado al pulsar el botón de modificar
     */
    public void modificar(ActionEvent actionEvent) {
        Especialidad seleccionada =
                tablaEspecialidades.getSelectionModel().getSelectedItem();

        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (seleccionada == null) {
            mostrarError("Tabla no seleccionada",
                    "Debes seleccionar una especialidad."
            );
        } else if (nombre.isEmpty()) {
            mostrarError("Datos incompletos",
                    "El nombre de la especialidad es obligatorio."
            );
        } else {
            try {
                EspecialidadDB.actualizarEspecialidad(seleccionada.getIdEspecialidad(), nombre, descripcion);

                cargarDatosTabla();
                tablaEspecialidades.getSelectionModel().clearSelection();
                limpiarFormulario(null);

            } catch (RuntimeException e) {
                e.printStackTrace();
                mostrarError("Error crítico",
                        "No se ha podido modificar la especialidad."
                );
            }
        }
    }

    /**
     * Borra la especialidad seleccionada en la tabla. Muestra un error si no hay
     * selección o si tiene médicos asociados (RuntimeException).
     *
     * @param actionEvent evento generado al pulsar el botón de eliminar
     */
    public void eliminar(ActionEvent actionEvent) {
        Especialidad seleccionada = tablaEspecialidades.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarError("Error al borrar la especialidad",
                    "Debe seleccionar una especialidad para poder borrarla"
            );
        } else {
            try {
                borrarEspecialidad(seleccionada);
                cargarDatosTabla();

            } catch (RuntimeException e) {
                e.printStackTrace();
                mostrarError("Error al borrar la especialidad",
                        "No se puede borrar una especialidad con médicos asociados. Primero debe eliminar los médicos o cambiarles la especialidad."
                );
            }
        }
    }

    /**
     * Vacía los campos del formulario al pulsar el botón de limpiar.
     *
     * @param actionEvent evento generado al pulsar el botón de limpiar
     */
    public void limpiarFormulario(ActionEvent actionEvent) {
        txtNombre.clear();
        txtDescripcion.clear();
    }

    /**
     * Al hacer clic en una fila de la tabla, rellena el formulario con el nombre
     * y la descripción de la especialidad seleccionada.
     *
     * @param mouseEvent evento de ratón generado al hacer clic en la tabla
     */
    public void seleccionarFila(MouseEvent mouseEvent) {
        Especialidad seleccionada = tablaEspecialidades.getSelectionModel().getSelectedItem();

        if (seleccionada != null) {
            txtNombre.setText(seleccionada.getNombreEspecialidad());
            txtDescripcion.setText(seleccionada.getDescripcionEspecialidad());
        }
    }
}
