package desktopApplication.components.map.car;

import animatefx.animation.Pulse;
import animatefx.animation.Swing;
import com.fxgraph.graph.Model;
import controller.stations.Station;
import controller.trips.Path;
import controller.trips.Trip;
import desktopApplication.components.main.JavaFXController;
import desktopApplication.components.map.coordinate.CoordinatesManager;
import desktopApplication.components.map.layout.MapGridLayout;
import desktopApplication.components.map.road.ArrowedEdge;
import desktopApplication.components.map.road.BoldArrowedEdge;
import desktopApplication.components.map.station.StationManager;
import desktopApplication.components.map.stationDetails.StationDetailsDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.util.function.Supplier;

public class CarIconController {

    @FXML private VBox carIcon;

    private String name;
    private int x;
    private int y;
    private Trip trip;
    private JavaFXController controller;
    private boolean isRouteShown;

    public CarIconController()
    {
        isRouteShown = false;
    }

    @FXML
    private void initialize()
    {

    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setName(String ownerName) {
        this.name = ownerName;
        Tooltip tooltip = new Tooltip(ownerName);
        Tooltip.install(carIcon, tooltip);
    }

    public void setRouteShown(boolean routeShown) {
        isRouteShown = routeShown;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public void setController(JavaFXController controller) {
        this.controller = controller;
    }

    @FXML
    void showTripRoute(MouseEvent event)
    {
        if(!isRouteShown)
        {
            this.isRouteShown = true;

            this.controller.getGraph().beginUpdate();

            StationManager sm = this.controller.createStations(this.controller.getGraph().getModel());
            CoordinatesManager cm = this.controller.createCoordinates(this.controller.getGraph().getModel());
            this.controller.createEdges(this.controller.getGraph().getModel(), this.controller.getCm());
            CarManager carM = this.controller.putCarIconInRellevantStation(this.controller.getGraph().getModel());
            SeveralCarsManager sCarM = this.controller.putSeveralCarsIconInRellevantStation(this.controller.getGraph().getModel());

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

        }

        else
        {
            this.isRouteShown = false;

            this.controller.getGraph().beginUpdate();

            StationManager sm = this.controller.createStations(this.controller.getGraph().getModel());
            CoordinatesManager cm = this.controller.createCoordinates(this.controller.getGraph().getModel());
            this.controller.createEdges(this.controller.getGraph().getModel(), this.controller.getCm());
            CarManager carM = this.controller.putCarIconInRellevantStation(this.controller.getGraph().getModel());
            SeveralCarsManager sCarM = this.controller.putSeveralCarsIconInRellevantStation(this.controller.getGraph().getModel());

            this.controller.getGraph().endUpdate();
            this.controller.getGraph().layout(new MapGridLayout(cm, sm, carM, sCarM));
        }
    }

}
