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


    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEspecialidad"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEspecialidad"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcionEspecialidad"));
        cargarDatosTabla();
    }

    private void cargarDatosTabla() {
        ObservableList<Especialidad> especialidades = FXCollections.observableArrayList(EspecialidadDB.listarEspecialidades());
        tablaEspecialidades.setItems(especialidades);
    }

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
            limpiarForm();
        }
    }

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
                limpiarForm();

            } catch (RuntimeException e) {
                e.printStackTrace();
                mostrarError("Error crítico",
                        "No se ha podido modificar la especialidad."
                );
            }
        }
    }

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

    public void limpiarFormulario(ActionEvent actionEvent) {
        limpiarForm();
    }

    public void limpiarForm() {
        txtNombre.clear();
        txtDescripcion.clear();
    }

    public void seleccionarFila(MouseEvent mouseEvent) {
        Especialidad seleccionada = tablaEspecialidades.getSelectionModel().getSelectedItem();

        if (seleccionada != null) {
            txtNombre.setText(seleccionada.getNombreEspecialidad());
            txtDescripcion.setText(seleccionada.getDescripcionEspecialidad());
        }
    }
}
