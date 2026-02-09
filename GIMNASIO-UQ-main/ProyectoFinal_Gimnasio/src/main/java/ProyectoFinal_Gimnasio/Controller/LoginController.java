package ProyectoFinal_Gimnasio.Controller;

import ProyectoFinal_Gimnasio.Factory.ModelFactory;
import ProyectoFinal_Gimnasio.Model.Administrador;
import ProyectoFinal_Gimnasio.Model.Gimnasio;
import ProyectoFinal_Gimnasio.Model.Recepcionista;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField userTxt;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private Button loginBtn;

    // Usar ModelFactory para obtener la instancia de Gimnasio
    private Gimnasio gimnasio = ModelFactory.getInstance().getGimnasio();

    @FXML
    void login(ActionEvent event) {
        String usuario = userTxt.getText();
        String password = passwordTxt.getText();

        Object empleado = gimnasio.login(usuario, password);

        if (empleado instanceof Administrador) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminView.fxml"));
                Parent root = loader.load();
                // No es necesario pasar el controlador aquí, FXML lo maneja
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) loginBtn.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (empleado instanceof Recepcionista) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/RecepcionistaView.fxml"));
                Parent root = loader.load();
                // No es necesario pasar el controlador aquí, FXML lo maneja
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) loginBtn.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de inicio de sesión");
            alert.setHeaderText("Credenciales incorrectas");
            alert.setContentText("El usuario o la contraseña son incorrectos.");
            alert.showAndWait();
        }
    }
}
