package desktopApplication.components.main;

import animatefx.animation.*;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.Model;
import com.fxgraph.graph.PannableCanvas;
import controller.MainController;
import controller.exceptions.*;
import controller.stations.Station;
import controller.stations.StationDetails;
import controller.stations.Stop;
import controller.stations.StopDetails;
import controller.time.Schedual;
import controller.time.Time;
import controller.trips.*;
import desktopApplication.components.feedback.FeedbackController;
import desktopApplication.components.map.car.CarManager;
import desktopApplication.components.map.car.CarNode;
import desktopApplication.components.map.car.SeveralCarsManager;
import desktopApplication.components.map.car.SeveralCarsNode;
import desktopApplication.components.map.coordinate.CoordinateNode;
import desktopApplication.components.map.coordinate.CoordinatesManager;
import desktopApplication.components.map.layout.MapGridLayout;
import desktopApplication.components.map.road.ArrowedEdge;
import desktopApplication.components.map.station.StationManager;
import desktopApplication.components.map.station.StationNode;
import desktopApplication.components.map.stationDetails.StationDetailsDTO;
import desktopApplication.components.offeredTrip.AddOfferedTripController;
import desktopApplication.components.offeredTrip.OfferedTripController;
import desktopApplication.components.tripRequest.AddTripRequestController;
import desktopApplication.components.tripRequest.MatchedTripController;
import desktopApplication.components.tripRequest.TripRequestController;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tasks.LoadXMLTask;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class JavaFXController {

    private String theme1Url = getClass().getResource("/desktopApplication/components/main/theme1.css").toExternalForm();
    private String theme2Url = getClass().getResource("/desktopApplication/components/main/theme2.css").toExternalForm();
    private String theme3Url = getClass().getResource("/desktopApplication/components/main/theme3.css").toExternalForm();


    @FXML private BorderPane borderPane;
    @FXML private ScrollPane scrollpaneContainer;
    @FXML private Accordion tripRequestsAccordion;
    @FXML private Accordion offeredTripsAccordion;
    @FXML private Button loadXMLButton;
    @FXML private Button addTripRequestButton;
    @FXML private Button addOfferedTripButton;
    @FXML private MenuButton chooseThemeMenuButton;
    @FXML private MenuItem theme1MenuItem;
    @FXML private MenuItem theme2MenuItem;
    @FXML private MenuItem theme3MenuItem;
    @FXML private MenuButton nextMenuButton;
    @FXML private MenuButton prevMenuButton;
    @FXML private Label dayLabel;
    @FXML private Label timeLabel;
    @FXML private CheckBox animationCheckBox;


    private SimpleBooleanProperty isFileSelected;
    private SimpleIntegerProperty hour;
    private SimpleStringProperty minutes;
    private SimpleIntegerProperty day;
    private SimpleBooleanProperty isTimeLessThanTwo;
    private SimpleBooleanProperty isTimeLessThanOne;
    private SimpleBooleanProperty isTimeLessThanTwelveThirty;
    private SimpleBooleanProperty isTimeLessThanTwelveAndFive;

    private String currTheme;
    private static Stage primaryStage;
    private MainController mainController;
    private FXMLLoader fxmlLoader;
    private Model model;
    private Graph graph;
    private CoordinatesManager cm;
    private StationManager sm;
    private CarManager carM;
    private SeveralCarsManager sCarM;
    private Task<Boolean> currentRunningTask;

    ObservableList<String> changeTime = FXCollections.observableArrayList("5 minutes", "Half hour", "1 hour", "2 hours", "1 day");


    public JavaFXController()
    {
        this.isFileSelected = new SimpleBooleanProperty(false);
        this.hour = new SimpleIntegerProperty();
        this.minutes = new SimpleStringProperty();
        this.day = new SimpleIntegerProperty();
        this.isTimeLessThanTwo = new SimpleBooleanProperty();
        this.isTimeLessThanOne = new SimpleBooleanProperty();
        this.isTimeLessThanTwelveThirty = new SimpleBooleanProperty();
        this.isTimeLessThanTwelveAndFive = new SimpleBooleanProperty();
        this.currTheme = theme1Url;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public Graph getGraph() {
        return graph;
    }

    public CoordinatesManager getCm() {
        return cm;
    }

    public StationManager getSm() {
        return sm;
    }

    public CarManager getCarM() {
        return carM;
    }

    public SeveralCarsManager getsCarM() {
        return sCarM;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public String getCurrTheme() {
        return currTheme;
    }

    public void setFxmlLoader(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    @FXML
    private void initialize() {

        addTripRequestButton.disableProperty().bind(isFileSelected.not());
        addOfferedTripButton.disableProperty().bind(isFileSelected.not());
        prevMenuButton.disableProperty().bind(isFileSelected.not());
        nextMenuButton.disableProperty().bind(isFileSelected.not());

    }

    private void startAndInitializeTimeLabels()
    {
        this.day.set(1);
        this.hour.set(0);
        this.minutes.set("00");

        dayLabel.textProperty().bind(day.asString());
        timeLabel.textProperty().bind(Bindings.concat(hour, ":", minutes));

        isTimeLessThanTwelveThirty.bind(Bindings.and(hour.isEqualTo(0), minutes.lessThan("30")));
        isTimeLessThanTwelveAndFive.bind(Bindings.and(hour.isEqualTo(0), minutes.isEqualTo("00")));

        prevMenuButton.getItems().get(4).disableProperty().bind(day.isEqualTo(1));
        prevMenuButton.getItems().get(3).disableProperty().bind(Bindings.and(day.isEqualTo(1), hour.lessThan(2)));
        prevMenuButton.getItems().get(2).disableProperty().bind(Bindings.and(day.isEqualTo(1), hour.lessThan(1)));
        prevMenuButton.getItems().get(1).disableProperty().bind(Bindings.and(day.isEqualTo(1), isTimeLessThanTwelveThirty));
        prevMenuButton.getItems().get(0).disableProperty().bind(Bindings.and(day.isEqualTo(1), isTimeLessThanTwelveAndFive));
    }

    public MainController getMainController() {
        return mainController;
    }

    @FXML
    void theme1MenuItemAction(ActionEvent event) {
        primaryStage.getScene().getStylesheets().clear();
        primaryStage.getScene().setUserAgentStylesheet(null);
        primaryStage.getScene().getStylesheets().add(theme1Url);
        this.currTheme = theme1Url;
    }

    @FXML
    void theme2MenuItemAction(ActionEvent event) {
        primaryStage.getScene().getStylesheets().clear();
        primaryStage.getScene().setUserAgentStylesheet(null);
        primaryStage.getScene().getStylesheets().add(theme2Url);
        this.currTheme = theme2Url;
    }

    @FXML
    void theme3MenuItemAction(ActionEvent event) {
        primaryStage.getScene().getStylesheets().clear();
        primaryStage.getScene().setUserAgentStylesheet(null);
        primaryStage.getScene().getStylesheets().add(theme3Url);
        this.currTheme = theme3Url;
    }

    public void buildMap() {

        startAndInitializeTimeLabels();

        Graph graphMap = new Graph();
        createMap(graphMap);
        this.graph = graphMap;

        ScrollPane scrollPane = (ScrollPane) this.fxmlLoader.getNamespace().get("scrollpaneContainer");
        PannableCanvas canvas = graphMap.getCanvas();
        scrollPane.setContent(canvas);

        Platform.runLater(() -> {
            graphMap.getUseViewportGestures().set(false);
            graphMap.getUseNodeGestures().set(false);
        });

        showData();
    }

    public void createMap(Graph graphMap) {

        final Model model = graphMap.getModel();
        this.model = model;
        graphMap.beginUpdate();

        StationManager sm = createStations(model);
        CoordinatesManager cm = createCoordinates(model);
        createEdges(model, cm);
        CarManager carM = putCarIconInRellevantStation(model);
        SeveralCarsManager sCarM = putSeveralCarsIconInRellevantStation(model);

        graphMap.endUpdate();

        this.cm = cm;
        this.sm = sm;
        this.carM = carM;
        this.sCarM = sCarM;

        graphMap.layout(new MapGridLayout(cm, sm, carM, sCarM));
    }

    public StationManager createStations(Model model)
    {
        StationManager sm = new StationManager(StationNode::new);
        int x, y;
        String name;

        for(int i=0; i<this.getMainController().getTransPool().getMapDescriptor().getAllStations().size(); i++)
        {
            x = this.getMainController().getTransPool().getMapDescriptor().getAllStations().get(i).getX();
            y = this.getMainController().getTransPool().getMapDescriptor().getAllStations().get(i).getY();
            name = this.getMainController().getTransPool().getMapDescriptor().getAllStations().get(i).getName();

            StationNode station = sm.getOrCreate(x, y);
            station.setName(name);

            addPassengersInfoForCertainStation(station);
            model.addCell(station);
        }

        return sm;
    }

    private void addPassengersForCertainStation(StationNode station)
    {
        List<String> passengers = new ArrayList<>();

        for(int i=0; i<this.mainController.getTransPool().getAllTripsInfo().getAllMatchingTripRequests().size(); i++)
        {
            MatchedTrip matchedTrip = this.mainController.getTransPool().getAllTripsInfo().getAllMatchingTripRequests().get(i);
            if(matchedTrip.getStartStation().equals(station.getName())) {
                if (matchedTrip.getSchedual().getStartOrArrive().equals(Schedual.StartOrArrive.START)) {
                    if (matchedTrip.getSchedual().getDay() == this.day.getValue())
                        if (matchedTrip.getSchedual().getTime().getHour() == this.hour.getValue() && matchedTrip.getSchedual().getTime().getMinutes() == Integer.parseInt(this.minutes.getValue()))
                            passengers.add(matchedTrip.getPassenger().getName() + " is getting on");

                }
                else //the time at start station is in arrive time
                {
                    if (matchedTrip.getSchedual().getDay() == this.day.getValue())
                        if(matchedTrip.getArriveTime().getHour() ==this.hour.getValue() && matchedTrip.getArriveTime().getMinutes() == Integer.parseInt(this.minutes.getValue()))
                            passengers.add(matchedTrip.getPassenger() + " is getting on");
                }
            }
            else if(matchedTrip.getEndStation().equals(station.getName()))
            {
                if (matchedTrip.getSchedual().getStartOrArrive().equals(Schedual.StartOrArrive.ARRIVE)) {
                    if (matchedTrip.getSchedual().getDay() == this.day.getValue())
                        if (matchedTrip.getSchedual().getTime().getHour() == this.hour.getValue() && matchedTrip.getSchedual().getTime().getMinutes() == Integer.parseInt(this.minutes.getValue()))
                            passengers.add(matchedTrip.getPassenger() + " is getting off");

                }
                else //the time at end station is in arrive time
                {
                    if (matchedTrip.getSchedual().getDay() == this.day.getValue())
                        if(matchedTrip.getArriveTime().getHour() ==this.hour.getValue() && matchedTrip.getArriveTime().getMinutes() == Integer.parseInt(this.minutes.getValue()))
                            passengers.add(matchedTrip.getPassenger() + " is getting off");
                }
            }
        }

        station.setDetailsSupplier(() -> {
            return new StationDetailsDTO(passengers);
        });
    }

    private void addPassengersInfoForCertainStation(StationNode station)
    {
        List<String> passengers = new ArrayList<>();

        for(int i=0; i<this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().size(); i++)
        {
            Trip trip = this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i);
            List<Stop> tripStops = new ArrayList<>();
            tripStops.addAll(trip.getStops());

            for(int j=0; j<tripStops.size(); j++)
            {
                Time tripTimeAtStation = new Time(this.mainController.findTimeAtStation(trip, tripStops.get(j).getStationName()));

                if(tripStops.get(j).getStationName().equals(station.getName()))
                {
                    if(this.hour.getValue() == tripTimeAtStation.getHour() && Integer.parseInt(this.minutes.getValue()) == tripTimeAtStation.getMinutes())
                    {
                        for(int k=0; k<tripStops.get(j).getStopDetails().size(); k++)
                        {
                            if(tripStops.get(j).getStopDetails().get(k).getDay() == this.day.getValue())
                            {
                                if(tripStops.get(j).getStopDetails().get(k).getStatus().equals(StopDetails.Status.GET_ON))
                                    passengers.add(tripStops.get(j).getStopDetails().get(k).getPassenger().getName() + " is getting on");
                                else
                                    passengers.add(tripStops.get(j).getStopDetails().get(k).getPassenger().getName() + " is getting off");
                            }
                        }
                    }
                }
            }
        }

        station.setDetailsSupplier(() -> {
            return new StationDetailsDTO(passengers);
        });
    }

    public CoordinatesManager createCoordinates(Model model) {

        CoordinatesManager cm = new CoordinatesManager(CoordinateNode::new);
        int width, length;

        width = this.mainController.getTransPool().getMapDescriptor().getMapBoundries().getWidth();
        length = this.mainController.getTransPool().getMapDescriptor().getMapBoundries().getLength();

        for (int i=0; i<width; i++) {
            for (int j = 0; j < length; j++) {
                model.addCell(cm.getOrCreate(i+1, j+1));
            }
        }

        return cm;
    }

    public void createEdges(Model model, CoordinatesManager cm)
    {
        Station from, to;

        for (int i=0; i<this.mainController.getTransPool().getMapDescriptor().getAllPaths().size(); i++)
        {
            Path path = this.mainController.getTransPool().getMapDescriptor().getAllPaths().get(i);
            from = this.mainController.convertStringToStation(path.getFrom());
            to = this.mainController.convertStringToStation(path.getTo());

            ArrowedEdge e13 = new ArrowedEdge(cm.getOrCreate(from.getX(),from.getY()), cm.getOrCreate(to.getX(),to.getY()), path.isOneWay());
            //e13.textProperty().set("L: 7 ; FC: 4");
            model.addEdge(e13);

//            Platform.runLater(() -> {
//                e13.getLine().getStyleClass().add("line2");
//                e13.getText().getStyleClass().add("edge-text");
//            });
        }
    }

    public CarManager putCarIconInRellevantStation(Model model)
    {
        CarManager cm = new CarManager(CarNode::new);
        List<StationDetails> stationsList = new ArrayList<>();
        int stationIndex;
        StationDetails newStation;
        boolean isStationFound;
        String tripPath;
        String[] allStationsNames;

        for(int i=0; i<this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().size(); i++)
        {
            isStationFound = false;

            if(this.mainController.isTripHappenningNow(this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i), this.day.getValue(), this.hour.getValue(), Integer.parseInt(this.minutes.getValue())))
            {
                tripPath = this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i).getPath();
                allStationsNames = tripPath.split(",");

                for(int j=0; j<this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i).getRoute().size() && !isStationFound; j++)
                {
                    Path path = this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i).getRoute().get(j);

                    if(j == this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i).getRoute().size() - 1)
                    {
                        if(this.hour.getValue() == this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i).getHourArrive().getHour() && Integer.parseInt(this.minutes.getValue()) == this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i).getHourArrive().getMinutes())
                        {
                            String station = allStationsNames[allStationsNames.length - 1];
                            Station lastStation = convertStringToStation(station);
                            stationIndex = findStationIndexInStationsDetailsList(lastStation, stationsList);
                            if(stationIndex != -1)
                                stationsList.get(stationIndex).addNewTripToList(this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                            else
                            {
                                newStation = new StationDetails(lastStation, this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                                stationsList.add(newStation);
                            }
                            isStationFound = true;
                        }
                    }

                    if(path.isOneWay())
                    {
                        if(this.mainController.isCurrTimeLessThenStationToTime(this.hour.getValue(), Integer.parseInt(this.minutes.getValue()), path.getStationToArriveHour()))
                        {
                            Station from = this.mainController.convertStringToStation(path.getFrom());
                            stationIndex = findStationIndexInStationsDetailsList(from, stationsList);
                            if(stationIndex != -1)
                                stationsList.get(stationIndex).addNewTripToList(this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                            else
                            {
                                newStation = new StationDetails(from, this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                                stationsList.add(newStation);
                            }
                            isStationFound = true;
                        }
                    }
                    else
                    {
                        int stationFromIndex = this.mainController.findStationIndexAtPath(path.getFrom(), i);
                        int stationToIndex = this.mainController.findStationIndexAtPath(path.getTo(), i);
                        if(stationFromIndex < stationToIndex)
                        {
                            if(this.mainController.isCurrTimeLessThenStationToTime(this.hour.getValue(), Integer.parseInt(this.minutes.getValue()), path.getStationToArriveHour()))
                            {
                                Station from = this.mainController.convertStringToStation(path.getFrom());
                                stationIndex = findStationIndexInStationsDetailsList(from, stationsList);
                                if(stationIndex != -1)
                                    stationsList.get(stationIndex).addNewTripToList(this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                                else
                                {
                                    newStation = new StationDetails(from, this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                                    stationsList.add(newStation);
                                }
                                isStationFound = true;
                            }
                        }
                        else
                        {
                            if(this.mainController.isCurrTimeLessThenStationToTime(this.hour.getValue(), Integer.parseInt(this.minutes.getValue()), path.getStationToArriveHour()))
                            {
                                Station to = this.mainController.convertStringToStation(path.getTo());
                                stationIndex = findStationIndexInStationsDetailsList(to, stationsList);
                                if(stationIndex != -1)
                                    stationsList.get(stationIndex).addNewTripToList(this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                                else
                                {
                                    newStation = new StationDetails(to, this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                                    stationsList.add(newStation);
                                }
                                isStationFound = true;
                            }
                        }
                    }

                }
            }
        }

        for(int i=0; i<stationsList.size(); i++)
        {
            if(stationsList.get(i).getNumOfCars() == 1) //only one car in station
            {
                CarNode car = cm.getOrCreate(stationsList.get(i).getStation().getX(), stationsList.get(i).getStation().getY());
                car.setName(stationsList.get(i).getTripsInStation().get(0).getOwner());
                car.setController(this);
                car.setTrip(stationsList.get(i).getTripsInStation().get(0));
                model.addCell(car);
            }
        }
        return cm;
    }

    public SeveralCarsManager putSeveralCarsIconInRellevantStation(Model model)
    {
        SeveralCarsManager scm = new SeveralCarsManager(SeveralCarsNode::new);
        List<StationDetails> stationsList = new ArrayList<>();
        int stationIndex;
        StationDetails newStation;
        boolean isStationFound;
        String tripPath;
        String[] allStationsNames;

        for(int i=0; i<this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().size(); i++)
        {
            isStationFound = false;

            if(this.mainController.isTripHappenningNow(this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i), this.day.getValue(), this.hour.getValue(), Integer.parseInt(this.minutes.getValue())))
            {
                tripPath = this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i).getPath();
                allStationsNames = tripPath.split(",");

                for(int j=0; j<this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i).getRoute().size() && !isStationFound; j++)
                {
                    Path path = this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i).getRoute().get(j);

                    if(j == this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i).getRoute().size() - 1)
                    {
                        if(this.hour.getValue() == this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i).getHourArrive().getHour() && Integer.parseInt(this.minutes.getValue()) == this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i).getHourArrive().getMinutes())
                        {
                            String station = allStationsNames[allStationsNames.length - 1];
                            Station lastStation = convertStringToStation(station);
                            stationIndex = findStationIndexInStationsDetailsList(lastStation, stationsList);
                            if(stationIndex != -1)
                                stationsList.get(stationIndex).addNewTripToList(this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                            else
                            {
                                newStation = new StationDetails(lastStation, this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                                stationsList.add(newStation);
                            }
                            isStationFound = true;
                        }
                    }

                    if(path.isOneWay())
                    {
                        if(this.mainController.isCurrTimeLessThenStationToTime(this.hour.getValue(), Integer.parseInt(this.minutes.getValue()), path.getStationToArriveHour()))
                        {
                            Station from = this.mainController.convertStringToStation(path.getFrom());
                            stationIndex = findStationIndexInStationsDetailsList(from, stationsList);
                            if(stationIndex != -1)
                                stationsList.get(stationIndex).addNewTripToList(this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                            else
                            {
                                newStation = new StationDetails(from, this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                                stationsList.add(newStation);
                            }
                            isStationFound = true;
                        }
                    }
                    else
                    {
                        int stationFromIndex = this.mainController.findStationIndexAtPath(path.getFrom(), i);
                        int stationToIndex = this.mainController.findStationIndexAtPath(path.getTo(), i);
                        if(stationFromIndex < stationToIndex)
                        {
                            if(this.mainController.isCurrTimeLessThenStationToTime(this.hour.getValue(), Integer.parseInt(this.minutes.getValue()), path.getStationToArriveHour()))
                            {
                                Station from = this.mainController.convertStringToStation(path.getFrom());
                                stationIndex = findStationIndexInStationsDetailsList(from, stationsList);
                                if(stationIndex != -1)
                                    stationsList.get(stationIndex).addNewTripToList(this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                                else
                                {
                                    newStation = new StationDetails(from, this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                                    stationsList.add(newStation);
                                }
                                isStationFound = true;
                            }
                        }
                        else
                        {
                            if(this.mainController.isCurrTimeLessThenStationToTime(this.hour.getValue(), Integer.parseInt(this.minutes.getValue()), path.getStationToArriveHour()))
                            {
                                Station to = this.mainController.convertStringToStation(path.getTo());
                                stationIndex = findStationIndexInStationsDetailsList(to, stationsList);
                                if(stationIndex != -1)
                                    stationsList.get(stationIndex).addNewTripToList(this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                                else
                                {
                                    newStation = new StationDetails(to, this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i));
                                    stationsList.add(newStation);
                                }
                                isStationFound = true;
                            }
                        }
                    }

                }
            }
        }

        for(int i=0; i<stationsList.size(); i++)
        {
            if(stationsList.get(i).getNumOfCars() != 1) //only one car in station
            {
                SeveralCarsNode cars = scm.getOrCreate(stationsList.get(i).getStation().getX(), stationsList.get(i).getStation().getY());
                cars.setController(this);
                cars.setTripsInStation(stationsList.get(i).getTripsInStation());
                model.addCell(cars);
            }
        }
        return scm;
    }

    public Station convertStringToStation(String station)
    {
        return this.mainController.convertStringToStation(station);
    }

    private int findStationIndexInStationsDetailsList(Station station, List<StationDetails> stationsList )
    {
        int res = -1;
        for(int i=0; i<stationsList.size(); i++)
        {
            if(stationsList.get(i).getStation().getName().equals(station.getName()))
                return i;
        }
        return res;
    }

    @FXML
    void addOfferedTripButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApplication/components/offeredTrip/addOfferedTrip.fxml");
            loader.setLocation(url);
            Parent root = loader.load();
            AddOfferedTripController addOfferedTripController = loader.getController();
            addOfferedTripController.setJavaFXController(this);
            addOfferedTripController.getGridPane().getStylesheets().clear();
            addOfferedTripController.getGridPane().getStylesheets().add(currTheme);

            Stage stage = new Stage();
            stage.setTitle("Add Offered Trip stage");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(JavaFXController.getPrimaryStage());
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addTripRequestButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApplication/components/tripRequest/addTripRequest.fxml");
            loader.setLocation(url);
            Parent root = loader.load();
            AddTripRequestController addTripRequestController = loader.getController();
            addTripRequestController.setJavaFXController(this);
            addTripRequestController.getGridPane().getStylesheets().clear();
            addTripRequestController.getGridPane().getStylesheets().add(currTheme);
            loadStationsMenuButtons(addTripRequestController);

            Stage stage = new Stage();
            stage.setTitle("Add Trip request stage");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(JavaFXController.getPrimaryStage());
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStationsMenuButtons(AddTripRequestController addTripRequestController)
    {
        ObservableList<String> stationsList = FXCollections.observableArrayList();

        for(int j=0; j< this.mainController.getTransPool().getMapDescriptor().getAllStations().size(); j++)
        {
            String station = this.mainController.getTransPool().getMapDescriptor().getAllStations().get(j).getName();
            stationsList.add(station);
        }

        addTripRequestController.getFirstStationComboBox().setItems(stationsList);
        addTripRequestController.getLastStationComboBox().setItems(stationsList);
    }

    @FXML
    void loadXMLButtonAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        tripRequestsAccordion.getPanes().clear();
        if(this.mainController.getTransPool() != null)
            this.mainController.clearSystem();
        offeredTripsAccordion.getPanes().clear();

        LoadXMLController loadXMLController = new LoadXMLController();

        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("loadXML.fxml");
            loader.setLocation(url);
            Parent root = loader.load();
            loadXMLController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("load XML stage");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(JavaFXController.getPrimaryStage());
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();

            this.currentRunningTask = new LoadXMLTask(selectedFile, this.mainController);
            bindTaskToUIComponents(currentRunningTask, loadXMLController);
            this.mainController.loadXMLFile(this.currentRunningTask);

            this.currentRunningTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                    new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent t) {
                            buildMap();
                            stage.close();
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }

        isFileSelected.set(true);
    }

    public void bindTaskToUIComponents(Task<Boolean> task, LoadXMLController loadXMLController)
    {
        loadXMLController.getTaskMessageLabel().textProperty().bind(task.messageProperty());
        loadXMLController.getTaskProgressBar().progressProperty().bind(task.progressProperty());
        task.setOnFailed(evt -> {
            loadXMLController.getErrorLabel().setText(task.exceptionProperty().asString().getValue().substring(task.exceptionProperty().asString().getValue().lastIndexOf(":")+1));
        });
    }

    private void showData()
    {
        int id, price, hour, minutes, arriveHour, arriveMinutes, day, capacity;
        double fuelConsumption;
        String owner, route, recurrences;
        List<Trip> allOfferedTrips = this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips();

        for(int i=0; i<allOfferedTrips.size(); i++)
        {
            id = allOfferedTrips.get(i).getId();
            owner = allOfferedTrips.get(i).getOwner();
            route = allOfferedTrips.get(i).getPath();
            price = this.mainController.calculateOfferedTripPrice(i);
            hour = allOfferedTrips.get(i).getStartTime().getHour();
            minutes = allOfferedTrips.get(i).getStartTime().getMinutes();
            arriveHour = allOfferedTrips.get(i).getHourArrive().getHour();
            arriveMinutes = allOfferedTrips.get(i).getHourArrive().getMinutes();
            fuelConsumption = allOfferedTrips.get(i).getAllTripFuelConsumption();
            day = allOfferedTrips.get(i).getDayStart();
            recurrences = allOfferedTrips.get(i).getRecurrences();
            capacity = allOfferedTrips.get(i).getCapacity();

            addTitledPaneToOfferedTripAccordion(owner);
            createOfferedTripTile(id, owner, route, price, hour, minutes, arriveHour, arriveMinutes, fuelConsumption, day, recurrences, capacity, i);
        }
    }


    private void createMatchedTripTile(MatchedTrip matchedTrip, int indexInAccordion) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApplication/components/tripRequest/matchedTrip.fxml");
            loader.setLocation(url);
            Node matchedTripTile = loader.load();

            MatchedTripController matchedTripController = loader.getController();
            matchedTripController.setId(matchedTrip.getId());
            matchedTripController.setName(matchedTrip.getPassenger().getName());
            matchedTripController.setStartStation(matchedTrip.getStartStation());
            matchedTripController.setEndStation(matchedTrip.getEndStation());
            matchedTripController.setHour(matchedTrip.getSchedual().getTime().getHour());
            matchedTripController.setMinutes(matchedTrip.getSchedual().getTime().getMinutes());
            matchedTripController.setDay(matchedTrip.getSchedual().getDay());
            matchedTripController.setOwnersAndIdsComboBox(matchedTrip.getOwnersNames(), matchedTrip.getAllTripsIds());
            matchedTripController.setPrice(matchedTrip.getPrice());
            matchedTripController.setArriveHour(matchedTrip.getArriveTime().getHour());
            matchedTripController.setArriveMinutes(matchedTrip.getArriveTime().getMinutes());
            matchedTripController.setFuelConsumption(matchedTrip.getFuelConsumptionOfPassenger());
            if(matchedTrip.getSchedual().getStartOrArrive().equals(Schedual.StartOrArrive.START))
            {
                matchedTripController.setTextOfTripRequestTimeLabel("Start time:");
                matchedTripController.setTextOfOfferedTripTimeLabel("Arrive Time:");
            }
            else
            {
                matchedTripController.setTextOfTripRequestTimeLabel("Arrive time:");
                matchedTripController.setTextOfOfferedTripTimeLabel("Start Time:");
            }
            tripRequestsAccordion.getPanes().get(indexInAccordion).setContent(matchedTripTile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTripRequestTile(int id, String name, String startStation, String endStation, int hour, int minutes, int day, Schedual.StartOrArrive startOrArrive) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApplication/components/tripRequest/tripRequest.fxml");
            loader.setLocation(url);
            Node tripRequestTile = loader.load();

            TripRequestController tripRequestController = loader.getController();
            tripRequestController.setId(id);
            tripRequestController.setName(name);
            tripRequestController.setStartStation(startStation);
            tripRequestController.setEndStation(endStation);
            tripRequestController.setHour(hour);
            tripRequestController.setMinutes(minutes);
            tripRequestController.setDay(day);
            tripRequestController.setJavaFXController(this);
            tripRequestController.setIndexInAccordion(tripRequestsAccordion.getPanes().size()-1);
            if(startOrArrive.equals(Schedual.StartOrArrive.START))
            {
                tripRequestController.setTextTimeTextLabel("Start time:");
                tripRequestController.setIsStartTime(true);
            }
            else
            {
                tripRequestController.setTextTimeTextLabel("Arrive time:");
                tripRequestController.setIsStartTime(false);
            }

            tripRequestsAccordion.getPanes().get(tripRequestsAccordion.getPanes().size()-1).setContent(tripRequestTile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createOfferedTripTile(int id, String owner, String route, int price, int hour, int minutes, int arriveHour, int arriveMinutes, double fuelConsumption, int day, String recurrences, int capacity, int indexOfOfferedTrip) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApplication/components/offeredTrip/offeredTrip.fxml");
            loader.setLocation(url);
            Node offeredTripTile = loader.load();

            OfferedTripController offeredTripController = loader.getController();
            offeredTripController.setId(id);
            offeredTripController.setOwner(owner);
            offeredTripController.setRoute(route);
            offeredTripController.setPrice(price);
            offeredTripController.setHour(hour);
            offeredTripController.setMinutes(minutes);
            offeredTripController.setArriveHour(arriveHour);
            offeredTripController.setArriveMinutes(arriveMinutes);
            offeredTripController.setFuelConsumption(fuelConsumption);
            offeredTripController.setDay(day);
            offeredTripController.setRecurrences(recurrences);
            offeredTripController.setCapacity(capacity);
            offeredTripController.setIndexInAccordion(offeredTripsAccordion.getPanes().size()-1);
            offeredTripController.setIndexOfOfferedTrip(indexOfOfferedTrip);
            offeredTripController.setJavaFXController(this);

            offeredTripsAccordion.getPanes().get(offeredTripsAccordion.getPanes().size()-1).setContent(offeredTripTile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeNewOfferedTrip(String owner, String route, int day, int hour, int minutes, String recurrences, int ppk, int capacity)
    {
        this.mainController.makeNewOfferedTrip(owner, route, capacity, ppk, hour, minutes, day, recurrences, this.mainController.getTransPool().getMapDescriptor());
        Trip trip = this.mainController.getAllOfferdTrips().get(this.mainController.getAllOfferdTrips().size()-1);
        int price = this.mainController.calculateOfferedTripPrice(this.mainController.getAllOfferdTrips().size()-1);
        addTitledPaneToOfferedTripAccordion(owner);
        createOfferedTripTile(trip.getId(), owner, route, price, hour, minutes, trip.getHourArrive().getHour(), trip.getHourArrive().getMinutes(), trip.getAllTripFuelConsumption(), day, recurrences, trip.getCapacity(), this.mainController.getAllOfferdTrips().size()-1);
    }

    public List<Integer> findPossibleTripDirectOnly(int id, int maxOptions)
    {
        return this.mainController.findPossibleTripDirectOnly(id, maxOptions);
    }

    public List<List<TripPartInfo>> findPossibleTrips(int id, int maxOptions)
    {
        return this.mainController.findPossibleTrips(id, maxOptions);
    }

    public boolean areStationsExist(String route)
    {
        return this.mainController.areStationsExists(route);
    }

    public boolean arePathsExist(String route)
    {
        return this.mainController.arePathsExist(route);
    }


    public void makeNewTripRequest(String name, String firstStation, String lastStation, int day, int hour, int minutes, Schedual.StartOrArrive startOrArrive)
    {
        this.mainController.makeNewTripRequest(name, firstStation, lastStation, hour, minutes, day, startOrArrive);
        TripRequest tripRequest = this.mainController.getAllTripRequests().get(this.mainController.getAllTripRequests().size()-1);
        addTitledPaneToTripRequestAccordion(name);
        createTripRequestTile(tripRequest.getId(), name, firstStation, lastStation, hour, minutes, day, startOrArrive);
    }

    public void addTitledPaneToTripRequestAccordion(String passengerName)
    {
        TitledPane newTitledPane = new TitledPane();
        newTitledPane.setText(passengerName);
        this.tripRequestsAccordion.getPanes().add(newTitledPane);
    }

    public void addTitledPaneToOfferedTripAccordion(String ownerName)
    {
        TitledPane newTitledPane = new TitledPane();
        newTitledPane.setText(ownerName);
        this.offeredTripsAccordion.getPanes().add(newTitledPane);
    }

    private void updateMap()
    {
        this.graph.beginUpdate();

        StationManager sm = createStations(model);
        CoordinatesManager cm = createCoordinates(model);
        createEdges(model, cm);
        CarManager carM = putCarIconInRellevantStation(this.model);
        SeveralCarsManager sCarM = putSeveralCarsIconInRellevantStation(this.model);

        for (int i=0; i<this.model.getAllCells().size(); i++)
        {
            if(this.model.getAllCells().get(i).getClass().getName().equals("desktopApplication.components.map.station.StationNode"))
                addPassengersInfoForCertainStation((StationNode)this.model.getAllCells().get(i));
        }
        this.graph.endUpdate();

        this.carM = carM;
        this.sCarM = sCarM;

        this.graph.layout(new MapGridLayout(cm, sm, carM, sCarM));
    }

    @FXML
    void prevMenuButtonAction(ActionEvent event) {
    }

    @FXML
    void nextMenuButtonAction(ActionEvent event) {

    }

    @FXML
    void fiveMinutesBackwardsAction(ActionEvent event) {
        if(Integer.parseInt(this.minutes.getValue()) == 0)
        {
            if(this.hour.get() == 0)
            {
                this.hour.set(23);
                this.day.set(this.day.getValue()-1);
            }
            else
                this.hour.set(this.hour.get()-1);

            this.minutes.setValue("55");
        }
        else
        {
            if(Integer.parseInt(this.minutes.getValue()) == 10)
                this.minutes.set("05");
            else if(Integer.parseInt(this.minutes.getValue()) == 5)
                this.minutes.set("00");
            else
            {
                int newMinutes = Integer.parseInt(this.minutes.getValue()) - 5;
                this.minutes.set(String.valueOf(newMinutes));
            }
        }
        updateMap();
    }

    @FXML
    void halfHourBackwardsAction(ActionEvent event) {
        int newMinutes;
        if(Integer.parseInt(this.minutes.getValue()) < 30)
        {
            if(this.hour.get() == 0)
            {
                this.hour.set(23);
                this.day.set(this.day.getValue()-1);
            }
            else
                this.hour.set(this.hour.get()-1);

            newMinutes = (Integer.parseInt(this.minutes.getValue()) + 30);
            checkAndSetMinutes(newMinutes);
        }
        else
        {
            newMinutes = Integer.parseInt(this.minutes.getValue()) - 30;
            checkAndSetMinutes(newMinutes);
        }
        updateMap();
    }

    @FXML
    void oneHourBackwardsAction(ActionEvent event) {
        if(this.hour.get() == 0)
        {
            this.hour.set(23);
            this.day.set(this.day.get()-1);
        }
        else
            this.hour.set(this.hour.get()-1);
        updateMap();

    }


    @FXML
    void twoHoursBackwardsAction(ActionEvent event) {
        if(this.hour.get() == 0 || this.hour.get() == 1)
        {
            this.hour.set(this.hour.get() + 22);
            this.day.set(this.day.get()-1);
        }
        else
            this.hour.set(this.hour.get()-2);
        updateMap();

    }

    @FXML
    void oneDayBackwardsAction(ActionEvent event) {
        this.day.set(this.day.getValue()-1);
        updateMap();

    }


    @FXML
    void fiveMinutesForwardAction(ActionEvent event) {
        if(Integer.parseInt(this.minutes.getValue()) == 55)
        {
            if(this.hour.get() == 23)
            {
                this.hour.set(0);
                this.day.set(this.day.getValue()+1);
            }
            else
                this.hour.set(this.hour.get()+1);

            this.minutes.setValue("00");
        }
        else
        {
            if(Integer.parseInt(this.minutes.getValue()) == 0)
                this.minutes.set("05");
            else
            {
                int newMinutes = Integer.parseInt(this.minutes.getValue()) + 5;
                this.minutes.set(String.valueOf(newMinutes));
            }
        }
        updateMap();

    }

    @FXML
    void halfHourForwardAction(ActionEvent event)
    {
        int newMinutes;
        if(Integer.parseInt(this.minutes.getValue()) >= 30)
        {
            if(this.hour.get() == 23)
            {
                this.hour.set(0);
                this.day.set(this.day.getValue()+1);
            }
            else
                this.hour.set(this.hour.get()+1);

            newMinutes = (Integer.parseInt(this.minutes.getValue()) + 30) % 60;
            checkAndSetMinutes(newMinutes);
        }
        else
        {
            newMinutes = Integer.parseInt(this.minutes.getValue()) + 30;
            this.minutes.set(String.valueOf(newMinutes));
        }
        updateMap();

    }

    @FXML
    void oneHourForwardAction(ActionEvent event) {
        if(this.hour.get() == 23)
        {
            this.hour.set(0);
            this.day.set(this.day.get()+1);
        }
        else
            this.hour.set(this.hour.get()+1);
        updateMap();

    }

    @FXML
    void twoHoursForwardAction(ActionEvent event) {
        if(this.hour.get() == 23 || this.hour.get() == 22)
        {
            this.hour.set((this.hour.get()+ 2 ) % 24);
            this.day.set(this.day.get()+1);
        }
        else
            this.hour.set(this.hour.get()+2);
        updateMap();

    }

    @FXML
    void oneDayForwardAction(ActionEvent event) {
        this.day.set(this.day.getValue()+1);
        updateMap();

    }


    private void checkAndSetMinutes(int minutes)
    {
        if(minutes == 0)
            this.minutes.set("00");
        else if(minutes == 5)
            this.minutes.set("05");
        else
            this.minutes.set(String.valueOf(minutes));
    }

    public int calculateStartOrArriveHour(int idOfTripRequest, int indexOfOfferedTrip)
    {
        return this.mainController.calculateStartOrArriveHour(idOfTripRequest, indexOfOfferedTrip);
    }

    public int calculateStartOrArriveMinutes(int idOfTripRequest, int indexOfOfferedTrip)
    {
        return this.mainController.calculateStartOrArriveMinutes(idOfTripRequest, indexOfOfferedTrip);
    }

    public int calculateTripFuelConsumption(int idOfTripRequest, int indexOfOfferedTrip)
    {
        return this.mainController.calculateTripFuelConsumption(idOfTripRequest, indexOfOfferedTrip);
    }

    public void makeMatch(int idOfTripRequest, List<Integer> indexOfOfferedTrip, int tripRequestIndexInAccordion, List<Integer> offeredTripIndexInAccordion, List<TripPartInfo> tripInfo)
    {
        for(int i=0; i<indexOfOfferedTrip.size(); i++)
            this.mainController.makeMatch(idOfTripRequest, indexOfOfferedTrip.get(i), tripInfo.get(i));

        this.mainController.finishMatch(idOfTripRequest, tripInfo);

        createMatchedTripTile(this.mainController.getAllMatchingTripRequests().get(this.mainController.getAllMatchingTripRequests().size() - 1), tripRequestIndexInAccordion);
        openFeedbackWindow(offeredTripIndexInAccordion, indexOfOfferedTrip);

        if(this.animationCheckBox.isSelected())
            for(int i=0; i<offeredTripIndexInAccordion.size(); i++)
                new Wobble(this.offeredTripsAccordion.getPanes().get(offeredTripIndexInAccordion.get(i))).play();
    }

    public void openFeedbackWindow(List<Integer> offeredTripIndexInAccordion,  List<Integer> indexOfOfferedTrip)
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApplication/components/feedback/feedback.fxml");
            loader.setLocation(url);
            Parent root = loader.load();
            FeedbackController feedbackController = loader.getController();
            feedbackController.setJavaFXController(this);
            feedbackController.setIndexOfOfferedTrip(indexOfOfferedTrip);
            feedbackController.setIndexOfOfferedTripInAccordion(offeredTripIndexInAccordion);
            feedbackController.getGridPane().getStylesheets().add(this.currTheme);

            Stage stage = new Stage();
            stage.setTitle("Add controller.feedback stage");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(JavaFXController.getPrimaryStage());
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFeedbackToOwner(int rate, String feedback, int indexOfOfferedTrip, int offeredTripIndexInAccordion)
    {
        this.mainController.addFeedbackToOwner(rate, feedback, indexOfOfferedTrip);
    }

    public int findIndexOfTripByOwnerName(String owner)
    {
        for(int i=0; i<this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().size(); i++)
        {
            if(this.mainController.getTransPool().getAllTripsInfo().getAllOfferdTrips().get(i).getOwner().equals(owner))
                return i;
        }
        return -1;
    }

}

