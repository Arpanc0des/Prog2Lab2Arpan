module com.example.prog2lab1arpan {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.example.prog2lab1arpan to javafx.fxml;
    exports com.example.prog2lab1arpan;
}