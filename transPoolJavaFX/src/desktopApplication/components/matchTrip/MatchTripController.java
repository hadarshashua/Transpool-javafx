package desktopApplication.components.matchTrip;

import controller.time.Time;
import controller.trips.Trip;
import controller.trips.TripPartInfo;
import desktopApplication.components.main.JavaFXController;
import desktopApplication.components.tripRequest.TripRequestController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MatchTripController {

    @FXML private BorderPane borderPane;
    @FXML private Spinner<Integer> maxOptionsSpinner;
    @FXML private Button searchButton;
    @FXML private FlowPane optionsFlowPane;
    @FXML private Button cancelButton;
    @FXML private Label textLabel;
    @FXML private CheckBox directOnlyCheckBox;


    private TripRequestController tripRequest;
    private JavaFXController javaFXController;
    private boolean isStartTime;

    public MatchTripController()
    {
    }

    @FXML
    private void initialize()
    {
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000,1);
        maxOptionsSpinner.setValueFactory(spinnerValueFactory);
    }

    public void setJavaFXController(JavaFXController javaFXController) {
        this.javaFXController = javaFXController;
    }

    public void setTripRequest(TripRequestController tripRequest) {
        this.tripRequest = tripRequest;
    }

    public void setStartTime(boolean startTime) {
        isStartTime = startTime;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    @FXML
    void searchButtonAction(ActionEvent event)
    {
        String routeDescription, owner, firstStation, lastStation, price, hour, time, minutes, fuelConsumption;
        List<Integer> indexList = new ArrayList<>();
        List<TripPartInfo> tripInfo = new ArrayList<>();
        int intFuelConsumption, intPrice, intHour, intMinutes;

        this.textLabel.setText("");

        if(directOnlyCheckBox.isSelected())
        {
            indexList.addAll(this.javaFXController.findPossibleTripDirectOnly(this.tripRequest.getId(), maxOptionsSpinner.getValue()));
            Trip trip;

            if(indexList.size() == 0)
                this.textLabel.setText("No suitable trips have found for your request");

            for(int i=0; i<indexList.size(); i++)
            {
                trip = new Trip(this.javaFXController.getMainController().getAllOfferdTrips().get(indexList.get(i)));
                owner = trip.getOwner();
                firstStation = this.tripRequest.getStartStation();
                lastStation = this.tripRequest.getEndStation();
                routeDescription = ("Get on at station " + firstStation + " get off at station "+ lastStation + " with owner " + owner);
                intPrice = this.javaFXController.getMainController().calculateTripPrice(this.tripRequest.getId(), indexList.get(i));
                price = String.valueOf(intPrice);
                intHour = this.javaFXController.calculateStartOrArriveHour(this.tripRequest.getId(), indexList.get(i));
                hour = String.valueOf(intHour);
                intMinutes = this.javaFXController.calculateStartOrArriveMinutes(this.tripRequest.getId(), indexList.get(i));
                minutes = String.valueOf(intMinutes);
                time = (hour + ":" + minutes);
                intFuelConsumption = this.javaFXController.calculateTripFuelConsumption(this.tripRequest.getId(), indexList.get(i));
                fuelConsumption = String.valueOf(intFuelConsumption);

                TripPartInfo tripPartInfo = new TripPartInfo(firstStation, lastStation, owner, intFuelConsumption, intPrice, trip.getDayStart() , new Time(intHour, intMinutes));
                tripInfo.add(tripPartInfo);

                createTripOptionTile(routeDescription, price, time, fuelConsumption, indexList, tripInfo);
            }
        }
        else
        {
            List<List<TripPartInfo>> tripOptionsList = this.javaFXController.findPossibleTrips(this.tripRequest.getId(), maxOptionsSpinner.getValue());
            if(tripOptionsList == null)
                this.textLabel.setText("No suitable trips have found for your request");
            createTripOptionTilesFromList(tripOptionsList);
        }
    }

    private void createTripOptionTilesFromList(List<List<TripPartInfo>> tripOptionsList)
    {
        String routeDescription, owner, hour, minutes, time;
        int price, fuelConsumption, tripIndex;
        List<Integer> tripsIndexList;

        for(int i=0; i<tripOptionsList.size(); i++)
        {
            tripsIndexList = new ArrayList<>();
            routeDescription = "";
            price = 0;
            fuelConsumption = 0;

            for(int j=0; j<tripOptionsList.get(i).size(); j++)
            {
                owner = tripOptionsList.get(i).get(j).getOwner();
                routeDescription = (routeDescription + "Get on at station " + tripOptionsList.get(i).get(j).getFirstStation() + " get off at station " + tripOptionsList.get(i).get(j).getLastStation() + " with owner " + owner + " at day " + tripOptionsList.get(i).get(j).getArriveDay() + "\n");
                price+= tripOptionsList.get(i).get(j).getPrice();
                fuelConsumption+= tripOptionsList.get(i).get(j).getFuelConsumption();
                tripIndex = this.javaFXController.findIndexOfTripByOwnerName(owner);
                tripsIndexList.add(tripIndex);
            }

            if(isStartTime)
            {
                hour = String.valueOf(tripOptionsList.get(i).get(tripOptionsList.get(i).size()-1).getArriveTime().getHour());
                minutes = String.valueOf(tripOptionsList.get(i).get(tripOptionsList.get(i).size()-1).getArriveTime().getMinutes());
            }
            else
            {
                hour = String.valueOf(tripOptionsList.get(i).get(0).getArriveTime().getHour());
                minutes = String.valueOf(tripOptionsList.get(i).get(0).getArriveTime().getMinutes());
            }
            time = (hour + ":" + minutes);

            createTripOptionTile(routeDescription, String.valueOf(price), time, String.valueOf(fuelConsumption), tripsIndexList, tripOptionsList.get(i));
        }
    }

    private void createTripOptionTile(String routeDescription, String price, String time, String fuelConsumption, List<Integer> tripsIndexList, List<TripPartInfo> listInfo) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApplication/components/matchTrip/tripOption.fxml");
            loader.setLocation(url);
            Node tripOptionTile = loader.load();

            TripOption tripOption = loader.getController();
            tripOption.setJavaFXController(this.javaFXController);
            tripOption.setTripRequest(this.tripRequest);
            tripOption.setIndexOfOfferedTrip(tripsIndexList);
            tripOption.setTextRouteDescriptionTextArea(routeDescription);
            tripOption.setTextPriceTextField(price);
            tripOption.setTextTimeTextField(time);
            tripOption.setTextFuelConsumptionTextField(fuelConsumption);
            tripOption.setIndexInAccordion(tripsIndexList);
            tripOption.setTripInfo(listInfo);
            if(this.isStartTime)
                tripOption.setTimeLabel("Arrive time:");
            else
                tripOption.setTimeLabel("Start time:");

            this.optionsFlowPane.getChildren().add(tripOptionTile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancelButtonAction(ActionEvent event)
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


}

