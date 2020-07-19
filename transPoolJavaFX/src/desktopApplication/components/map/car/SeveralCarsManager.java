package desktopApplication.components.map.car;

import desktopApplication.components.map.NodesManager;

import java.util.function.BiFunction;

public class SeveralCarsManager extends NodesManager<SeveralCarsNode>
{
    public SeveralCarsManager(BiFunction<Integer, Integer, SeveralCarsNode> factory) {
        super(factory);
    }
}
