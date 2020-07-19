package desktopApplication.components.tripRequest;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.List;

public class MatchedTripController {

    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label startStationLabel;
    @FXML private Label endStationLabel;
    @FXML private Label timeLabel;
    @FXML private Label dayLabel;
    //@FXML private Label tripIdLabel;
    //@FXML private Label ownerLabel;
    @FXML private Label priceLabel;
    @FXML private Label arriveTimeLabel;
    @FXML private Label fuelConsumptionLabel;
    @FXML private Label tripRequestTimeLabel;
    @FXML private Label offeredTripTimeLabel;
    @FXML private ComboBox<String> ownersAndIdsComboBox;


    protected SimpleIntegerProperty id;
    protected SimpleStringProperty name;
    protected SimpleStringProperty startStation;
    protected SimpleStringProperty endStation;
    protected SimpleIntegerProperty hour;
    protected SimpleStringProperty minutes;
    protected SimpleIntegerProperty day;
    //protected SimpleStringProperty tripId;
    //protected SimpleStringProperty owner;
    protected SimpleIntegerProperty price;
    protected SimpleIntegerProperty arriveHour;
    protected SimpleStringProperty arriveMinutes;
    protected SimpleDoubleProperty fuelConsumption;

    public MatchedTripController()
    {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.startStation = new SimpleStringProperty();
        this.endStation = new SimpleStringProperty();
        this.hour = new SimpleIntegerProperty();
        this.minutes = new SimpleStringProperty();
        this.day = new SimpleIntegerProperty();
        //this.tripId = new SimpleStringProperty();
        //this.owner = new SimpleStringProperty();
        this.price = new SimpleIntegerProperty();
        this.arriveHour = new SimpleIntegerProperty();
        this.arriveMinutes = new SimpleStringProperty();
        this.fuelConsumption = new SimpleDoubleProperty();
    }

    @FXML
    private void initialize() {
        idLabel.textProperty().bind(id.asString());
        nameLabel.textProperty().bind(name);
        startStationLabel.textProperty().bind(startStation);
        endStationLabel.textProperty().bind(endStation);
        timeLabel.textProperty().bind(Bindings.concat(hour, ":", minutes));
        dayLabel.textProperty().bind(day.asString());
        //tripIdLabel.textProperty().bind(tripId.asString());
        //ownerLabel.textProperty().bind(owner);
        priceLabel.textProperty().bind(price.asString());
        arriveTimeLabel.textProperty().bind(Bindings.concat(arriveHour, ":", arriveMinutes));
        fuelConsumptionLabel.textProperty().bind(Bindings.concat(fuelConsumption.asString(), " liters"));
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setStartStation(String startStation) {
        this.startStation.set(startStation);
    }

    public void setEndStation(String endStation) {
        this.endStation.set(endStation);
    }

    public void setHour(int hour) {
        this.hour.set(hour);
    }

    public void setMinutes(int minutes) {
        String sMinutes = String.valueOf(minutes);
        if(minutes < 10)
            this.minutes.setValue("0" + minutes);
        else
            this.minutes.setValue(sMinutes);
    }

    public void setDay(int day) {
        this.day.set(day);
    }

//    public void setTripId(List<Integer> tripsIds)
//    {
//        for(int i=0; i<tripsIds.size(); i++)
//        {
//            this.tripId.set(this.tripId + ", " +String.valueOf(tripsIds.get(i)));
//        }
//    }

//    public void setOwner(List<String> owners)
//    {
//        for(int i=0; i<owners.size(); i++)
//        {
//            this.owner.set(this.owner + ", " + owners.get(i));
//        }
//    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public void setArriveHour(int arriveHour) {
        this.arriveHour.set(arriveHour);
    }

    public void setArriveMinutes(int arriveMinutes) {
        String sMinutes = String.valueOf(arriveMinutes);
        if(arriveMinutes < 10)
            this.arriveMinutes.setValue("0" + arriveMinutes);
        else
            this.arriveMinutes.setValue(sMinutes);
    }

    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption.set(fuelConsumption);
    }

    public void setTextOfTripRequestTimeLabel(String text)
    {
        this.tripRequestTimeLabel.setText(text);
    }

    public void setTextOfOfferedTripTimeLabel(String text)
    {
        this.offeredTripTimeLabel.setText(text);
    }

    public void setOwnersAndIdsComboBox(List<String> owners, List<Integer> ids)
    {
        for(int i=0; i<owners.size();i++)
        {
            ownersAndIdsComboBox.getItems().add(owners.get(i) + ", id: " + String.valueOf(ids.get(i)));
        }
    }


}
