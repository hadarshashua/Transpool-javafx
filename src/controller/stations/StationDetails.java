package controller.stations;

import controller.trips.Trip;

import java.util.ArrayList;
import java.util.List;

public class StationDetails
{
    private Station station;
    private int numOfCars;
    private List<Trip> tripsInStation;

    public StationDetails(Station station, Trip trip)
    {
        this.station = station;
        this.numOfCars = 1;
        tripsInStation = new ArrayList<>();
        tripsInStation.add(trip);
    }

    public void setNumOfCars(int numOfCars) {
        this.numOfCars = numOfCars;
    }

    public Station getStation() {
        return station;
    }

    public int getNumOfCars() {
        return numOfCars;
    }

    public List<Trip> getTripsInStation() {
        return tripsInStation;
    }

    public void addNewTripToList(Trip trip)
    {
        tripsInStation.add(trip);
        this.numOfCars++;
    }

}
