package desktopApplication.components.map.car;

import com.fxgraph.graph.Model;
import controller.stations.Station;
import controller.trips.Path;
import controller.trips.Trip;
import desktopApplication.components.main.JavaFXController;
import desktopApplication.components.map.coordinate.CoordinatesManager;
import desktopApplication.components.map.layout.MapGridLayout;
import desktopApplication.components.map.road.BoldArrowedEdge;
import desktopApplication.components.map.station.StationManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.List;

public class ShowTripsController {

    @FXML private ComboBox<String> driversComboBox;
    @FXML private Button cancelButton;
    @FXML private Button showRouteButton;
    @FXML private Label errorLabel;

    private List<Trip> tripsInStation;
    private JavaFXController controller;

    public ShowTripsController()
    {

    }

    @FXML
    private void initialize()
    {

    }

    public void setController(JavaFXController controller) {
        this.controller = controller;
    }

    public void setTripsInStation(List<Trip> tripsInStation) {
        this.tripsInStation = tripsInStation;
    }

    public void setDriversComboBox()
    {
        ObservableList<String> driversList = FXCollections.observableArrayList();
        for(int i=0; i<this.tripsInStation.size(); i++)
        {
            driversList.add(this.tripsInStation.get(i).getOwner());
        }
        driversComboBox.setItems(driversList);
    }

    @FXML
    void cancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void showRouteButtonAction(ActionEvent event)
    {
        String driver;
        this.errorLabel.setText("");
        boolean isDriverChosen = true;

        driver = driversComboBox.getValue();
        if(driversComboBox.getValue() == null)
        {
            this.errorLabel.setText("Error: choose driver!");
            isDriverChosen = false;
        }


        Trip trip = null;

        if(isDriverChosen)
        {
            this.controller.getGraph().beginUpdate();

            StationManager sm = this.controller.createStations(this.controller.getGraph().getModel());
            CoordinatesManager cm = this.controller.createCoordinates(this.controller.getGraph().getModel());
            this.controller.createEdges(this.controller.getGraph().getModel(), this.controller.getCm());
            CarManager carM = this.controller.putCarIconInRellevantStation(this.controller.getGraph().getModel());
            SeveralCarsManager sCarM = this.controller.putSeveralCarsIconInRellevantStation(this.controller.getGraph().getModel());

            for (int i=0; i<this.tripsInStation.size(); i++)
            {
                if(this.tripsInStation.get(i).getOwner().equals(driver))
                    trip = tripsInStation.get(i);
            }

            for(int i=0; i<trip.getRoute().size(); i++)
            {
                Path path = trip.getRoute().get(i);
                Station from = this.controller.convertStringToStation(path.getFrom());
                Station to = this.controller.convertStringToStation(path.getTo());

                Model model = this.controller.getGraph().getModel();
                BoldArrowedEdge edge = new BoldArrowedEdge(cm.getOrCreate(from.getX(),from.getY()), cm.getOrCreate(to.getX(),to.getY()), path.isOneWay());

                model.addEdge(edge);
            }

            this.controller.getGraph().endUpdate();
            this.controller.getGraph().layout(new MapGridLayout(cm, sm, carM, sCarM));

            Stage stage = (Stage) showRouteButton.getScene().getWindow();
            stage.close();
        }
    }
}
