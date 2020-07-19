package desktopApplication.components.map.car;

import com.fxgraph.cells.AbstractCell;
import com.fxgraph.graph.Graph;
import controller.trips.Trip;
import desktopApplication.components.main.JavaFXController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;

public class SeveralCarsNode extends AbstractCell {
    private int x;
    private int y;
    private List<Trip> tripsInStation;
    private JavaFXController controller;

    public SeveralCarsNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setController(JavaFXController controller) {
        this.controller = controller;
    }

    public void setTripsInStation(List<Trip> tripsInStation) {
        this.tripsInStation = tripsInStation;
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
            URL url = getClass().getResource("severalCars.fxml");
            fxmlLoader.setLocation(url);
            VBox root = fxmlLoader.load(url.openStream());

            // updates information on the actual node's controller
            SeveralCarsController controller = fxmlLoader.getController();
            controller.setX(x);
            controller.setY(y);
            controller.setController(this.controller);
            controller.setTripsInStation(this.tripsInStation);

            return root;
        } catch (Exception e) {
            e.printStackTrace();
            return new Label("Error when tried to create graphic node !");

        }
    }
}
