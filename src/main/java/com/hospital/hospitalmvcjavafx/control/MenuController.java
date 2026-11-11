package com.hospital.hospitalmvcjavafx.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MenuController {
    @FXML
    private AnchorPane contentArea;

    /**
     * Carga la vista de especialidades en el área de contenido.
     */
    public void mostrarEspecialidades() {
        cargarVista("/com/hospital/hospitalmvcjavafx/especialidad-view.fxml");
    }

    /**
     * Carga la vista de médicos en el área de contenido.
     */
    public void mostrarMedicos() {
        cargarVista("/com/hospital/hospitalmvcjavafx/medico-view.fxml");
    }

    /**
     * Carga la vista de pacientes en el área de contenido.
     */
    public void mostrarClientes() {
        cargarVista("/com/hospital/hospitalmvcjavafx/paciente-view.fxml");
    }

    /**
     * Carga un archivo FXML y lo muestra en el área de contenido, sustituyendo
     * lo que hubiera. Throws IOException si no se puede cargar, mostrando una alerta de error
     *
     * @param path ruta del archivo FXML dentro de los recursos del proyecto
     */
    private void cargarVista(String path) {
        try {
            Parent node = FXMLLoader.load(getClass().getResource(path));
            contentArea.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se ha podido cargar el archivo");
            alert.show();
        }
    }
}
