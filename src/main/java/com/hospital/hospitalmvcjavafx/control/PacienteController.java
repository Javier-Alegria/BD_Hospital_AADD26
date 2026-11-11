package com.hospital.hospitalmvcjavafx.control;

import com.hospital.hospitalmvcjavafx.dao.MedicoDB;
import com.hospital.hospitalmvcjavafx.dao.PacienteDB;
import com.hospital.hospitalmvcjavafx.modelo.Medico;
import com.hospital.hospitalmvcjavafx.modelo.Paciente;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.hospital.hospitalmvcjavafx.Utilidades.mostrarError;

public class PacienteController {

    @FXML private TextField txtNombrePaciente;
    @FXML private TextField txtDniPaciente;
    @FXML private DatePicker dpFecha;
    @FXML private ListView<Medico> lvMedicos;

    @FXML private TableView<Paciente> tablaPacientes;
    @FXML private TableColumn<Paciente, Integer> colId;
    @FXML private TableColumn<Paciente, String> colNombrePaciente;
    @FXML private TableColumn<Paciente, String> colDniPaciente;
    @FXML private TableColumn<Paciente, LocalDate> colFecnaPaciente;
    @FXML private TableColumn<Paciente, String> colMedicos;

    /**
     * Inicializa el controlador: activa la selección múltiple en la lista de médicos,
     * la rellena con los médicos de la base de datos, asocia cada columna de la tabla
     * con su atributo de Paciente (la de médicos muestra sus nombres separados por comas)
     * y carga los datos.
     */
    @FXML public void initialize(){
        // Con este modo de selección se permiten selecciones múltiples
        lvMedicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Personalizamos el nombre de cada Médico
        lvMedicos.setCellFactory(param -> new ListCell<Medico>() {
            @Override
            protected void updateItem(Medico medico, boolean empty) {
                super.updateItem(medico, empty);
                if (empty || medico == null) {
                    setText(null);
                } else {
                    setText(medico.getNombreMedico());
                }
            }
        });

        // Cargamos la lista completa de medicos disponibles desde la BD en el ListView
        List<Medico> todosLosMedicos = MedicoDB.listarMedicos();
        lvMedicos.setItems(FXCollections.observableArrayList(todosLosMedicos));

        // Mapeamos las columnas de la tabla al objeto Paciente utilizando el estilo definido
        colId.setCellValueFactory(new PropertyValueFactory<>("idPaciente"));
        colNombrePaciente.setCellValueFactory(new PropertyValueFactory<>("nombrePaciente"));
        colDniPaciente.setCellValueFactory(new PropertyValueFactory<>("dniPaciente"));
        colFecnaPaciente.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFechaNacimiento()));

        // Mapeamos la columna especial para concatenar los nombres de los médicos asignados al paciente
        colMedicos.setCellValueFactory(cellData -> {
            Paciente paciente = cellData.getValue();
            if (paciente != null && paciente.getMedicosList() != null && !paciente.getMedicosList().isEmpty()) {
                String nombres = paciente.getMedicosList().stream()
                        .map(Medico::getNombreMedico)
                        .collect(Collectors.joining(", "));
                return new SimpleStringProperty(nombres);
            }
            return new SimpleStringProperty("Sin médicos");
        });

        // Cargamos los registros iniciales de la tabla de pacientes
        cargarDatosTabla();
    }

    /**
     * Obtiene los pacientes de la base de datos y los muestra en la tabla.
     */
    private void cargarDatosTabla() {
        ObservableList<Paciente> pacienteObservableList = FXCollections.observableArrayList(PacienteDB.consultarPacientes());
        tablaPacientes.setItems(pacienteObservableList);
    }

    /**
     * Inserta un nuevo paciente con los datos del formulario y los médicos
     * seleccionados en la lista. Si se inserta correctamente, recarga la tabla
     * y limpia el formulario.
     *
     * @param actionEvent evento generado al pulsar el botón de guardar
     */
    @FXML public void guardar(ActionEvent actionEvent) {
        if (txtNombrePaciente.getText().trim().isEmpty()
                || txtDniPaciente.getText().trim().isEmpty()
                || dpFecha.getValue() == null) {
            mostrarError("Datos incompletos",
                    "El nombre, el DNI y la fecha de nacimiento son obligatorios"
            );
        } else {
            Paciente nuevoPaciente = new Paciente();
            nuevoPaciente.setNombrePaciente(txtNombrePaciente.getText().trim());
            nuevoPaciente.setDniPaciente(txtDniPaciente.getText().trim());
            nuevoPaciente.setFechaNacimiento(dpFecha.getValue());

            // Extraer los pilotos seleccionados en el ListView e incluirlos en el objeto
            List<Medico> seleccionados = new ArrayList<>(lvMedicos.getSelectionModel().getSelectedItems());
            nuevoPaciente.setMedicosList(seleccionados);

            int resultado = PacienteDB.insertarPaciente(nuevoPaciente);
            if (resultado > 0) {
                cargarDatosTabla();
                limpiarFormulario(null);
            } else {
                mostrarError("Error crítico",
                        "No se ha podido guardar el paciente"
                );
            }
        }
    }

    /**
     * Actualiza el paciente seleccionado en la tabla con los datos del formulario y
     * los médicos seleccionados en la lista. Muestra un error si no hay fila
     * seleccionada, si falta algún dato o si falla la actualización.
     *
     * @param actionEvent evento generado al pulsar el botón de modificar
     */
    @FXML
    public void modificar(ActionEvent actionEvent) {
        Paciente pacienteSeleccionado = tablaPacientes.getSelectionModel().getSelectedItem();

        if (pacienteSeleccionado == null) {
            mostrarError("Tabla no seleccionada",
                    "Debes seleccionar un paciente"
            );
        } else if (txtNombrePaciente.getText().trim().isEmpty()
                || txtDniPaciente.getText().trim().isEmpty()
                || dpFecha.getValue() == null) {
            mostrarError("Datos incompletos",
                    "El nombre, el DNI y la fecha de nacimiento son obligatorios"
            );
        } else {
            pacienteSeleccionado.setNombrePaciente(txtNombrePaciente.getText().trim());
            pacienteSeleccionado.setDniPaciente(txtDniPaciente.getText().trim());
            pacienteSeleccionado.setFechaNacimiento(dpFecha.getValue());

            // Obtener la selección actual del ListView de médicos
            List<Medico> seleccionados = new ArrayList<>(lvMedicos.getSelectionModel().getSelectedItems());
            pacienteSeleccionado.setMedicosList(seleccionados);

            int resultado = PacienteDB.actualizarPaciente(pacienteSeleccionado);
            if (resultado > 0) {
                cargarDatosTabla();
                limpiarFormulario(null);
            } else {
                mostrarError("Error crítico",
                        "No se ha podido modificar el paciente"
                );
            }
        }
    }

    /**
     * Borra el paciente seleccionado en la tabla. Muestra un error si no hay fila
     * seleccionada o si falla el borrado. Si se borra correctamente, recarga la
     * tabla y limpia el formulario.
     *
     * @param actionEvent evento generado al pulsar el botón de eliminar
     */
    @FXML
    public void eliminar(ActionEvent actionEvent) {
        Paciente pacienteSeleccionado = tablaPacientes.getSelectionModel().getSelectedItem();

        if (pacienteSeleccionado == null) {
            mostrarError("Error al borrar",
                    "Debes seleccionar un paciente"
            );
        } else {
            int resultado = PacienteDB.eliminarPaciente(pacienteSeleccionado.getIdPaciente());
            if (resultado > 0) {
                cargarDatosTabla();
                limpiarFormulario(null);
            } else {
                mostrarError("Error crítico",
                        "No se ha podido borrar el paciente"
                );
            }
        }
    }

    /**
     * Vacía los campos del formulario y quita la selección de la lista de médicos
     * y de la tabla.
     *
     * @param actionEvent evento generado al pulsar el botón de limpiar (puede ser null
     *                    si se llama desde otro método)
     */
    @FXML public void limpiarFormulario(ActionEvent actionEvent) {
        txtNombrePaciente.clear();
        txtDniPaciente.clear();
        dpFecha.setValue(null);
        lvMedicos.getSelectionModel().clearSelection();
        tablaPacientes.getSelectionModel().clearSelection();
    }

    /**
     * Al hacer clic en una fila de la tabla, rellena el formulario con los datos del
     * paciente y marca en la lista los médicos que tiene asignados.
     *
     * @param mouseEvent evento de ratón generado al hacer clic en la tabla
     */
    @FXML public void seleccionarFila(MouseEvent mouseEvent) {
        Paciente carreraSeleccionada = tablaPacientes.getSelectionModel().getSelectedItem();
        if (carreraSeleccionada != null) {
            txtNombrePaciente.setText(carreraSeleccionada.getNombrePaciente());
            txtDniPaciente.setText(carreraSeleccionada.getDniPaciente());
            dpFecha.setValue(carreraSeleccionada.getFechaNacimiento());

            // Limpiamos selecciones anteriores del ListView
            lvMedicos.getSelectionModel().clearSelection();

            // Marcamos de forma automática en el ListView los pilotos que ya participan en esta carrera
            if (carreraSeleccionada.getMedicosList() != null) {
                for (Medico pCarrera : carreraSeleccionada.getMedicosList()) {
                    for (Medico pList : lvMedicos.getItems()) {
                        if (pCarrera.getIdMedico() == pList.getIdMedico()) {
                            lvMedicos.getSelectionModel().select(pList);
                        }
                    }
                }
            }
        }
    }
}
