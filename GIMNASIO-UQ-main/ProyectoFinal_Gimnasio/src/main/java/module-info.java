module ProyectoFinal_Gimnasio {
    // --- Dependencias de JavaFX ---
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    // --- Dependencias de Java ---
    requires java.desktop;

    // --- Apertura de paquetes para FXML y reflexión ---
    opens ProyectoFinal_Gimnasio.Controller to javafx.fxml;
    opens ProyectoFinal_Gimnasio.ViewController to javafx.fxml;
    opens ProyectoFinal_Gimnasio.Model to javafx.base, javafx.fxml;

    // --- Exportación de paquetes ---
    exports ProyectoFinal_Gimnasio;
    exports ProyectoFinal_Gimnasio.Model;
    exports ProyectoFinal_Gimnasio.Controller;
    exports ProyectoFinal_Gimnasio.ViewController;
}
