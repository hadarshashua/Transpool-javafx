package controller.stations;

import controller.passenger.Passenger;

public class StopDetails
{
    public enum Status {GET_ON, GET_OFF}

    int day;
    Passenger passenger;
    Status status;

    public StopDetails (Passenger passenger, Status status, int day)
    {
        this.passenger = new Passenger(passenger);
        this.status = status;
        this.day = day;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Status getStatus() {
        return status;
    }

    public int getDay() {
        return day;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
