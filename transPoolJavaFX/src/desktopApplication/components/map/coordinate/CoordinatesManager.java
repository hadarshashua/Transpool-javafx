package desktopApplication.components.map.coordinate;

import desktopApplication.components.map.NodesManager;

import java.util.function.BiFunction;

/*
Makes sure that each coordinate node will e created exactly once
 */
public class CoordinatesManager extends NodesManager<CoordinateNode> {

    public CoordinatesManager(BiFunction<Integer, Integer, CoordinateNode> factory) {
        super(factory);
    }
}