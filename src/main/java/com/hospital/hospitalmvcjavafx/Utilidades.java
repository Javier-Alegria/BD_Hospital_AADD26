package com.hospital.hospitalmvcjavafx;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class Utilidades {

    /**
     * Muestra una ventana de alerta de error y espera a que el usuario la cierre.
     *
     * @param titulo texto que aparece en la cabecera de la alerta
     * @param mensaje texto con la descripción del error
     */
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