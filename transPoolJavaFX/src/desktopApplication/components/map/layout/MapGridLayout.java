package desktopApplication.components.map.layout;

import com.fxgraph.graph.Graph;
import com.fxgraph.layout.Layout;
import desktopApplication.components.map.car.CarManager;
import desktopApplication.components.map.car.SeveralCarsManager;
import desktopApplication.components.map.coordinate.CoordinatesManager;
import desktopApplication.components.map.station.StationManager;


public class MapGridLayout implements Layout {

    private final int SCALE = 70;
    private CoordinatesManager coordinatesManager;
    private StationManager stationManager;
    private CarManager carManager;
    private SeveralCarsManager severalCarsManager;

    public MapGridLayout(CoordinatesManager coordinatesManager, StationManager stationManager, CarManager carManager, SeveralCarsManager severalCarsManager) {
        this.coordinatesManager = coordinatesManager;
        this.stationManager = stationManager;
        this.carManager = carManager;
        this.severalCarsManager = severalCarsManager;
    }

    @Override
    public void execute(Graph graph) {

        coordinatesManager.getAllCoordinates().forEach(coordinateNode ->
                graph
                        .getGraphic(coordinateNode)
                        .relocate(coordinateNode.getX() * SCALE, coordinateNode.getY() * SCALE)
        );

        final int STATION_FIX_X = -15;
        final int STATION_FIX_Y = -15;
        stationManager.getAllCoordinates().forEach(stationNode -> {
            int x = stationNode.getX();
            int y = stationNode.getY();
            graph.getGraphic(stationNode).relocate(x * SCALE + STATION_FIX_X, y * SCALE + STATION_FIX_Y);
        });

        final int CAR_FIX_X = -15;
        final int CAR_FIX_Y = -25;
        carManager.getAllCoordinates().forEach(carNode -> {
            int x = carNode.getX();
            int y = carNode.getY();
            graph.getGraphic(carNode).relocate(x * SCALE + CAR_FIX_X, y * SCALE + CAR_FIX_Y);

        });

        final int SEVERAL_CARS_FIX_X = -15;
        final int SEVERAL_CARS_FIX_Y = -35;
        severalCarsManager.getAllCoordinates().forEach(severalCarsNode -> {
            int x = severalCarsNode.getX();
            int y = severalCarsNode.getY();
            graph.getGraphic(severalCarsNode).relocate(x * SCALE + SEVERAL_CARS_FIX_X, y * SCALE + SEVERAL_CARS_FIX_Y);

        });

    }

}
