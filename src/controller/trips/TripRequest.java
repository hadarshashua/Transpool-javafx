package controller.trips;

import controller.passenger.Passenger;
import controller.time.Schedual;
import controller.time.Time;

public class TripRequest
{
    private static int numOfTripRequests = 1;
    private int id;
    private String startStation, endStation;
    private Schedual schedual;
    Passenger passenger;

    public TripRequest() { }

    public TripRequest(String name, String startStation, String endStation, int hour, int minutes, int day, Schedual.StartOrArrive startOrArrive)
    {
        this.id = numOfTripRequests;
        numOfTripRequests++;
        this.passenger = new Passenger(name);
        this.startStation = startStation;
        this.endStation = endStation;
        this.schedual = new Schedual(hour, minutes, day, startOrArrive);
    }

    public TripRequest(TripRequest other)
    {
        this.id = other.id;
        this.passenger = new Passenger(other.passenger);
        this.startStation = other.startStation;
        this.endStation = other.endStation;
        this.schedual = new Schedual(other.schedual);
    }

    public int getId() {
        return id;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public Schedual getSchedual() {
        return schedual;
    }
}
