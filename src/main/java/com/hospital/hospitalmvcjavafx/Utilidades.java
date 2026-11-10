package com.hospital.hospitalmvcjavafx;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class Utilidades {

    public static void mostrarError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);

        alerta.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alerta.getDialogPane().setPrefWidth(400);

        alerta.showAndWait();
    }
}