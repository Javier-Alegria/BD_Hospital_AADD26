package com.hospital.hospitalmvcjavafx.control;

import com.hospital.hospitalmvcjavafx.dao.MedicoDB;
import com.hospital.hospitalmvcjavafx.modelo.Especialidad;
import com.hospital.hospitalmvcjavafx.modelo.Medico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import static com.hospital.hospitalmvcjavafx.dao.EspecialidadDB.*;
import static com.hospital.hospitalmvcjavafx.dao.MedicoDB.borrarMedico;
import static com.hospital.hospitalmvcjavafx.dao.MedicoDB.insertarMedico;

import static com.hospital.hospitalmvcjavafx.Utilidades.mostrarError;

public class MedicoController {

    // Menú de insert
    @FXML private TextField txtNombre;
    @FXML private TextField intColegiado;
    @FXML private TextField txtEspecialidad;

    // Tabla de datos
    @FXML private TableView<Medico> tblMedicos;

    // Id's de tabla de datos
    @FXML private TableColumn<Medico, Integer> colId;
    @FXML private TableColumn<Medico, String> colNombre;
    @FXML private TableColumn<Medico, String> colEspecialidad;
    @FXML private TableColumn<Medico, Integer> colNumColegiado;

    @FXML
    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("idMedico"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreMedico"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidadNombre"));
        colNumColegiado.setCellValueFactory(new PropertyValueFactory<>("numeroColegiado"));
        cargarDatos();
    }

    private void cargarDatos(){
        ObservableList<Medico> medicos = FXCollections.observableArrayList(MedicoDB.listarMedicos());
        tblMedicos.setItems(medicos);
    }

    private void limpiarForm(){
        txtNombre.clear();
        intColegiado.clear();
        txtEspecialidad.clear();
    }

    public void guardar(ActionEvent actionEvent) {

        String especialidad = txtEspecialidad.getText().trim();

        if (txtNombre.getText().trim().isEmpty()
            || intColegiado.getText().trim().isEmpty()
            || especialidad.isEmpty() ){
            mostrarError("Datos incompletos",
                    "Todos los campos son obligatorios"
            );
        } else if (!comprobarEspecialidad(especialidad)) {
            mostrarError("Error de búsqueda",
                    "La especialidad indicada no existe en la base de datos"
            );
        } else {
            try {
                int numeroColegiado = Integer.parseInt(intColegiado.getText().trim());

                Especialidad esp = buscarEspecialidad(especialidad);

                insertarMedico(new Medico(0,txtNombre.getText().trim(),numeroColegiado,esp));

                cargarDatos();
                limpiarForm();
            } catch (NumberFormatException e) {
                mostrarError("Datos incorrectos",
                        "El número de colegiado debe ser un entero válido"
                );
            }
        }

    }

    public void modificar(ActionEvent actionEvent) {
        Medico seleccionado = tblMedicos.getSelectionModel().getSelectedItem();

        String nombre = txtNombre.getText().trim();

        String especialidad = txtEspecialidad.getText().trim();

        if (seleccionado == null) {
            mostrarError("Tabla no seleccionada",
                    "Debes seleccionar un medico"
            );
        } else if (!comprobarEspecialidad(especialidad)) {
            mostrarError("Error de búsqueda",
                    "La especialidad no existe en la base de datos"
            );
        } else if (nombre.isEmpty()) {
            mostrarError("Datos incompletos",
                    "El nombre es obligatorio"
            );
        } else if (intColegiado.getText().trim().isEmpty()) {
            mostrarError("Datos incompletos",
                    "El número de colegiado es obligatorio"
            );
        } else {
            try {
                int colegiado = Integer.parseInt(intColegiado.getText().trim());

                Especialidad esp = buscarEspecialidad(especialidad);

                MedicoDB.actualizarMedico(seleccionado.getIdMedico(), nombre, colegiado, esp);

                cargarDatos();
                tblMedicos.getSelectionModel().clearSelection();
                limpiarForm();

            } catch (NumberFormatException e) {
                mostrarError("Datos incorrectos",
                        "El número de colegiado debe ser un entero válido"
                );
            } catch (RuntimeException re) {
                mostrarError("Error crítico",
                        "No se ha podido modificar el médico"
                );
            }
        }
    }

    public void eliminar(ActionEvent actionEvent) {
        try {
            borrarMedico(tblMedicos.getSelectionModel().getSelectedItem());
            cargarDatos();
        } catch (RuntimeException e) {
            e.printStackTrace();
            mostrarError("Error crítico",
                    "No se ha podido borrar médico"
            );
        }
    }

    public void limpiarFormulario(ActionEvent actionEvent) {
        limpiarForm();
    }

    public void seleccionarFila(MouseEvent mouseEvent) {
        Medico seleccionado = tblMedicos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            txtNombre.setText(seleccionado.getNombreMedico());
            intColegiado.setText(String.valueOf(seleccionado.getNumeroColegiado()));
            txtEspecialidad.setText(seleccionado.getEspecialidadNombre());

        }
    }
}
