package controller;

import controller.exceptions.*;
import controller.exceptions.NullPointerException;
import controller.map.MapDescriptor;
import controller.passenger.Passenger;
import controller.stations.Station;
import controller.stations.StopDetails;
import controller.time.Schedual;
import controller.time.Time;
import controller.trips.*;
import desktopApplication.components.main.JavaFXController;
import desktopApplication.components.main.LoadXMLController;
import javafx.concurrent.Task;
import jaxbSchema.generated.TransPool;
import tasks.LoadXMLTask;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainController
{
    TransPoolController transPool;
    private TripRequestsInfo tripRequestsInfo;
    private RideInfo rideInfo;
    private Task<Boolean> currentRunningTask;
    //private JavaFXController controller;

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxbSchema.generated";
    private String fileType = "xml";
    private final int MIN_SIZE_OF_MAP = 6;
    private final int MAX_SIZE_OF_MAP = 100;
    private final int ZERO = 0;

//    public MainController(JavaFXController controller) {
//        this.controller = controller;
//    }
    public MainController() {
    }

    public TransPoolController getTransPool() {
        return transPool;
    }

    public TripRequestsInfo getTripRequestsInfo() {
        return this.tripRequestsInfo;
    }

    public RideInfo getRideInfo() {
        return this.rideInfo;
    }

//    public JavaFXController getController() {
//        return controller;
//    }

    public void clearSystem()
    {
        this.getTransPool().getAllTripsInfo().getAllTripRequests().clear();
        this.getTransPool().getAllTripsInfo().getAllOfferdTrips().clear();
        this.getTransPool().getAllTripsInfo().getAllMatchingTripRequests().clear();
    }

    public void loadXMLFile(Task<Boolean> task)
    {
//        currentRunningTask = new LoadXMLTask(file, this);
//        controller.bindTaskToUIComponents(currentRunningTask, loadXMLController);
        new Thread(task).start();
    }

//    public void buildMap()
//    {
//        this.controller.createMap(this);
//    }

    public void loadInfoFromXML(File file) throws IllegalFileExtensionException, FileNotExistsException, SameStationsLocationException, SameStationsNamesException, IllegalMapSizeException, IllegalStationsLocationException, IllegalOfferedTripException, IllegalPathException, JAXBException, DayStartNotExistsException, DayStartNotValidException {

        isValidFile(file);
        JAXBContext jaxbContext = JAXBContext.newInstance(generatedClasses.TransPool.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        generatedClasses.TransPool transPool = (generatedClasses.TransPool) jaxbUnmarshaller.unmarshal(file);

        validateData(transPool);

        this.transPool = new controller.TransPoolController(transPool);

    }

    private void isValidFile(File file) throws FileNotExistsException, IllegalFileExtensionException {
        if (!file.exists())
        {
            throw new FileNotExistsException();
        }
        if (!isFileExtensionIsXML(file.getName()))
        {
            throw new IllegalFileExtensionException();
        }
    }

    private boolean isFileExtensionIsXML(String fileName)
    {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1).toUpperCase();
        return fileExtension.equals("XML");
    }

    private static generatedClasses.TransPool deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (generatedClasses.TransPool) u.unmarshal(in);
    }

    private void validateData(generatedClasses.TransPool transPool) throws IllegalMapSizeException, SameStationsNamesException, IllegalStationsLocationException, SameStationsLocationException, IllegalPathException, IllegalOfferedTripException, DayStartNotExistsException, DayStartNotValidException {
        checkMapSize(transPool);
        differentStationsNames(transPool);
        isAllStationsInMapBaundries(transPool);
        differentStationsPlaceInMap(transPool);
        allPathsAreLegal(transPool);
        allOfferedTripsAreLegal(transPool);
        checkIfDayStartExists(transPool);
        checkIfDayStartLegal(transPool);
    }

    public void checkMapSize(generatedClasses.TransPool transPool) throws IllegalMapSizeException {
        if(transPool.getMapDescriptor().getMapBoundries().getLength() < MIN_SIZE_OF_MAP || transPool.getMapDescriptor().getMapBoundries().getLength() > MAX_SIZE_OF_MAP)
            throw new IllegalMapSizeException();
        if(transPool.getMapDescriptor().getMapBoundries().getWidth() < MIN_SIZE_OF_MAP || transPool.getMapDescriptor().getMapBoundries().getWidth() > MAX_SIZE_OF_MAP)
            throw new IllegalMapSizeException();
    }

    public void differentStationsNames(generatedClasses.TransPool transPool) throws SameStationsNamesException {
        String stationName;
        for(int i=0; i<transPool.getMapDescriptor().getStops().getStop().size(); i++) {
            stationName = transPool.getMapDescriptor().getStops().getStop().get(i).getName().trim();
            for(int j=i+1; j<transPool.getMapDescriptor().getStops().getStop().size(); j++)
                if(transPool.getMapDescriptor().getStops().getStop().get(j).getName().trim().equals(stationName))
                    throw new SameStationsNamesException();
        }
    }

    public void isAllStationsInMapBaundries(generatedClasses.TransPool transPool) throws IllegalStationsLocationException {
        int length, width;
        length = transPool.getMapDescriptor().getMapBoundries().getLength();
        width = transPool.getMapDescriptor().getMapBoundries().getWidth();

        for(int i=0; i<transPool.getMapDescriptor().getStops().getStop().size(); i++) {
            if(transPool.getMapDescriptor().getStops().getStop().get(i).getX() < ZERO || transPool.getMapDescriptor().getStops().getStop().get(i).getX() > width)
                throw new IllegalStationsLocationException();
            if(transPool.getMapDescriptor().getStops().getStop().get(i).getY() < ZERO || transPool.getMapDescriptor().getStops().getStop().get(i).getY() > length)
                throw new IllegalStationsLocationException();
        }
    }

    public void differentStationsPlaceInMap(generatedClasses.TransPool transPool) throws SameStationsLocationException {
        int x,y;
        for(int i=0; i<transPool.getMapDescriptor().getStops().getStop().size(); i++) {
            x = transPool.getMapDescriptor().getStops().getStop().get(i).getX();
            y = transPool.getMapDescriptor().getStops().getStop().get(i).getY();

            for(int j=i+1; j<transPool.getMapDescriptor().getStops().getStop().size(); j++)
                if(transPool.getMapDescriptor().getStops().getStop().get(j).getX() == x && transPool.getMapDescriptor().getStops().getStop().get(j).getY() == y)
                    throw new SameStationsLocationException();
        }
    }

    public void allPathsAreLegal(generatedClasses.TransPool transPool) throws IllegalPathException {
        String from, to;
        for(int i=0; i<transPool.getMapDescriptor().getPaths().getPath().size(); i++)
        {
            from = transPool.getMapDescriptor().getPaths().getPath().get(i).getFrom();
            to = transPool.getMapDescriptor().getPaths().getPath().get(i).getTo();
            from.trim();
            to.trim();
            if(!isStationExist(transPool, from) || !isStationExist(transPool, to))
                throw new IllegalPathException();
        }
    }

    public void allOfferedTripsAreLegal(generatedClasses.TransPool transPool) throws IllegalOfferedTripException {
        for(int i=0; i<transPool.getPlannedTrips().getTransPoolTrip().size(); i++)
        {
            String[] allStationsNames = transPool.getPlannedTrips().getTransPoolTrip().get(i).getRoute().getPath().split(",");
            for(int j=0; j<allStationsNames.length-1; j++) {
                allStationsNames[j] = allStationsNames[j].trim();
                allStationsNames[j+1] = allStationsNames[j+1].trim();
                if (!isPathLegal(transPool, allStationsNames[j], allStationsNames[j + 1]))
                    throw new IllegalOfferedTripException();
            }
        }
    }

    public boolean isPathLegal(generatedClasses.TransPool transPool, String from, String to)
    {
        if(!isStationExist(transPool, from) || !isStationExist(transPool, to))
            return false;
        else if(!isPathExists(transPool, from, to))
            return false;

        return true;
    }

    public boolean isStationExist(generatedClasses.TransPool transPool, String stationName)
    {
        for(int i=0; i<transPool.getMapDescriptor().getStops().getStop().size(); i++)
            if(transPool.getMapDescriptor().getStops().getStop().get(i).getName().trim().equals(stationName))
                return true;
        return false;
    }

    public boolean isPathExists(generatedClasses.TransPool transPool, String from, String to)
    {
        for(int i=0; i<transPool.getMapDescriptor().getPaths().getPath().size(); i++)
        {
            if(transPool.getMapDescriptor().getPaths().getPath().get(i).getFrom().trim().equals(from) && transPool.getMapDescriptor().getPaths().getPath().get(i).getTo().trim().equals(to))
                return true;
            else if(!transPool.getMapDescriptor().getPaths().getPath().get(i).isOneWay())
                if(transPool.getMapDescriptor().getPaths().getPath().get(i).getFrom().trim().equals(to) && transPool.getMapDescriptor().getPaths().getPath().get(i).getTo().trim().equals(from))
                    return true;
        }
        return false;
    }

    public void checkIfDayStartExists(generatedClasses.TransPool transPool) throws DayStartNotExistsException {
        for(int i=0; i<transPool.getPlannedTrips().getTransPoolTrip().size(); i++)
        {
            if(transPool.getPlannedTrips().getTransPoolTrip().get(i).getScheduling().getDayStart() == null)
                throw new DayStartNotExistsException();
        }
    }

    public void checkIfDayStartLegal(generatedClasses.TransPool transPool) throws DayStartNotValidException {
        for(int i=0; i<transPool.getPlannedTrips().getTransPoolTrip().size(); i++)
        {
            if(transPool.getPlannedTrips().getTransPoolTrip().get(i).getScheduling().getDayStart() < 1)
                throw new DayStartNotValidException();
        }
    }

    public void isXMLFileLoaded() throws NullPointerException {
        if (this.transPool == null)
            throw new NullPointerException();
    }

    public List<Station> getStations() {
        return this.transPool.getMapDescriptor().getAllStations();
    }

    public boolean isStationExist(String stationName)
    {
        for(int i=0; i<this.transPool.getMapDescriptor().getAllStations().size(); i++)
            if(this.transPool.getMapDescriptor().getAllStations().get(i).getName().trim().equals(stationName))
                return true;
        return false;
    }

    public boolean checkHour(int hour)
    {
        if(hour < 0 || hour > 23)
            return false;
        return true;
    }

    public boolean checkMinutes(int minutes)
    {
        if(minutes < 0 || minutes > 59)
            return false;

        return true;
    }

    public void makeNewTripRequest(String name, String startStation, String endStation, int hour, int minutes, int day, Schedual.StartOrArrive startOrArrive)
    {
        minutes = roundMinutes(minutes);
        TripRequest newTrip = new TripRequest(name, startStation, endStation, hour, minutes, day, startOrArrive);
        this.transPool.getAllTripsInfo().addNewTripRequest(newTrip);
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

    public void addNewPassenger(String name)
    {
        Passenger newPassenger = new Passenger(name);
        this.transPool.getAllTripsInfo().addNewPassenger(newPassenger);
    }

    public void initializeTripRequestInfo()
    {
        this.tripRequestsInfo = new TripRequestsInfo(this.transPool.getAllTripsInfo());
    }

    public void initializeRideInfo() {
        this.rideInfo = new RideInfo(this.transPool.getAllTripsInfo());
    }

    public List<Trip> getAllOfferdTrips()
    {
        return this.transPool.getAllTripsInfo().getAllOfferdTrips();
    }

    public List<TripRequest> getAllTripRequests() {
        return this.transPool.getAllTripsInfo().getAllTripRequests();
    }

    public List<MatchedTrip> getAllMatchingTripRequests() {
        return this.transPool.getAllTripsInfo().getAllMatchingTripRequests();
    }

    public List<Integer> findPossibleTripDirectOnly(int tripRequestId, int maxOptions)
    {
        int indexOfTripRequest, firstStationIndex , lastStationIndex , countOptions = 0;
        String firstStation, lastStation;
        Time startHourOfTripRequest, hourAtFirstStation;
        TripRequest tripRequest;

        indexOfTripRequest = findTripRequestIndexById(tripRequestId);
        tripRequest = new TripRequest(this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest));

        firstStation = tripRequest.getStartStation();
        lastStation = tripRequest.getEndStation();

        if(tripRequest.getSchedual().getStartOrArrive() == Schedual.StartOrArrive.START)
            return findTripsAccordingToStartTimeDirectOnly(tripRequest, firstStation, lastStation, maxOptions);
        else
            return findTripsAccordingToArriveTimeDirectOnly(tripRequest, firstStation, lastStation, maxOptions);
    }

    private List<Integer> findTripsAccordingToStartTimeDirectOnly(TripRequest tripRequest, String firstStation, String lastStation, int maxOptions)
    {
        int  firstStationIndex , lastStationIndex, dayOfTripRequest, countOptions = 0;
        Time startHourOfTripRequest, hourAtFirstStation;
        List<Integer> res = new ArrayList<>();

        startHourOfTripRequest = new Time(tripRequest.getSchedual().getTime());
        dayOfTripRequest = tripRequest.getSchedual().getDay();

        for(int i=0; i<this.transPool.getAllTripsInfo().getAllOfferdTrips().size() && countOptions < maxOptions; i++)
        {
            firstStationIndex = findStationIndex(firstStation, i);
            lastStationIndex = findStationIndex(lastStation, i);

            if(findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), firstStation) != null) {
                hourAtFirstStation = new Time(findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), firstStation));

                if (firstStationIndex != -1 && lastStationIndex != -1)
                    if (firstStationIndex < lastStationIndex)
                        if(checkIfSameDays(dayOfTripRequest, this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getDayStart(), this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getRecurrences()))
                            if (hourAtFirstStation.getHour() == startHourOfTripRequest.getHour() && hourAtFirstStation.getMinutes() == startHourOfTripRequest.getMinutes())
                                if (this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getRemainPlaces() > 0) {
                                    countOptions++;
                                    res.add(i);
                            }
            }
        }
        return  res;
    }

    private boolean checkIfSameDays(int tripRequestDay, int offeredTripStartDay, String recurrences)
    {
        if(recurrences.equals("One Time"))
            return (tripRequestDay == offeredTripStartDay);
        else if(tripRequestDay >= offeredTripStartDay)
        {
            if(recurrences.equals("Daily"))
                return true;
            else if(recurrences.equals("BiDaily"))
                return ((tripRequestDay-offeredTripStartDay)%2 == 0);
            else if(recurrences.equals("Weekly"))
                return ((tripRequestDay-offeredTripStartDay)%7 == 0);
            else
                return ((tripRequestDay-offeredTripStartDay)%30 == 0);
        }
        else
            return false;
    }

    private List<Integer> findTripsAccordingToArriveTimeDirectOnly(TripRequest tripRequest, String firstStation, String lastStation, int maxOptions)
    {
        int  firstStationIndex , lastStationIndex, dayOfTripRequest, countOptions = 0;
        Time arriveHourOfTripRequest, hourAtLastStation;
        List<Integer> res = new ArrayList<>();

        arriveHourOfTripRequest = new Time(tripRequest.getSchedual().getTime());
        dayOfTripRequest = tripRequest.getSchedual().getDay();

        for(int i=0; i<this.transPool.getAllTripsInfo().getAllOfferdTrips().size() && countOptions < maxOptions; i++)
        {
            firstStationIndex = findStationIndex(firstStation, i);
            lastStationIndex = findStationIndex(lastStation, i);

            if(findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), lastStation) != null) {
                hourAtLastStation = new Time(findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), lastStation));

                if (firstStationIndex != -1 && lastStationIndex != -1)
                    if (firstStationIndex < lastStationIndex)
                        if(checkIfSameDays(dayOfTripRequest, this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getDayStart(), this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getRecurrences()))
                            if (hourAtLastStation.getHour() == arriveHourOfTripRequest.getHour() && hourAtLastStation.getMinutes() == arriveHourOfTripRequest.getMinutes())
                                if (this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getRemainPlaces() > 0) {
                                    countOptions++;
                                    res.add(i);
                                }
            }
        }
        return  res;
    }

    private int findTripRequestIndexById(int id)
    {
        for(int i=0; i<this.transPool.getAllTripsInfo().getAllTripRequests().size(); i++)
        {
            if(this.transPool.getAllTripsInfo().getAllTripRequests().get(i).getId() == id)
                return i;
        }
        return -1;
    }

    public int findStationIndex(String station, int index)
    {
        String[] allStationsNames = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(index).getPath().split(",");

        for (int j = 0; j < allStationsNames.length ; j++) {
            allStationsNames[j] = allStationsNames[j].trim();
            if (allStationsNames[j].equals(station))
                return j;
        }
        return -1; //if not found
    }

    public int findStationIndex(String station, Trip trip)
    {
        String[] allStationsNames = trip.getPath().split(",");

        for (int j = 0; j < allStationsNames.length ; j++) {
            allStationsNames[j] = allStationsNames[j].trim();
            if (allStationsNames[j].equals(station))
                return j;
        }
        return -1; //if not found
    }

    public Time findTimeAtStation(Trip trip, String station)
    {
        String[] allAtationsNames = trip.getPath().split(",");

        int fromStationIndex = findStationIndex(station, trip);

        if(fromStationIndex == 0)
        {
            if(trip.getRoute().get(0).getFrom().trim().equals(station)) //the trip requests starts from the first station of the trip
                return new Time(trip.getStartTime());

            else if(!trip.getRoute().get(0).isOneWay())
                if(trip.getRoute().get(0).getTo().trim().equals(station))
                    return new Time(trip.getStartTime());
        }

        else
        {
            for(int i=0; i< trip.getRoute().size(); i++)
            {
                if(trip.getRoute().get(i).getTo().trim().equals(station))
                    return trip.getRoute().get(i).getStationToArriveHour();

                else if (!trip.getRoute().get(0).isOneWay())
                    if(trip.getRoute().get(i).getFrom().trim().equals(station))
                        return trip.getRoute().get(i).getStationToArriveHour();
            }
        }
        return null;
    }

    public int calculateTripPrice(int idOfTripRequest, int indexOfOfferedTrip)
    {
        String firstStation, lastStation, route;
        int firstStationIndexAtPath, lastStationIndexAtPath, price = 0, length, ppk;
        int indexOfTripRequest = findTripRequestIndexById(idOfTripRequest);

        firstStation = this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest).getStartStation();
        lastStation = this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest).getEndStation();

        firstStationIndexAtPath = findStationIndexAtPath(firstStation, indexOfOfferedTrip);
        lastStationIndexAtPath = findStationIndexAtPath(lastStation, indexOfOfferedTrip);

        route = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip).getPath().substring(firstStationIndexAtPath , (lastStationIndexAtPath + (lastStation.length() == 1 ? 0 : lastStation.length()) + 1));
        String[] allStationsNames = route.split(",");

        for (int i=0; i<allStationsNames.length -1 ; i++)
        {
            allStationsNames[i] = allStationsNames[i].trim();
            allStationsNames[i+1] = allStationsNames[i+1].trim();
            length = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip).searchForCertainPath(allStationsNames[i], allStationsNames[i+1]).getLength();
            ppk = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip).getPpk();
            price += (length * ppk);
        }

        return price;
    }

    public int calculateStartOrArriveHour(int idOfTripRequest, int indexOfOfferedTrip)
    {
        String station;
        int hour, indexOfTripRequest;
        Time time;

        indexOfTripRequest = findTripRequestIndexById(idOfTripRequest);
        if(this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest).getSchedual().getStartOrArrive().equals(Schedual.StartOrArrive.START))
            station = this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest).getEndStation();
        else
            station = this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest).getStartStation();

        time = new Time(findTimeAtFirstOrLastStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip), station));

        hour = time.getHour();
        return hour;
    }

    public int calculateStartOrArriveMinutes(int idOfTripRequest, int indexOfOfferedTrip)
    {
        String station;
        int minutes, indexOfTripRequest;
        Time time;

        indexOfTripRequest = findTripRequestIndexById(idOfTripRequest);

        if(this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest).getSchedual().getStartOrArrive().equals(Schedual.StartOrArrive.START))
            station = this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest).getEndStation();
        else
            station = this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest).getStartStation();

        time = new Time(findTimeAtFirstOrLastStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip), station));

        minutes = time.getMinutes();
        return minutes;
    }

    private Time findTimeAtFirstOrLastStation(Trip trip, String station)
    {
        for(int i=0; i< trip.getRoute().size(); i++)
        {
            if(trip.getRoute().get(i).getTo().trim().equals(station))
                return trip.getRoute().get(i).getStationToArriveHour();

            else if (!trip.getRoute().get(0).isOneWay())
                if(trip.getRoute().get(i).getFrom().trim().equals(station))
                    return trip.getRoute().get(i).getStationToArriveHour();
        }
        return null;
    }

    public int calculateTripFuelConsumption(int idOfTripRequest, int indexOfOfferedTrip)
    {
        String firstStation, lastStation, route;
        int indexOfTripRequest, firstStationIndexAtPath, lastStationIndexAtPath, length, fuelConsumption, sumOfFuelConsumption = 0;
        Path path;

        indexOfTripRequest = findTripRequestIndexById(idOfTripRequest);

        firstStation = this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest).getStartStation();
        lastStation = this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest).getEndStation();

        firstStationIndexAtPath = findStationIndexAtPath(firstStation, indexOfOfferedTrip);
        lastStationIndexAtPath = findStationIndexAtPath(lastStation, indexOfOfferedTrip);

        route = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip).getPath().substring(firstStationIndexAtPath,  (lastStationIndexAtPath + (lastStation.length() == 1 ? 0 : lastStation.length()) + 1));
        String[] allStationsNames = route.split(",");

        for (int i=0; i<allStationsNames.length -1 ; i++)
        {
            allStationsNames[i] = allStationsNames[i].trim();
            allStationsNames[i+1] = allStationsNames[i+1].trim();
            path = new Path(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip).searchForCertainPath(allStationsNames[i], allStationsNames[i+1]));
            length = path.getLength();
            fuelConsumption = path.getFuelConsumption();
            sumOfFuelConsumption += length/fuelConsumption;
        }

        return sumOfFuelConsumption;
    }



    public int findStationIndexAtPath(String station, int index)
    {
        int stationIndexAtStringArray = -1, res = 0;
        String[] allStationsNames = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(index).getPath().split(",");

        for (int i = 0; i < allStationsNames.length ; i++) {
            allStationsNames[i] = allStationsNames[i].trim();
            if (allStationsNames[i].equals(station))
                stationIndexAtStringArray = i;
        }

        for (int i = 0; i < stationIndexAtStringArray ; i++) {
        res+= allStationsNames[i].length();
        res++; //add coma index
        }

        return res;
    }

    public int findStationIndexAtStringArray(String station, int index)
    {
        int stationIndexAtStringArray = -1, res = 0;
        String[] allStationsNames = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(index).getPath().split(",");

        for (int i = 0; i < allStationsNames.length ; i++) {
            allStationsNames[i] = allStationsNames[i].trim();
            if (allStationsNames[i].equals(station))
                stationIndexAtStringArray = i;
        }

        return stationIndexAtStringArray;
    }

    public void makeMatch(int idOfTripRequest, int indexOfOfferedTrip, TripPartInfo tripInfo)
    {
        int price, matchedTripId, fuelConsumptionOfPassenger, remainPlaces, indexOfTripRequest;
        String ownerName, startStation, endStation;
        Time arriveTime = new Time();
        MatchedTrip matchedTrip;
        Passenger passenger;

        indexOfTripRequest = findTripRequestIndexById(idOfTripRequest);

        Trip trip = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip);
        List<Path> pathsList = findAllPathsBetweenTwoStations(tripInfo.getFirstStation(), tripInfo.getLastStation(), trip);

        for(int i=0; i<pathsList.size(); i++)
        {
            pathsList.get(i).setRemainPlaces(pathsList.get(i).getRemainPlaces()-1);
        }

//        remainPlaces = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip).getRemainPlaces();
//        remainPlaces--;
//        this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip).setRemainPlaces(remainPlaces);

        passenger = this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest).getPassenger();
        trip.addNewPassenger(passenger);

//        startStation = this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest).getStartStation();
//        endStation = this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest).getEndStation();
        this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip).updateStops(tripInfo.getFirstStation(), passenger, StopDetails.Status.GET_ON, tripInfo.getArriveDay());
        this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip).updateStops(tripInfo.getLastStation(), passenger, StopDetails.Status.GET_OFF, tripInfo.getArriveDay());
    }

    public void finishMatch(int idOfTripRequest, List<TripPartInfo> tripInfo)
    {
        int price = 0, matchedTripId, fuelConsumptionOfPassenger = 0, indexOfTripRequest, indexOfOfferedTrip;
        Time arriveTime = new Time();
        List<String> ownersNames = new ArrayList<>();
        List<Integer> allTripsIds = new ArrayList<>();
        MatchedTrip matchedTrip;

        indexOfTripRequest = findTripRequestIndexById(idOfTripRequest);

        for(int i=0; i<tripInfo.size(); i++)
        {
            price+= tripInfo.get(i).getPrice();
            fuelConsumptionOfPassenger+= tripInfo.get(i).getFuelConsumption();
            ownersNames.add(tripInfo.get(i).getOwner());
            indexOfOfferedTrip = findIndexOfTripByOwnerName(tripInfo.get(i).getOwner());
            allTripsIds.add(this.getAllOfferdTrips().get(indexOfOfferedTrip).getId());
        }

        arriveTime.setHour(tripInfo.get(tripInfo.size()-1).getArriveTime().getHour());
        arriveTime.setMinutes(tripInfo.get(tripInfo.size()-1).getArriveTime().getMinutes());


//        price = calculateTripPrice(idOfTripRequest, indexOfOfferedTrip);
//        fuelConsumptionOfPassenger = calculateTripFuelConsumption(idOfTripRequest, indexOfOfferedTrip);
//        arriveTime.setHour(calculateStartOrArriveHour(idOfTripRequest, indexOfOfferedTrip));
//        arriveTime.setMinutes(calculateStartOrArriveMinutes(idOfTripRequest, indexOfOfferedTrip));
//        matchedTripId = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip).getId();
//        ownerName = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip).getOwner();

        TripRequest tripRequest = new TripRequest(this.transPool.getAllTripsInfo().getAllTripRequests().remove(indexOfTripRequest));
        matchedTrip = new MatchedTrip(tripRequest, allTripsIds, fuelConsumptionOfPassenger, ownersNames, price, arriveTime);
        this.transPool.getAllTripsInfo().addNewMatchedTrip(matchedTrip);
    }

    public int findIndexOfTripByOwnerName(String owner)
    {
        for(int i=0; i<this.transPool.getAllTripsInfo().getAllOfferdTrips().size(); i++)
        {
            if(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getOwner().equals(owner))
                return i;
        }
        return -1;
    }

    public void makeNewOfferedTrip(String owner, String path, int capacity, int ppk, int hour, int minutes, int dayStart, String recurrences, MapDescriptor mapDescriptor)
    {
        Trip newTrip = new Trip(owner, path, capacity, ppk, hour, minutes, dayStart, recurrences, mapDescriptor);
        this.transPool.getAllTripsInfo().addNewOfferedTrip(newTrip);
    }

    public boolean areStationsExists(String path)
    {
        String[]allStationsNames = path.split(",");
        for(int i=0; i<allStationsNames.length; i++)
        {
            allStationsNames[i] = allStationsNames[i].trim();
            if(!this.transPool.getMapDescriptor().isStationExists(allStationsNames[i]))
                return false;
        }
        return  true;
    }

    public boolean arePathsExist(String path)
    {
        String[]allStationsNames = path.split(",");
        for(int i=0; i<allStationsNames.length -1; i++)
        {
            allStationsNames[i] = allStationsNames[i].trim();
            allStationsNames[i+1] = allStationsNames[i+1].trim();
            if(!this.transPool.getMapDescriptor().isPathExists(allStationsNames[i], allStationsNames[i+1]))
                return false;
        }
        return  true;
    }

    public int calculateOfferedTripPrice(int index)
    {
        int tripPrice = 0;
        for(int i=0; i<this.transPool.getAllTripsInfo().getAllOfferdTrips().get(index).getRoute().size(); i++)
            tripPrice += this.transPool.getAllTripsInfo().getAllOfferdTrips().get(index).getPpk() * this.transPool.getAllTripsInfo().getAllOfferdTrips().get(index).getRoute().get(i).getLength();
        return tripPrice;
    }

    public void addFeedbackToOwner(int rate, String feedback, int indexOfOfferedTrip)
    {
        int numOfRates, sumOfRates;
        Trip trip = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(indexOfOfferedTrip);

        numOfRates = trip.getFeedbacks().getNumOfRates();
        sumOfRates = trip.getFeedbacks().getSumOfRates();
        trip.getFeedbacks().setNumOfRates(numOfRates + 1);
        trip.getFeedbacks().setSumOfRates(sumOfRates + rate);
        trip.getFeedbacks().setAvgRate((double)(sumOfRates + rate) / (double)(numOfRates + 1));
        if(!feedback.equals(""))
            trip.getFeedbacks().addNewFeedback(feedback);
    }




    public boolean isTripHappenningNow(Trip trip, int currDay, int currHour, int currMinutes)
    {
        if(checkIfTodayIsTripsDay(currDay, trip.getDayStart(), trip.getDayArrive(), trip.getRecurrences()))
            if(isCurrTimeInTripRangeTime(trip, currHour, currMinutes, trip.getStartTime(), trip.getHourArrive()))
                return true;

        return false;
    }

    private boolean checkIfTodayIsTripsDay(int currDay, int offeredTripStartDay, int offeredTripArriveDay, String recurrences)
    {
        if(recurrences.equals("One Time"))
            return (currDay >= offeredTripStartDay && currDay <= offeredTripArriveDay);
        else if(currDay >= offeredTripStartDay)
        {
            if(recurrences.equals("Daily"))
                return true;
            else if(offeredTripStartDay == offeredTripArriveDay)
            {
                if(recurrences.equals("BiDaily"))
                    return ((currDay - offeredTripStartDay) % 2 == 0);
                else if(recurrences.equals("Weekly"))
                    return ((currDay - offeredTripStartDay) % 7 == 0);
                else
                    return ((currDay - offeredTripStartDay) % 30 == 0);
            }
            else //trip ends at the next day or after
            {
                if(recurrences.equals("BiDaily"))
                    return true;
                else if(recurrences.equals("Weekly"))
                    return (((currDay - offeredTripStartDay) % 7 == 0) || ((currDay - offeredTripStartDay) % 7 == 1));
                else
                    return (((currDay - offeredTripStartDay) % 30 == 0) || ((currDay - offeredTripStartDay) % 30 == 1));
            }
        }
        else
            return false;
    }

    private boolean isCurrTimeInTripRangeTime(Trip trip, int currHour, int currMinutes, Time tripStartTime, Time tripArriveTime)
    {

        if(trip.getDayStart() != trip.getDayArrive())
        {
            if(currHour > tripStartTime.getHour() || currHour < tripArriveTime.getHour())
                return true;
            else if(currHour == tripStartTime.getHour() && currMinutes >= tripStartTime.getMinutes())
                return true;
            else if(currHour == tripArriveTime.getHour() && currMinutes <= tripArriveTime.getMinutes())
                return true;
        }
        else //trip starts and finished at the same day
        {
            if(currHour > tripStartTime.getHour() && currHour < tripArriveTime.getHour())
                return true;
            else if(tripStartTime.getHour() == tripArriveTime.getHour() && currHour == tripStartTime.getHour()) {
                if (currMinutes >= tripStartTime.getMinutes() && currMinutes <= tripArriveTime.getMinutes())
                    return true;
            }
            else if(currHour == tripStartTime.getHour() && currMinutes >= tripStartTime.getMinutes())
                return true;
            else if(currHour == tripArriveTime.getHour() && currMinutes <= tripArriveTime.getMinutes())
                return true;
        }
        return false;
    }

    public Station convertStringToStation(String stationName)
    {
        for(int i=0; i<this.transPool.getMapDescriptor().getAllStations().size(); i++)
        {
            if(this.transPool.getMapDescriptor().getAllStations().get(i).getName().equals(stationName))
                return this.transPool.getMapDescriptor().getAllStations().get(i);
        }
        return null;
    }

    public boolean isCurrTimeLessThenStationToTime(int currHour, int currMinutes, Time stationToArriveTime)
    {
        if(currHour < stationToArriveTime.getHour())
            return true;
        else if(currHour == stationToArriveTime.getHour())
        {
            if(currMinutes < stationToArriveTime.getMinutes())
                return true;
        }
        return false;
    }

    public List<List<TripPartInfo>> findPossibleTrips(int tripRequestId, int maxOptions)
    {
        int indexOfTripRequest;
        String firstStation, lastStation;
        TripRequest tripRequest;
        List<List<TripPartInfo>> tempRes = new ArrayList<>();
        List<List<TripPartInfo>> res = new ArrayList<>();

        indexOfTripRequest = findTripRequestIndexById(tripRequestId);
        tripRequest = new TripRequest(this.transPool.getAllTripsInfo().getAllTripRequests().get(indexOfTripRequest));

        firstStation = tripRequest.getStartStation();
        lastStation = tripRequest.getEndStation();

        List<Station> allAvailableStations = new ArrayList<>();

        if(tripRequest.getSchedual().getStartOrArrive() == Schedual.StartOrArrive.START)
        {
            allAvailableStations.addAll(buildAllAvailableStationsList(firstStation, this.transPool.getMapDescriptor().getAllStations()));
            tempRes = findTripsAccordingToStartTime(firstStation, firstStation, lastStation, "none",allAvailableStations, tripRequest.getSchedual().getDay(), tripRequest.getSchedual().getTime(), null);
        }
        else
        {
            allAvailableStations.addAll(buildAllAvailableStationsList(lastStation, this.transPool.getMapDescriptor().getAllStations()));
            tempRes = findTripsAccordingToArriveTime(firstStation, lastStation, lastStation, "none",allAvailableStations, tripRequest.getSchedual().getDay(), tripRequest.getSchedual().getTime(), null);
        }

        if(tempRes != null)
        {
            for(int i=0; i<tempRes.size() && i<maxOptions; i++)
            {
                res.add(tempRes.get(i));
            }
        }
        else
            res = null;

        return res;
    }

    private List<Station> buildAllAvailableStationsList(String station, List<Station> stationsList)
    {
        List<Station> res = new ArrayList<>();

        for(int i=0; i<stationsList.size(); i++)
        {
            if(!stationsList.get(i).getName().equals(station))
                res.add(new Station(stationsList.get(i)));
        }
        return res;
    }

    private List<List<TripPartInfo>> findTripsAccordingToStartTime(String firstStation, String currStation, String lastStation, String fromStation, List<Station> allAvailableStations, int day, Time time, Trip trip)
    {
        String[] allStationsNames;
        List<List<TripPartInfo>> res = new ArrayList<>();

        if(currStation.equals(lastStation))
        {
            String owner = trip.getOwner();
            List<Path> pathsList = new ArrayList<>();
            pathsList = (findAllPathsBetweenTwoStations(fromStation, currStation, trip));
            for(int j=0; j<pathsList.size(); j++)
                if(pathsList.get(j).getRemainPlaces() == 0)
                    return null;

            int fuelConsumption = 0;
            int price = 0;
            for(int i=0; i<pathsList.size(); i++)
            {
                fuelConsumption+=pathsList.get(i).getFuelConsumption();
                price+=(pathsList.get(i).getLength() * trip.getPpk());
            }
            TripPartInfo newTripPartInfo = new TripPartInfo(fromStation, lastStation, owner, fuelConsumption, price, day, time);
            List<TripPartInfo> partOfRes = new ArrayList<>();
            partOfRes.add(newTripPartInfo);
            res.add(partOfRes);
            return res;

        }
        else
        {
            if(firstStation.equals(currStation))
            {
                for(int i=0; i<this.transPool.getAllTripsInfo().getAllOfferdTrips().size(); i++)
                {
                    if(checkIfSameDays(day, this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getDayStart(), this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getRecurrences()))
                    {
                        if(findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), firstStation)!= null)
                        {
                            Time offeredTripTimeAtFirstStation = new Time(findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), firstStation));
                            if(time.getHour() == offeredTripTimeAtFirstStation.getHour() && time.getMinutes() == offeredTripTimeAtFirstStation.getMinutes())
                            {
                                allStationsNames = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getPath().split(",");
                                int indexOfFirstStation = findStationIndexAtStringArray(firstStation, i);
                                for(int k=0; k<allStationsNames.length; k++)
                                {
                                    if(k > indexOfFirstStation)
                                    {
                                        if(ckeckIfStationExistsInList(allStationsNames[k], allAvailableStations))
                                        {
                                            List<Station> newAllAvailableStations = new ArrayList<>();
                                            newAllAvailableStations.addAll(buildAllAvailableStationsList(allStationsNames[k], allAvailableStations));
                                            List<List<TripPartInfo>> resPart = findTripsAccordingToStartTime(firstStation, allStationsNames[k], lastStation, currStation, newAllAvailableStations, day, findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), allStationsNames[k]), this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i));
                                            if(resPart != null){
                                                res.addAll(resPart);
//                                                res.addAll(updateList(resPart, currStation, allStationsNames[k], this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), day, offeredTripTimeAtFirstStation))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                for(int i=0; i<this.transPool.getAllTripsInfo().getAllOfferdTrips().size(); i++)
                {
                    if(!this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getOwner().equals(trip.getOwner()))
                    {
                        int dayOfTripAtCurrStation = findFirstDayToGoOnTripFromCertainStation(currStation, this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), day, time);
                        if(dayOfTripAtCurrStation != -1)
                        {
                            if(findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), currStation)!= null)
                            {
                                Time offeredTripTimeAtCurrStation = new Time(findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), currStation));

                                allStationsNames = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getPath().split(",");
                                int indexOfCurrStation = findStationIndexAtStringArray(currStation, i);
                                for(int k=0; k<allStationsNames.length; k++)
                                {
                                    if(k > indexOfCurrStation)
                                    {
                                        if(ckeckIfStationExistsInList(allStationsNames[k], allAvailableStations))
                                        {
                                            List<Path> pathsList = new ArrayList<>();
                                            pathsList = (findAllPathsBetweenTwoStations(currStation, allStationsNames[k], this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i)));
                                            for(int j=0; j<pathsList.size(); j++)
                                                if(pathsList.get(j).getRemainPlaces() == 0)
                                                    return null;

                                            List<Station> newAllAvailableStations = new ArrayList<>();
                                            newAllAvailableStations.addAll(buildAllAvailableStationsList(allStationsNames[k], allAvailableStations));
                                            List<List<TripPartInfo>> resPart = findTripsAccordingToStartTime(firstStation, allStationsNames[k], lastStation, currStation, newAllAvailableStations, dayOfTripAtCurrStation, findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), allStationsNames[k]), this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i));
                                            if(resPart != null)
                                            {
                                                List<List<TripPartInfo>> tempList = updateList(resPart, fromStation, currStation, trip, day, time, true);
                                                if(tempList != null)
                                                    res.addAll(tempList);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(res.isEmpty())
            return null;
        else
            return res;
    }

    private List<List<TripPartInfo>> findTripsAccordingToArriveTime(String firstStation, String currStation, String lastStation, String fromStation, List<Station> allAvailableStations, int day, Time time, Trip trip)
    {
        String[] allStationsNames;
        List<List<TripPartInfo>> res = new ArrayList<>();

        if(currStation.equals(firstStation))
        {
            String owner = trip.getOwner();
            List<Path> pathsList = new ArrayList<>();
            pathsList = (findAllPathsBetweenTwoStations(currStation, fromStation, trip));
            for(int j=0; j<pathsList.size(); j++)
                if(pathsList.get(j).getRemainPlaces() == 0)
                    return null;

            int fuelConsumption = 0;
            int price = 0;
            for(int i=0; i<pathsList.size(); i++)
            {
                fuelConsumption+=pathsList.get(i).getFuelConsumption();
                price+=(pathsList.get(i).getLength() * trip.getPpk());
            }
            TripPartInfo newTripPartInfo = new TripPartInfo(firstStation, fromStation, owner, fuelConsumption, price, day, time);
            List<TripPartInfo> partOfRes = new ArrayList<>();
            partOfRes.add(newTripPartInfo);
            res.add(partOfRes);
            return res;

        }
        else
        {
            if(lastStation.equals(currStation))
            {
                for(int i=0; i<this.transPool.getAllTripsInfo().getAllOfferdTrips().size(); i++)
                {
                    if(checkIfSameDays(day, this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getDayStart(), this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getRecurrences()))
                    {
                        if(findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), lastStation)!= null)
                        {
                            Time offeredTripTimeAtLastStation = new Time(findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), lastStation));
                            if(time.getHour() == offeredTripTimeAtLastStation.getHour() && time.getMinutes() == offeredTripTimeAtLastStation.getMinutes())
                            {
                                allStationsNames = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getPath().split(",");
                                int indexOfLastStation = findStationIndexAtStringArray(lastStation, i);
                                for(int k=0; k<allStationsNames.length; k++)
                                {
                                    if(k < indexOfLastStation)
                                    {
                                        if(ckeckIfStationExistsInList(allStationsNames[k], allAvailableStations))
                                        {
                                            List<Station> newAllAvailableStations = new ArrayList<>();
                                            newAllAvailableStations.addAll(buildAllAvailableStationsList(allStationsNames[k], allAvailableStations));
                                            List<List<TripPartInfo>> resPart = findTripsAccordingToArriveTime(firstStation, allStationsNames[k], lastStation, currStation, newAllAvailableStations, day, findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), allStationsNames[k]), this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i));
                                            if(resPart != null){
                                                res.addAll(resPart);
//                                                res.addAll(updateList(resPart, currStation, allStationsNames[k], this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), day, offeredTripTimeAtFirstStation))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                for(int i=0; i<this.transPool.getAllTripsInfo().getAllOfferdTrips().size(); i++)
                {
                    if(!this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getOwner().equals(trip.getOwner()))
                    {
                        int dayOfTripAtCurrStation = findLastDayToGoOnTripFromCertainStation(currStation, this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), day, time);
                        if(dayOfTripAtCurrStation != -1)
                        {
                            if(findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), currStation)!= null)
                            {
                                Time offeredTripTimeAtCurrStation = new Time(findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), currStation));

                                allStationsNames = this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i).getPath().split(",");
                                int indexOfCurrStation = findStationIndexAtStringArray(currStation, i);
                                for(int k=0; k<allStationsNames.length; k++)
                                {
                                    if(k < indexOfCurrStation)
                                    {
                                        if(ckeckIfStationExistsInList(allStationsNames[k], allAvailableStations))
                                        {
                                            List<Path> pathsList = new ArrayList<>();
                                            pathsList = (findAllPathsBetweenTwoStations(allStationsNames[k], currStation, this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i)));
                                            for(int j=0; j<pathsList.size(); j++)
                                                if(pathsList.get(j).getRemainPlaces() == 0)
                                                    return null;

                                            List<Station> newAllAvailableStations = new ArrayList<>();
                                            newAllAvailableStations.addAll(buildAllAvailableStationsList(allStationsNames[k], allAvailableStations));
                                            List<List<TripPartInfo>> resPart = findTripsAccordingToArriveTime(firstStation, allStationsNames[k], lastStation, currStation, newAllAvailableStations, dayOfTripAtCurrStation, findTimeAtStation(this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i), allStationsNames[k]), this.transPool.getAllTripsInfo().getAllOfferdTrips().get(i));
                                            if(resPart != null)
                                            {
                                                List<List<TripPartInfo>> tempList = updateList(resPart, currStation, fromStation, trip, day, time, false);
                                                if(tempList != null)
                                                    res.addAll(tempList);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(res.isEmpty())
            return null;
        else
            return res;
    }

    private boolean ckeckIfStationExistsInList(String station, List<Station> allAvailableStations)
    {
        for(int i=0; i<allAvailableStations.size(); i++)
        {
            if(allAvailableStations.get(i).getName().equals(station))
                return true;
        }
        return false;
    }

    private List<Path> findAllPathsBetweenTwoStations(String from, String to, Trip trip)
    {
        List<Path> res = new ArrayList<>();
        int fromStationIndex = findStationIndex(from, trip);
        int toStationIndex = findStationIndex(to, trip);

        for(int i=0; i<trip.getRoute().size(); i++)
        {
            if(i >= fromStationIndex && i < toStationIndex)
                res.add(new Path(trip.getRoute().get(i)));
        }

        return res;
    }

    private Path findPathOfTwoStations(String from, String to, Trip trip)
    {
        for(int i=0; i<trip.getRoute().size(); i++)
        {
            Path path = trip.getRoute().get(i);
            if(path.getFrom().equals(from) && path.getTo().equals(to))
                return path;
            else if(!path.isOneWay())
            {
                if(path.getFrom().equals(to) && path.getTo().equals(from))
                    return path;
            }
        }
        return null;
    }

    private List<List<TripPartInfo>> updateList(List<List<TripPartInfo>> resPart, String firstStation, String lastStation, Trip trip, int day, Time time, boolean isStart)
    {
        String owner = trip.getOwner();

        List<Path> pathsList = new ArrayList<>();
        pathsList = (findAllPathsBetweenTwoStations(firstStation, lastStation, trip));
        for(int j=0; j<pathsList.size(); j++)
            if(pathsList.get(j).getRemainPlaces() == 0)
                return null;

        int fuelConsumption = 0;
        int price = 0;
        for(int i=0; i<pathsList.size(); i++)
        {
            fuelConsumption+=pathsList.get(i).getFuelConsumption();
            price+=(pathsList.get(i).getLength() * trip.getPpk());
        }

        TripPartInfo newTripPartInfo = new TripPartInfo(firstStation, lastStation, owner, fuelConsumption, price, day, time);

        for (int i=0; i<resPart.size(); i++)
        {
            if(isStart)
                resPart.get(i).add(0, newTripPartInfo);
            else
                resPart.get(i).add(newTripPartInfo);
        }
        return resPart;
    }

    private int findFirstDayToGoOnTripFromCertainStation(String station, Trip trip, int dayAtStation, Time timeAtStation)
    {
        if(trip.getDayStart() > dayAtStation)
            return trip.getDayStart();
        else if(trip.getDayStart() == dayAtStation)
        {
            if(findTimeAtStation(trip, station) == null)
                return -1;
            else
            {
                Time timeOfTripAtStation = new Time(findTimeAtStation(trip, station));
                if(timeAtStation.getHour() < timeOfTripAtStation.getHour())
                    return trip.getDayStart();
                else if(timeAtStation.getHour() == timeOfTripAtStation.getHour())
                    if(timeAtStation.getMinutes() <= timeOfTripAtStation.getMinutes())
                        return trip.getDayStart();
            }
        }
        else //day start < day at station
            return findDayAccordingToRecurrences(dayAtStation, trip, timeAtStation, station);

        return -1;
    }

    private int findDayAccordingToRecurrences(int dayAtStation, Trip trip, Time timeAtStation, String station)
    {
        if(trip.getRecurrences().equals("One Time"))
            return -1;
        else if(trip.getRecurrences().equals("Daily"))
        {
            if(findTimeAtStation(trip, station) == null)
                return -1;
            else {
                Time timeOfTripAtStation = new Time(findTimeAtStation(trip, station));
                if (timeAtStation.getHour() < timeOfTripAtStation.getHour())
                    return dayAtStation;
                else if (timeAtStation.getHour() == timeOfTripAtStation.getHour()) {
                    if (timeAtStation.getMinutes() <= timeOfTripAtStation.getMinutes())
                        return dayAtStation;
                } else
                    return (dayAtStation + 1);

            }
        }
        else if(trip.getRecurrences().equals("BiDaily"))
        {
            int dayOfTrip = trip.getDayStart();
            while(dayOfTrip < dayAtStation)
                dayOfTrip+=2;

            if(dayOfTrip == dayAtStation)
            {
                if(findTimeAtStation(trip, station) == null)
                    return -1;
                else
                {
                    Time timeOfTripAtStation = new Time(findTimeAtStation(trip, station));
                    if(timeAtStation.getHour() < timeOfTripAtStation.getHour())
                        return dayAtStation;
                    else if(timeAtStation.getHour() == timeOfTripAtStation.getHour())
                    {
                        if(timeAtStation.getMinutes() <= timeOfTripAtStation.getMinutes())
                            return dayAtStation;
                    }
                    else
                        return (dayOfTrip + 2);
                }
            }
            else
                return dayOfTrip;
        }
        else if(trip.getRecurrences().equals("Weekly"))
        {
            int dayOfTrip = trip.getDayStart();
            while(dayOfTrip < dayAtStation)
                dayOfTrip+=7;

            if(dayOfTrip == dayAtStation)
            {
                if(findTimeAtStation(trip, station) == null)
                    return -1;
                else
                {
                    Time timeOfTripAtStation = new Time(findTimeAtStation(trip, station));
                    if(timeAtStation.getHour() < timeOfTripAtStation.getHour())
                        return dayAtStation;
                    else if(timeAtStation.getHour() == timeOfTripAtStation.getHour())
                    {
                        if(timeAtStation.getMinutes() <= timeOfTripAtStation.getMinutes())
                            return dayAtStation;
                    }
                    else
                        return (dayOfTrip + 7);
                }
            }
            else
                return dayOfTrip;
        }
        else //recurrences = monthly
        {
            int dayOfTrip = trip.getDayStart();
            while(dayOfTrip < dayAtStation)
                dayOfTrip+=30;

            if(dayOfTrip == dayAtStation)
            {
                if(findTimeAtStation(trip, station) == null)
                    return -1;
                else
                {
                    Time timeOfTripAtStation = new Time(findTimeAtStation(trip, station));
                    if(timeAtStation.getHour() < timeOfTripAtStation.getHour())
                        return dayAtStation;
                    else if(timeAtStation.getHour() == timeOfTripAtStation.getHour())
                    {
                        if(timeAtStation.getMinutes() <= timeOfTripAtStation.getMinutes())
                            return dayAtStation;
                    }
                    else
                        return (dayOfTrip + 30);
                }
            }
            else
                return dayOfTrip;
        }

        return -1;
    }

    private int findLastDayToGoOnTripFromCertainStation(String station, Trip trip, int dayAtStation, Time timeAtStation)
    {
        if(trip.getDayStart() > dayAtStation)
            return -1;
        else if(trip.getDayStart() == dayAtStation)
        {
            if(findTimeAtStation(trip, station) == null)
                return -1;
            else
            {
                Time timeOfTripAtStation = new Time(findTimeAtStation(trip, station));
                if(timeAtStation.getHour() > timeOfTripAtStation.getHour())
                    return trip.getDayStart();
                else if(timeAtStation.getHour() == timeOfTripAtStation.getHour())
                    if(timeAtStation.getMinutes() >= timeOfTripAtStation.getMinutes())
                        return trip.getDayStart();
            }
        }
        else //day start < day at station
            return findLastDayAccordingToRecurrences(dayAtStation, trip, timeAtStation, station);

        return -1;
    }

    private int findLastDayAccordingToRecurrences(int dayAtStation, Trip trip, Time timeAtStation, String station)
    {
        if(trip.getRecurrences().equals("One Time"))
            return trip.getDayStart();
        else if(trip.getRecurrences().equals("Daily"))
        {
            if(findTimeAtStation(trip, station) == null)
                return -1;
            else {
                Time timeOfTripAtStation = new Time(findTimeAtStation(trip, station));
                if (timeAtStation.getHour() > timeOfTripAtStation.getHour())
                    return dayAtStation;
                else if (timeAtStation.getHour() == timeOfTripAtStation.getHour()) {
                    if (timeAtStation.getMinutes() >= timeOfTripAtStation.getMinutes())
                        return dayAtStation;
                } else
                    return (dayAtStation - 1);

            }
        }
        else if(trip.getRecurrences().equals("BiDaily"))
        {
            int dayOfTrip = trip.getDayStart();
            while(dayOfTrip < dayAtStation)
                dayOfTrip+=2;

            if(dayOfTrip == dayAtStation)
            {
                if(findTimeAtStation(trip, station) == null)
                    return -1;
                else
                {
                    Time timeOfTripAtStation = new Time(findTimeAtStation(trip, station));
                    if(timeAtStation.getHour() > timeOfTripAtStation.getHour())
                        return dayAtStation;
                    else if(timeAtStation.getHour() == timeOfTripAtStation.getHour())
                    {
                        if(timeAtStation.getMinutes() >= timeOfTripAtStation.getMinutes())
                            return dayAtStation;
                    }
                    else
                        return (dayOfTrip - 2);
                }
            }
            else
                return (dayOfTrip - 2);
        }
        else if(trip.getRecurrences().equals("Weekly"))
        {
            int dayOfTrip = trip.getDayStart();
            while(dayOfTrip < dayAtStation)
                dayOfTrip+=7;

            if(dayOfTrip == dayAtStation)
            {
                if(findTimeAtStation(trip, station) == null)
                    return -1;
                else
                {
                    Time timeOfTripAtStation = new Time(findTimeAtStation(trip, station));
                    if(timeAtStation.getHour() > timeOfTripAtStation.getHour())
                        return dayAtStation;
                    else if(timeAtStation.getHour() == timeOfTripAtStation.getHour())
                    {
                        if(timeAtStation.getMinutes() >= timeOfTripAtStation.getMinutes())
                            return dayAtStation;
                    }
                    else
                        return (dayOfTrip - 7);
                }
            }
            else
                return (dayOfTrip - 7);
        }
        else //recurrences = monthly
        {
            int dayOfTrip = trip.getDayStart();
            while(dayOfTrip < dayAtStation)
                dayOfTrip+=30;

            if(dayOfTrip == dayAtStation)
            {
                if(findTimeAtStation(trip, station) == null)
                    return -1;
                else
                {
                    Time timeOfTripAtStation = new Time(findTimeAtStation(trip, station));
                    if(timeAtStation.getHour() > timeOfTripAtStation.getHour())
                        return dayAtStation;
                    else if(timeAtStation.getHour() == timeOfTripAtStation.getHour())
                    {
                        if(timeAtStation.getMinutes() >= timeOfTripAtStation.getMinutes())
                            return dayAtStation;
                    }
                    else
                        return (dayOfTrip - 30);
                }
            }
            else
                return (dayOfTrip - 30);
        }

        return -1;
    }

}