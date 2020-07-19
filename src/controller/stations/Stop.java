package controller.stations;

import controller.passenger.Passenger;

import java.util.ArrayList;
import java.util.List;

public class Stop
{
    private String stationName;
    private List<StopDetails> stopDetails = new ArrayList<>();

    public Stop() {}

    public Stop(String stationName, Passenger passenger, StopDetails.Status status, int day)
    {
        this.stationName = stationName;        ;
        addStopDetails(passenger, status, day);
    }

    public String getStationName() {
        return stationName;
    }

    public List<StopDetails> getStopDetails() {
        return stopDetails;
    }

    public void addStopDetails (Passenger passenger, StopDetails.Status status, int day)
    {
        this.stopDetails.add(new StopDetails(passenger,status, day));
    }
}
