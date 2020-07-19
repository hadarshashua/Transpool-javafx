package controller.trips;

import controller.feedback.Feedbacks;
import controller.map.MapDescriptor;
import controller.passenger.Passenger;
import controller.stations.Stop;
import controller.stations.StopDetails;
import controller.time.Time;

import java.util.ArrayList;
import java.util.List;

public class Trip extends TransPoolTrip
{
    private static int numOfTrips = 1;
    private final int MINUTES_IN_HOUR = 60;


    private int remainPlaces, id, dayArrive;
    private double allTripFuelConsumption;
    private Time hourArrive;
    private List<Passenger> allPassengersInTrip = new ArrayList<>();
    private List<Stop> stops = new ArrayList<>();
    private Feedbacks feedbacks;

    public Trip() { }

    public Trip(String owner, String path, int capacity, int ppk, int hour, int minutes, int dayStart, String recurrences, MapDescriptor mapDescriptor)
    {
        super( owner,  path,  capacity,  ppk,  hour, minutes, dayStart, recurrences, mapDescriptor);

        this.id = numOfTrips++;
        this.dayArrive = dayStart;
        this.hourArrive = new Time();
        calculateHourArrive();
        this.remainPlaces = capacity;
        calculateAllTripFuelConsumption();
        this.feedbacks = new Feedbacks();
    }

    public Trip(Trip other)
    {
        super( other.getOwner(),  other.getPath(),  other.getCapacity(),  other.getPpk(),  other.getStartTime().getHour(), other.getStartTime().getMinutes(), other.getDayStart(), other.getRecurrences(), other.getMapDescriptor());

        this.id = other.id;
        this.hourArrive = new Time();
        calculateHourArrive();
        this.remainPlaces = other.getCapacity();
        calculateAllTripFuelConsumption();
    }


    public int getId()
    {
        return this.id;
    }

    public Time getHourArrive()
    {
        return this.hourArrive;
    }

    public int getRemainPlaces()
    {
        return this.remainPlaces;
    }

    public int getDayArrive() {
        return dayArrive;
    }

    public double getAllTripFuelConsumption()
    {
        return this.allTripFuelConsumption;
    }

    public List<Passenger> getAllPassengersInTrip()
    {
        return this.allPassengersInTrip;
    }

    public List<Stop> getStops()
    {
        return this.stops;
    }

    public Feedbacks getFeedbacks()
    {
        return this.feedbacks;
    }

    public void setAllPassengersInTrip(List<Passenger> allPassengersInTrip) {
        this.allPassengersInTrip = allPassengersInTrip;
    }

    public void setHourArrive(Time hourArrive) {
        this.hourArrive.setHour(hourArrive.getHour());
        this.hourArrive.setMinutes(hourArrive.getMinutes());
    }

    public void setRemainPlaces(int remainPlaces) {
        this.remainPlaces = remainPlaces;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public void setAllTripFuelConsumption(double allTripFuelConsumption) {
        this.allTripFuelConsumption = allTripFuelConsumption;
    }

    public void calculateHourArrive()
    {
        int hour, minutes;
        double time = 0, saveTime;
        for(int i=0; i<this.getRoute().size(); i++)
            time += ((double)(this.getRoute().get(i).getLength()) / (double)(this.getRoute().get(i).getSpeedLimit()));

        time*=MINUTES_IN_HOUR;
        saveTime = time;

        hour = this.getStartTime().getHour();
        while(time >= MINUTES_IN_HOUR - this.getStartTime().getMinutes())
        {
            hour = addOneHourAndUpdateDayArrive(hour);
            time-=MINUTES_IN_HOUR;
        }
        //hour = this.getStartTime().getHour() + (int)(time / MINUTES_IN_HOUR);

        minutes = roundMinutes((this.getStartTime().getMinutes() + (int)saveTime) % MINUTES_IN_HOUR);

        this.hourArrive.setHour(hour);
        this.hourArrive.setMinutes(minutes);
    }

    private int addOneHourAndUpdateDayArrive(int prevHour)
    {
        int res;
        if(prevHour == 23) //jump to next day
        {
            res = 0;
            this.dayArrive++;
        }
        else
            res = prevHour+1;
        return res;
    }

    public void calculateAllTripFuelConsumption()
    {
        double sumOfFuelConsumption = 0;
        for(int i=0; i<this.getRoute().size(); i++)
        {
            sumOfFuelConsumption += (this.getRoute().get(i).getLength() / this.getRoute().get(i).getFuelConsumption());
        }
        setAllTripFuelConsumption(sumOfFuelConsumption);
    }

    public void addNewPassenger(Passenger passenger)
    {
        this.allPassengersInTrip.add(passenger);
    }

    public void addNewStop(Stop stop)
    {
        this.stops.add(stop);
    }

    public void updateStops(String stationName, Passenger passenger, StopDetails.Status status, int day)
    {
        boolean isNewStop = true;

        for(int i=0; i<this.stops.size() && isNewStop ; i++)
        {
            if(this.stops.get(i).getStationName().equals(stationName))
            {
                this.stops.get(i).addStopDetails(passenger, status, day);
                isNewStop = false;
            }
        }

        if(isNewStop)
            addNewStop(new Stop(stationName, passenger, status, day));
    }
}