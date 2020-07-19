package desktopApplication.components.map.car;

import desktopApplication.components.map.NodesManager;
import desktopApplication.components.map.station.StationNode;

import java.util.function.BiFunction;

public class CarManager extends NodesManager<CarNode>
{
    public CarManager(BiFunction<Integer, Integer, CarNode> factory) {
        super(factory);
    }
}
