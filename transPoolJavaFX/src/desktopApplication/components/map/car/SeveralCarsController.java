package desktopApplication.components.map.car;

import controller.trips.Trip;
import desktopApplication.components.main.JavaFXController;
import desktopApplication.components.tripRequest.AddTripRequestController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class SeveralCarsController
{
    @FXML private VBox severalCarsIcon;

    private int x;
    private int y;
    private List<Trip> tripsInStation;
    private JavaFXController controller;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setController(JavaFXController controller) {
        this.controller = controller;
    }

    public void setTripsInStation(List<Trip> tripsInStation) {
        this.tripsInStation = tripsInStation;
    }

    @FXML
    void showAllTripsInStation(MouseEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApplication/components/map/car/showTrips.fxml");
            loader.setLocation(url);
            Parent root = loader.load();
            ShowTripsController showTripsController = loader.getController();
            showTripsController.setTripsInStation(this.tripsInStation);
            showTripsController.setController(this.controller);
            showTripsController.setDriversComboBox();

            Stage stage = new Stage();
            stage.setTitle("show trips in station");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(JavaFXController.getPrimaryStage());
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
