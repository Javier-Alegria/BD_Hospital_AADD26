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

public class PacienteController {

    @FXML
    private TextField txtNombrePaciente;
    @FXML
    private TextField txtDniPaciente;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private ListView<Medico> lvMedicos;

    @FXML
    private TableView<Paciente> tablaPacientes;
    @FXML
    private TableColumn<Paciente, Integer> colId;
    @FXML
    private TableColumn<Paciente, String> colNombrePaciente;
    @FXML
    private TableColumn<Paciente, String> colDniPaciente;
    @FXML
    private TableColumn<Paciente, LocalDate> colFecnaPaciente;
    @FXML
    private TableColumn<Paciente, String> colMedicos;

    @FXML
    public void initialize(){
        // Con este modo de selección se permiten selecciones múltiples
        lvMedicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Personalizamos el nombre de cada Piloto
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

        // Mapeamos la columna especial para concatenar los nombres de los pilotos que corren esa carrera
        colMedicos.setCellValueFactory(cellData -> {
            Paciente paciente = cellData.getValue();
            if (paciente != null && paciente.getMedicosList() != null && !paciente.getMedicosList().isEmpty()) {
                String nombres = paciente.getMedicosList().stream()
                        .map(Medico::getNombreMedico)
                        .collect(Collectors.joining(", "));
                return new SimpleStringProperty(nombres);
            }
            return new SimpleStringProperty("Sin participantes");
        });

        // Cargamos los registros iniciales de la tabla de carreras
        cargarDatosTabla();
    }

    private void cargarDatosTabla() {
        ObservableList<Paciente> carrerasObservableList = FXCollections.observableArrayList(PacienteDB.consultarPacientes());
        tablaPacientes.setItems(carrerasObservableList);
    }

    @FXML
    public void guardar(ActionEvent actionEvent) {
        Paciente nuevoPaciente = new Paciente();
        nuevoPaciente.setNombrePaciente(txtNombrePaciente.getText());
        nuevoPaciente.setDniPaciente(txtDniPaciente.getText());
        nuevoPaciente.setFechaNacimiento(dpFecha.getValue());

        // Extraer los pilotos seleccionados en el ListView e incluirlos en el objeto
        List<Medico> seleccionados = new ArrayList<>(lvMedicos.getSelectionModel().getSelectedItems());
        nuevoPaciente.setMedicosList(seleccionados);

        int resultado = PacienteDB.insertarPaciente(nuevoPaciente);
        if (resultado > 0) {
            cargarDatosTabla();
            limpiarFormulario(null);
        }
    }

    @FXML
    public void modificar(ActionEvent actionEvent) {
        Paciente pacienteSeleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (pacienteSeleccionado != null) {
            pacienteSeleccionado.setNombrePaciente(txtNombrePaciente.getText());
            pacienteSeleccionado.setDniPaciente(txtDniPaciente.getText());
            pacienteSeleccionado.setFechaNacimiento(dpFecha.getValue());

            // Obtener la selección actual del ListView de pilotos para actualizar participaciones
            List<Medico> seleccionados = new ArrayList<>(lvMedicos.getSelectionModel().getSelectedItems());
            pacienteSeleccionado.setMedicosList(seleccionados);

            int resultado = PacienteDB.actualizarPaciente(pacienteSeleccionado);
            if (resultado > 0) {
                cargarDatosTabla();
                limpiarFormulario(null);
            }
        }
    }

    @FXML
    public void eliminar(ActionEvent actionEvent) {
        Paciente carreraSeleccionada = tablaPacientes.getSelectionModel().getSelectedItem();
        if (carreraSeleccionada != null) {
            int resultado = PacienteDB.eliminarPaciente(carreraSeleccionada.getIdPaciente());
            if (resultado > 0) {
                cargarDatosTabla();
                limpiarFormulario(null);
            }
        }
    }

    @FXML
    public void limpiarFormulario(ActionEvent actionEvent) {
        txtNombrePaciente.clear();
        txtDniPaciente.clear();
        dpFecha.setValue(null);
        lvMedicos.getSelectionModel().clearSelection();
        tablaPacientes.getSelectionModel().clearSelection();
    }

    @FXML
    public void seleccionarFila(MouseEvent mouseEvent) {
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
