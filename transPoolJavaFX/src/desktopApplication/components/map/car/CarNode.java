package desktopApplication.components.map.car;

import com.fxgraph.cells.AbstractCell;
import com.fxgraph.graph.Graph;
import controller.trips.Trip;
import desktopApplication.components.main.JavaFXController;
import desktopApplication.components.map.station.StationController;
import desktopApplication.components.map.stationDetails.StationDetailsDTO;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.function.Supplier;

public class CarNode extends AbstractCell {

    private int x;
    private int y;
    private String name;
    private Trip trip;
    private JavaFXController controller;

    public CarNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setController(JavaFXController controller) {
        this.controller = controller;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    /*
    Creates the graphical representation of the station.
     */
    public Region getGraphic(Graph graph) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("carIcon.fxml");
            fxmlLoader.setLocation(url);
            VBox root = fxmlLoader.load(url.openStream());

            // updates information on the actual node's controller
            CarIconController controller = fxmlLoader.getController();
            controller.setX(x);
            controller.setY(y);
            controller.setName(name);
            controller.setController(this.controller);
            controller.setTrip(this.trip);

            return root;
        } catch (Exception e) {
            e.printStackTrace();
            return new Label("Error when tried to create graphic node !");

        }
    }
}
