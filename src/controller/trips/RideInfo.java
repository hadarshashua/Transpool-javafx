package controller.trips;

import controller.passenger.Passenger;
import controller.stations.Stop;

import java.util.List;

public class RideInfo
{
    private AllTripsInfo allTripsInfo;

    public RideInfo(AllTripsInfo allTripsInfo)
    {
        this.allTripsInfo = new AllTripsInfo(allTripsInfo);
    }

    public int getId(int index)
    {
        return this.allTripsInfo.getAllOfferdTrips().get(index).getId();
    }

    public String getOwner(int index)
    {
        return this.allTripsInfo.getAllOfferdTrips().get(index).getOwner();
    }

    public String getPath(int index)
    {
        return this.allTripsInfo.getAllOfferdTrips().get(index).getPath();
    }

    public int getTripPrice(int index)
    {
        int tripPrice = 0;
        for(int i=0; i<this.allTripsInfo.getAllOfferdTrips().get(index).getRoute().size(); i++)
            tripPrice += this.allTripsInfo.getAllOfferdTrips().get(index).getPpk() * this.allTripsInfo.getAllOfferdTrips().get(index).getRoute().get(i).getLength();
        return tripPrice;
    }

//    public int getHourStart(int index)
//    {
//        return this.allTripsInfo.getAllOfferdTrips().get(index).getHourStart();
//    }

    public int getHourArrive(int index)
    {
        return this.allTripsInfo.getAllOfferdTrips().get(index).getHourArrive().getHour();
    }

    public int getMinutesArrive(int index)
    {
        return this.allTripsInfo.getAllOfferdTrips().get(index).getHourArrive().getMinutes();
    }

    public int getRemainPlaces(int index)
    {
        return this.allTripsInfo.getAllOfferdTrips().get(index).getRemainPlaces();
    }

    public List<Passenger> getAllPassengersInTrip(int index)
    {
        return this.allTripsInfo.getAllOfferdTrips().get(index).getAllPassengersInTrip();
    }

    public List<Stop> getStops(int index)
    {
        return this.allTripsInfo.getAllOfferdTrips().get(index).getStops();
    }

    public double getAllTripFuelConsumption(int index)
    {
        return this.allTripsInfo.getAllOfferdTrips().get(index).getAllTripFuelConsumption();
    }

}