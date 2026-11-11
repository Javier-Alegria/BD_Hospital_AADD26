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

    /**
     * Inicializa el controlador: carga las especialidades en el ComboBox, asocia
     * cada columna de la tabla con su atributo de Médico y carga los datos.
     */
    @FXML public void initialize(){
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

    /**
     * Inicializa el controlador: carga las especialidades en el ComboBox, asocia
     * cada columna de la tabla con su atributo de Medico y carga los datos.
     */
    private void cargarDatos(){
        ObservableList<Medico> medicosObservableList = FXCollections.observableArrayList(MedicoDB.listarMedicos());
        tblMedicos.setItems(medicosObservableList);
    }

    /**
     * Inserta un nuevo médico con los datos del formulario. Todos los campos son
     * obligatorios y el número de colegiado debe ser un entero. Después recarga
     * la tabla y limpia el formulario.
     *
     * @param actionEvent evento generado al pulsar el botón de guardar
     */
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

    /**
     * Actualiza el médico seleccionado en la tabla con los datos del formulario.
     * Muestra un error si no hay fila seleccionada, si falta algún dato, si el
     * número de colegiado no es un entero (NumberFormatException) o si falla la
     * actualización (RuntimeException).
     *
     * @param actionEvent evento generado al pulsar el botón de modificar
     */
    public void modificar(ActionEvent actionEvent) {
        Medico seleccionado = tblMedicos.getSelectionModel().getSelectedItem();

        String nombre = txtNombre.getText().trim();


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
                int colegiado = Integer.parseInt(intColegiado.getText().trim());

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

    /**
     * Borra el médico seleccionado en la tabla y recarga los datos. Muestra un
     * error si no hay selección o si falla el borrado (RuntimeException).
     *
     * @param actionEvent evento generado al pulsar el botón de eliminar
     */
    public void eliminar(ActionEvent actionEvent) {
        Medico seleccionado = tblMedicos.getSelectionModel().getSelectedItem();

        if(seleccionado == null){
            mostrarError("Error al borrar",
                    "Debes seleccionar un médico"
            );
        } else {
            try {
                borrarMedico(seleccionado);
                cargarDatos();
            } catch (RuntimeException e) {
                mostrarError("Error crítico",
                        "No se ha podido borrar médico: " + e.getMessage()
                );
            }
        }
    }

    /**
     * Vacía los campos del formulario y deselecciona la especialidad del ComboBox.
     *
     * @param actionEvent evento generado al pulsar el botón de limpiar (puede ser null
     *                    si se llama desde otro método)
     */
    public void limpiarFormulario(ActionEvent actionEvent) {
        txtNombre.clear();
        intColegiado.clear();
        cbEspecialidad.getSelectionModel().clearSelection();
    }

    /**
     * Al hacer clic en una fila de la tabla, rellena el formulario con el nombre,
     * el número de colegiado y la especialidad del médico seleccionado.
     *
     * @param mouseEvent evento de ratón generado al hacer clic en la tabla
     */
    public void seleccionarFila(MouseEvent mouseEvent) {
        Medico seleccionado = tblMedicos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            txtNombre.setText(seleccionado.getNombreMedico());
            intColegiado.setText(String.valueOf(seleccionado.getNumeroColegiado()));
            cbEspecialidad.setValue(seleccionado.getEspecialidad());
        }
    }
}
