module com.hospital.hospitalmvcjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.base;

    opens com.hospital.hospitalmvcjavafx to javafx.fxml;
    opens com.hospital.hospitalmvcjavafx.control to javafx.fxml;
    opens com.hospital.hospitalmvcjavafx.modelo to javafx.base;
    exports com.hospital.hospitalmvcjavafx;
}