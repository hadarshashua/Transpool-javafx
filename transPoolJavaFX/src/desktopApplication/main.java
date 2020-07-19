package desktopApplication;


import controller.MainController;
import desktopApplication.components.main.JavaFXController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class main extends Application {

    private String theme1Url = getClass().getResource("/desktopApplication/components/main/theme1.css").toExternalForm();

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/desktopApplication/components/main/mainTransPool.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load();

        MainController mainController = new MainController();
        JavaFXController javaFXController = new JavaFXController();
        javaFXController = fxmlLoader.getController();
        javaFXController.setPrimaryStage(primaryStage);
        javaFXController.setMainController(mainController);
        javaFXController.setFxmlLoader(fxmlLoader);

        Scene scene = new Scene(root, 1000, 600);

        primaryStage.setScene(scene);
        scene.getStylesheets().add(theme1Url);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



