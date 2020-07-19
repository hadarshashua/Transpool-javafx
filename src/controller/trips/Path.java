package controller.trips;

import controller.time.Time;

public class Path
{
    private String from, to;
    private boolean oneWay;
    private int length, fuelConsumption, speedLimit, stationToArriveDay, remainPlaces;
    private Time stationToArriveHour; //the time we arrive to last station of the path


    public Path() { }

    public Path(String from, String to, boolean oneWay, int length, int fuelConsumption, int speedLimit)
    {
        this.from =from;
        this.to = to;
        this.oneWay = oneWay;
        this.length =length;
        this.fuelConsumption = fuelConsumption;
        this.speedLimit = speedLimit;
        this.stationToArriveHour = new Time();
    }

    public Path(Path other)
    {
        this.from =other.from;
        this.to = other.to;
        this.oneWay = other.oneWay;
        this.length =other.length;
        this.fuelConsumption = other.fuelConsumption;
        this.speedLimit = other.speedLimit;
        this.remainPlaces = other.remainPlaces;
        this.stationToArriveHour = new Time(other.stationToArriveHour);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getLength() {
        return length;
    }

    public int getFuelConsumption() {
        return fuelConsumption;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public int getRemainPlaces() {
        return remainPlaces;
    }

    public Time getStationToArriveHour() {
        return stationToArriveHour;
    }

    public int getStationToArriveDay() {
        return stationToArriveDay;
    }

    public boolean isOneWay() {
        return oneWay;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setFuelConsumption(int fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    public void setStationToArriveHour(Time stationToArriveHour) {
        this.stationToArriveHour = stationToArriveHour;
    }

    public void setStationToArriveDay(int stationToArriveDay) {
        this.stationToArriveDay = stationToArriveDay;
    }

    public void setRemainPlaces(int remainPlaces) {
        this.remainPlaces = remainPlaces;
    }
}
