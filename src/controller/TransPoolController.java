package controller;

import controller.map.MapDescriptor;
import controller.trips.AllTripsInfo;

public class TransPoolController {
    jaxbSchema.generated.TransPool transPool;
    private MapDescriptor mapDescriptor;
    private AllTripsInfo allTripsInfo;

    public TransPoolController(generatedClasses.TransPool transPool)
    {
        this.mapDescriptor = new MapDescriptor(transPool.getMapDescriptor());
        this.allTripsInfo = new AllTripsInfo(transPool.getPlannedTrips(), this.mapDescriptor);
    }

    public AllTripsInfo getAllTripsInfo() {
        return allTripsInfo;
    }

    public MapDescriptor getMapDescriptor() {
        return mapDescriptor;
    }
}
