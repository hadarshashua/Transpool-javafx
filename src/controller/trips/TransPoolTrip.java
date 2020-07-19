package controller.trips;


import controller.map.MapDescriptor;
import controller.time.Time;

import java.util.ArrayList;
import java.util.List;

public class TransPoolTrip
{
    private final int MINUTES_IN_HOUR = 60;

    private MapDescriptor mapDescriptor;
    private String owner, path, recurrences;
    private int capacity, ppk, dayStart ;
    private List<Path> route = new ArrayList<>();
    private Time startTime;


    public TransPoolTrip() { }

    public TransPoolTrip(String owner, String path, int capacity, int ppk, int hour, int minutes, int dayStart, String recurrences, MapDescriptor mapDescriptor)
    {
        this.owner = owner;
        this.path = path;
        this.capacity = capacity;
        this.ppk = ppk;
        this.dayStart = dayStart;
        this.recurrences = recurrences;
        this.startTime = new Time(hour, minutes);

        this.mapDescriptor = new MapDescriptor(mapDescriptor);
        buildRoute();
    }

    public TransPoolTrip(TransPoolTrip other)
    {
        this.owner = other.owner;
        this.path = other.path;
        this.capacity = other.capacity;
        this.ppk = other.ppk;
        this.dayStart = other.dayStart;
        this.recurrences = other.recurrences;
        this.startTime = new Time(other.startTime.getHour(), other.startTime.getMinutes());

        this.mapDescriptor = new MapDescriptor(other.mapDescriptor);
        buildRoute();
    }

    public String getOwner()
    {
        return this.owner;
    }

    public String getPath()
    {
        return path;
    }

    public int getCapacity()
    {
        return capacity;
    }

    public int getPpk()
    {
        return ppk;
    }

    public int getDayStart() {
        return dayStart;
    }

    public String getRecurrences() {
        return recurrences;
    }

    public Time getStartTime() {
        return startTime;
    }

    public List<Path> getRoute()
    {
        return route;
    }

    public MapDescriptor getMapDescriptor() {
        return mapDescriptor;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPpk(int ppk) {
        this.ppk = ppk;
    }

    public void setRoute(List<Path> route) {
        for(int i=0; i<route.size(); i++)
            this.route.add(route.get(i));
    }

    public void buildRoute()
    {
        String[] allStationsNames =  path.split(",");
        Time stationArriveTime = new Time(this.startTime);

        for(int i=0; i<allStationsNames.length-1; i++)
        {
            allStationsNames[i] = allStationsNames[i].trim();
            allStationsNames[i+1] = allStationsNames[i+1].trim();
            this.route.add(new Path(searchForCertainPath(allStationsNames[i], allStationsNames[i+1])));
            this.route.get(this.route.size()-1).setRemainPlaces(this.capacity);
        }

        for(int i=0; i<this.route.size(); i++)
        {
            stationArriveTime = new Time(calculateHourArriveToLastStationOfPath(this.route.get(i), stationArriveTime));
        }
    }

    public Time calculateHourArriveToLastStationOfPath(Path path, Time stationArriveTime)
    {
        double time = 0;
        int hour, minutes;

        time += ((double)(path.getLength()) / (double)(path.getSpeedLimit()));

        time*=60;

        hour = stationArriveTime.getHour();
        while(time >= MINUTES_IN_HOUR - stationArriveTime.getMinutes())
        {
            hour = addOneHour(hour);
            time-=MINUTES_IN_HOUR;
        }
        //hour = stationArriveTime.getHour() + (int)(time / 60);
        minutes = roundMinutes((stationArriveTime.getMinutes() + (int)time) % 60);

//        if(minutes >= 60) {
//            hour++;
//            minutes-=60;
//        }

        stationArriveTime.setHour(hour);
        stationArriveTime.setMinutes(minutes);
        path.setStationToArriveHour(stationArriveTime);

        return stationArriveTime;
    }

    private int addOneHour(int prevHour)
    {
        int res;
        if(prevHour == 23) //jump to next day
        {
            res = 0;
        }
        else
            res = prevHour+1;
        return res;
    }

    public int roundMinutes(int minutes)
    {
        if(minutes % 5 == 1)
            return minutes-1;
        else if(minutes % 5 == 2)
            return minutes-2;
        else if(minutes % 5 == 3)
            return minutes+2;
        else if(minutes % 5 == 4)
            return minutes+1;
        else //minutes % 5 =0
            return minutes;
    }

    public Path searchForCertainPath(String from, String to)
    {
        for(int i=0; i<this.mapDescriptor.getAllPaths().size(); i++)
        {
            if(this.mapDescriptor.getAllPaths().get(i).getFrom().trim().equals(from) && this.mapDescriptor.getAllPaths().get(i).getTo().trim().equals(to))
                return this.mapDescriptor.getAllPaths().get(i);
            else if(!this.mapDescriptor.getAllPaths().get(i).isOneWay())
                if(this.mapDescriptor.getAllPaths().get(i).getFrom().trim().equals(to) && this.mapDescriptor.getAllPaths().get(i).getTo().trim().equals(from))
                    return this.mapDescriptor.getAllPaths().get(i);
        }
        return null;
    }
}
