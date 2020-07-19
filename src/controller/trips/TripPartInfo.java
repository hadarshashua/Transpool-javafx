package controller.trips;

import controller.time.Time;

public class TripPartInfo {
    private String firstStation, lastStation, owner;
    private int fuelConsumption, price, arriveDay;
    private Time arriveTime;

    public TripPartInfo(String firstStation, String lastStation, String owner, int fuelConsumption, int price, int arriveDay, Time arriveTime)
    {
        this.firstStation = firstStation;
        this.lastStation = lastStation;
        this.owner = owner;
        this.fuelConsumption = fuelConsumption;
        this.price = price;
        this.arriveDay = arriveDay;
        this.arriveTime = new Time(arriveTime);
    }

    public TripPartInfo(TripPartInfo other)
    {
        this.firstStation = other.firstStation;
        this.lastStation = other.lastStation;
        this.owner = other.owner;
        this.fuelConsumption = other.fuelConsumption;
        this.price = other.price;
        this.arriveDay = other.arriveDay;
        this.arriveTime = new Time(other.arriveTime);
    }

    public String getFirstStation() {
        return firstStation;
    }

    public String getLastStation() {
        return lastStation;
    }

    public String getOwner() {
        return owner;
    }

    public int getFuelConsumption() {
        return fuelConsumption;
    }

    public int getPrice() {
        return price;
    }

    public int getArriveDay() {
        return arriveDay;
    }

    public Time getArriveTime() {
        return arriveTime;
    }

}
