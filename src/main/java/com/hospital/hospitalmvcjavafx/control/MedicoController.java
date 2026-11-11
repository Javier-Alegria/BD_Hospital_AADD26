package com.hospital.hospitalmvcjavafx.control;

import com.hospital.hospitalmvcjavafx.dao.MedicoDB;
import com.hospital.hospitalmvcjavafx.modelo.Especialidad;
import com.hospital.hospitalmvcjavafx.modelo.Medico;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.List;

import static com.hospital.hospitalmvcjavafx.dao.EspecialidadDB.*;
import static com.hospital.hospitalmvcjavafx.dao.MedicoDB.borrarMedico;
import static com.hospital.hospitalmvcjavafx.dao.MedicoDB.insertarMedico;

import static com.hospital.hospitalmvcjavafx.Utilidades.mostrarError;

public class MedicoController {

    // Menú de insert
    @FXML private TextField txtNombre;
    @FXML private TextField intColegiado;
    @FXML private ComboBox<Especialidad> cbEspecialidad;

    // Tabla de datos
    @FXML private TableView<Medico> tblMedicos;

    // Id's de tabla de datos
    @FXML private TableColumn<Medico, Integer> colId;
    @FXML private TableColumn<Medico, String> colNombre;
    @FXML private TableColumn<Medico, String> colEspecialidad;
    @FXML private TableColumn<Medico, Integer> colNumColegiado;

    @FXML
    public void initialize(){
        // 1- Obtener la lista de Especialidades
        List<Especialidad> especialidadesList = listarEspecialidades();

        // 2- Convertirla en una observable list (Requerido por JavaFX
        ObservableList<Especialidad> especialidadesObservableList = FXCollections.observableArrayList(especialidadesList);

        // 3- Asignar los Items al combo box
        if (cbEspecialidad != null){
            cbEspecialidad.setItems(especialidadesObservableList);
        } else {
            mostrarError("Alerta del código","cbEspecialidades sigue viniendo null desde el FXML.");
        }

        colId.setCellValueFactory(new PropertyValueFactory<>("idMedico"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreMedico"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidadNombre"));
        colNumColegiado.setCellValueFactory(new PropertyValueFactory<>("numeroColegiado"));
        cargarDatos();
    }

    private void cargarDatos(){
        ObservableList<Medico> medicosObservableList = FXCollections.observableArrayList(MedicoDB.listarMedicos());
        tblMedicos.setItems(medicosObservableList);
    }

    public void guardar(ActionEvent actionEvent) {
        if (txtNombre.getText().trim().isEmpty()
            || intColegiado.getText().trim().isEmpty()
            || cbEspecialidad.getSelectionModel().getSelectedItem() == null ){
            mostrarError("Datos incompletos",
                    "Todos los campos son obligatorios"
            );
        } else {
            try {
                int numeroColegiado = Integer.parseInt(intColegiado.getText().trim());

                insertarMedico(new Medico(0,txtNombre.getText().trim(),numeroColegiado,cbEspecialidad.getValue()));

                cargarDatos();
                limpiarFormulario(null);
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
        int colegiado = Integer.parseInt(intColegiado.getText().trim());

        if (seleccionado == null) {
            mostrarError("Tabla no seleccionada",
                    "Debes seleccionar un medico"
            );
        } else if (cbEspecialidad.getSelectionModel().getSelectedItem() == null) {
            mostrarError("Datos incompletos",
                    "Debe seleccionar una especialidad para el médico"
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
                MedicoDB.actualizarMedico(seleccionado.getIdMedico(), nombre, colegiado, cbEspecialidad.getValue());

                cargarDatos();
                tblMedicos.getSelectionModel().clearSelection();
                limpiarFormulario(null);

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
            mostrarError("Error crítico",
                    "No se ha podido borrar médico: " + e.getMessage()
            );
        }
    }

    public void limpiarFormulario(ActionEvent actionEvent) {
        txtNombre.clear();
        intColegiado.clear();
        cbEspecialidad.setValue(null);
    }

    public void seleccionarFila(MouseEvent mouseEvent) {
        Medico seleccionado = tblMedicos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            txtNombre.setText(seleccionado.getNombreMedico());
            intColegiado.setText(String.valueOf(seleccionado.getNumeroColegiado()));
            cbEspecialidad.setValue(seleccionado.getEspecialidad());
        }
    }
}
