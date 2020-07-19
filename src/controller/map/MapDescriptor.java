package controller.map;

import controller.stations.Station;
import controller.trips.Path;

import java.util.ArrayList;
import java.util.List;

public class MapDescriptor
{
    private jaxbSchema.generated.MapDescriptor mapDescriptor;

    private MapBoundries mapBoundries;
    private List<Station> allStations = new ArrayList<>();
    private List<Path> allPaths = new ArrayList<>();

    public MapDescriptor(generatedClasses.MapDescriptor mapDescriptor)
    {
        this.mapBoundries = new MapBoundries(mapDescriptor.getMapBoundries().getLength(), mapDescriptor.getMapBoundries().getWidth());

        for(int i=0; i<mapDescriptor.getStops().getStop().size(); i++)
        {
            int x = mapDescriptor.getStops().getStop().get(i).getX();
            int y = mapDescriptor.getStops().getStop().get(i).getY();
            String name = mapDescriptor.getStops().getStop().get(i).getName();
            Station newStation = new Station(x, y, name);
            this.allStations.add(newStation);
        }

        for(int i=0; i<mapDescriptor.getPaths().getPath().size(); i++)
        {
            String from = mapDescriptor.getPaths().getPath().get(i).getFrom();
            String to = mapDescriptor.getPaths().getPath().get(i).getTo();
            int fuelConsumption = mapDescriptor.getPaths().getPath().get(i).getFuelConsumption();
            int length = mapDescriptor.getPaths().getPath().get(i).getLength();
            int speedLimit = mapDescriptor.getPaths().getPath().get(i).getSpeedLimit();
            boolean isOneWay = mapDescriptor.getPaths().getPath().get(i).isOneWay();
            Path newPath = new Path(from, to, isOneWay, length, fuelConsumption,speedLimit);
            this.allPaths.add(newPath);
        }
    }

    public MapDescriptor(MapDescriptor other)
    {
        this.mapBoundries = new MapBoundries(other.getMapBoundries());

        for(int i=0; i<other.getAllStations().size(); i++)
            this.allStations.add(other.getAllStations().get(i));

        for(int i=0; i<other.getAllPaths().size(); i++)
            this.allPaths.add(other.getAllPaths().get(i));
    }

    public List<Station> getAllStations()
    {
        return allStations;
    }

    public List<Path> getAllPaths() {
        return allPaths;
    }

    public MapBoundries getMapBoundries() {
        return mapBoundries;
    }

    public boolean isStationExists(String station)
    {
        for(int i=0; i<this.allStations.size(); i++)
            if(this.allStations.get(i).getName().trim().equals(station))
                return true;
        return false;
    }

    public boolean isPathExists(String from, String to)
    {
        for(int i=0; i<this.allPaths.size(); i++)
        {
            if(this.allPaths.get(i).getFrom().equals(from) && this.allPaths.get(i).getTo().equals(to))
                return true;
            else if((!this.allPaths.get(i).isOneWay()) && (this.allPaths.get(i).getFrom().equals(to) && this.allPaths.get(i).getTo().equals(from)))
                return true;
        }
        return false;
    }
}
