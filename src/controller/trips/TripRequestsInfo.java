package controller.trips;

import java.util.List;

public class TripRequestsInfo
{
    private AllTripsInfo allTripsInfo;

    public TripRequestsInfo(AllTripsInfo allTripsInfo)
    {
        this.allTripsInfo = new AllTripsInfo(allTripsInfo);
    }

    public int getId(int index)
    {
        return this.allTripsInfo.getAllTripRequests().get(index).getId();
    }

    public String getName(int index)
    {
        return this.allTripsInfo.getAllTripRequests().get(index).getPassenger().getName();
    }

    public String getStartStation(int index)
    {
        return this.allTripsInfo.getAllTripRequests().get(index).getStartStation();
    }

    public String getEndStation(int index)
    {
        return this.allTripsInfo.getAllTripRequests().get(index).getEndStation();
    }

    public int getHour(int index)
    {
        return this.allTripsInfo.getAllTripRequests().get(index).getSchedual().getTime().getHour();
    }

    public int getMinutes(int index)
    {
        return this.allTripsInfo.getAllTripRequests().get(index).getSchedual().getTime().getMinutes();
    }

    public int getMTRId(int index)
    {
        return this.allTripsInfo.getAllMatchingTripRequests().get(index).getId();
    }

    public String getMTRName(int index)
    {
        return this.allTripsInfo.getAllMatchingTripRequests().get(index).getPassenger().getName();
    }

    public String getMTRStartStation(int index)
    {
        return this.allTripsInfo.getAllMatchingTripRequests().get(index).getStartStation();
    }

    public String getMTREndStation(int index)
    {
        return this.allTripsInfo.getAllMatchingTripRequests().get(index).getEndStation();
    }

    public int getMTRHour(int index)
    {
        return this.allTripsInfo.getAllMatchingTripRequests().get(index).getSchedual().getTime().getHour();
    }

    public int getMTRMinutes(int index)
    {
        return this.allTripsInfo.getAllMatchingTripRequests().get(index).getSchedual().getTime().getMinutes();
    }

//    public int getMTRRideId(int index)
//    {
//        return this.allTripsInfo.getAllMatchingTripRequests().get(index).getMatchedTripId();
//    }

    public List<Integer> getMTRRideId(int index)
    {
        return this.allTripsInfo.getAllMatchingTripRequests().get(index).getAllTripsIds();
    }

//    public String getMTROwnerName(int index)
//    {
//        return this.allTripsInfo.getAllMatchingTripRequests().get(index).getOwnerName();
//    }

    public List<String> getMTROwnerName(int index)
    {
        return this.allTripsInfo.getAllMatchingTripRequests().get(index).getOwnersNames();
    }

    public int getMTRPrice(int index)
    {
        return this.allTripsInfo.getAllMatchingTripRequests().get(index).getPrice();
    }

    public int getMTRArriveHour (int index)
    {
        return this.allTripsInfo.getAllMatchingTripRequests().get(index).getArriveTime().getHour();
    }

    public int getMTRArriveMinutes (int index)
    {
        return this.allTripsInfo.getAllMatchingTripRequests().get(index).getArriveTime().getMinutes();
    }

    public int getMTRFuelConsumption (int index)
    {
        return this.allTripsInfo.getAllMatchingTripRequests().get(index).getFuelConsumptionOfPassenger();
    }
}