package ProyectoFinal_Gimnasio;

import ProyectoFinal_Gimnasio.Factory.ModelFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GimnasioUQApp extends Application {

    private ModelFactory modelFactory;

    @Override
    public void start(Stage primaryStage) throws Exception {
        modelFactory = ModelFactory.getInstance();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Gimnasio UQ Fit");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (modelFactory != null) {
            modelFactory.guardarDatos(modelFactory.getGimnasio());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
