package com.hospital.hospitalmvcjavafx.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.io.IOException;

public class MenuController {
    @FXML
    private AnchorPane contentArea;

    public void mostrarEspecialidades() {
        cargarVista("/com/hospital/hospitalmvcjavafx/especialidad-view.fxml");
    }

    public void mostrarMedicos() {
        cargarVista("/com/hospital/hospitalmvcjavafx/medico-view.fxml");
    }

    public void mostrarClientes() {
        cargarVista("/com/hospital/hospitalmvcjavafx/paciente-view.fxml");
    }

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
