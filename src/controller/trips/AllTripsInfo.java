package controller.trips;

import controller.map.MapDescriptor;
import controller.passenger.Passenger;
import jaxbSchema.generated.PlannedTrips;

import java.util.ArrayList;
import java.util.List;

public class AllTripsInfo
{
    private PlannedTrips plannedTrips;

    private List<Trip> allOfferdTrips = new ArrayList<>(); //all planned trips in system
    private List<TripRequest> allTripRequests = new ArrayList<>(); //all trip requests in system which havent matched yet
    private List<MatchedTrip> allMatchingTripRequests = new ArrayList<>(); //all matched trip requests in system
    private List<Passenger> allPassengers = new ArrayList<>(); // all passengers in system

    public AllTripsInfo(generatedClasses.PlannedTrips plannedTrips, MapDescriptor mapDescriptor)
    {
        List<Trip> allTrips = new ArrayList<>();
        int minutes;

        for(int i=0; i<plannedTrips.getTransPoolTrip().size(); i++)
        {
            String owner = plannedTrips.getTransPoolTrip().get(i).getOwner();
            int capacity = plannedTrips.getTransPoolTrip().get(i).getCapacity();
            int ppk = plannedTrips.getTransPoolTrip().get(i).getPPK();
            String path = plannedTrips.getTransPoolTrip().get(i).getRoute().getPath();
            int hour = plannedTrips.getTransPoolTrip().get(i).getScheduling().getHourStart();
            if(plannedTrips.getTransPoolTrip().get(i).getScheduling().getMinuteStart() == null)
                minutes = 0;
            else
                minutes = plannedTrips.getTransPoolTrip().get(i).getScheduling().getMinuteStart();
            int dayStart = plannedTrips.getTransPoolTrip().get(i).getScheduling().getDayStart();
            String recurrences = plannedTrips.getTransPoolTrip().get(i).getScheduling().getRecurrences();
            Trip newTrip = new Trip(owner, path, capacity, ppk, hour, minutes,  dayStart, recurrences, mapDescriptor);
            allTrips.add(newTrip);
        }

        this.allOfferdTrips.addAll(allTrips);
    }

    public AllTripsInfo(AllTripsInfo other)
    {
        for(int i=0; i<other.getAllOfferdTrips().size(); i++)
            this.allOfferdTrips.add(other.getAllOfferdTrips().get(i));

        for(int i=0; i<other.getAllMatchingTripRequests().size(); i++)
            this.allMatchingTripRequests.add(other.getAllMatchingTripRequests().get(i));

        for(int i=0; i<other.getAllTripRequests().size(); i++)
            this.allTripRequests.add(other.getAllTripRequests().get(i));

        for(int i=0; i<other.getAllPassengers().size(); i++)
            this.allPassengers.add(other.getAllPassengers().get(i));
    }

    public List<Trip> getAllOfferdTrips()
    {
        return this.allOfferdTrips;
    }

    public List<MatchedTrip> getAllMatchingTripRequests() {
        return allMatchingTripRequests;
    }

    public List<TripRequest> getAllTripRequests() {
        return allTripRequests;
    }

    public List<Passenger> getAllPassengers() {
        return allPassengers;
    }

    public void addNewTripRequest(TripRequest newTrip)
    {
        this.allTripRequests.add(newTrip);
    }

    public void addNewMatchedTrip(MatchedTrip newMatchedTrip)
    {
        this.allMatchingTripRequests.add(newMatchedTrip);
    }

    public void addNewPassenger(Passenger newPassenger)
    {
        this.allPassengers.add(newPassenger);
    }

    public void addNewOfferedTrip(Trip newTrip)
    {
        this.getAllOfferdTrips().add(newTrip);
    }

//    public Passenger searchForCertainPassenger(String name)
//    {
//        for(int i=0; i<this.allPassengers.size(); i++)
//        {
//            if(this.allPassengers.get(i).getName().equals(name))
//                return this.allPassengers.get(i);
//        }
//        return null;
//    }
}


