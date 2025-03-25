package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repositories.base.RepositoryFactory;
import services.Service;


public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Update the path to your FXML file correctly
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/layout.fxml"));
        Parent root = loader.load();

        // Inject services into the controller
        MainController controller = loader.getController();
        controller.initServices(
                new Service<>(RepositoryFactory.createPatientRepository(), new validators.PatientValidator()),
                new Service<>(RepositoryFactory.createAppointmentRepository(), new validators.AppointmentValidator())
        );

        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
