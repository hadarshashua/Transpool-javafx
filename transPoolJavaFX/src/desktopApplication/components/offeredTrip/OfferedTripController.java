package desktopApplication.components.offeredTrip;

import controller.passenger.Passenger;
import controller.trips.Trip;
import desktopApplication.components.feedback.FeedbackController;
import desktopApplication.components.feedback.FeedbacksShowController;
import desktopApplication.components.main.JavaFXController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class OfferedTripController {

    @FXML private Label idLabel;
    @FXML private Label ownerLabel;
    @FXML private Label routeLabel;
    @FXML private Label priceLabel;
    @FXML private Label startTimeLabel;
    @FXML private Label arriveTimeLabel;
    @FXML private Label fuelConsumptionLabel;
    @FXML private Label dayLabel;
    @FXML private Label recurrencesLabel;
    @FXML private Label capacityLabel;
    @FXML private Button feedbacksButton;


    protected SimpleIntegerProperty id;
    protected SimpleStringProperty owner;
    protected SimpleStringProperty route;
    protected SimpleIntegerProperty price;
    protected SimpleIntegerProperty hour;
    protected SimpleStringProperty minutes;
    protected SimpleIntegerProperty arriveHour;
    protected SimpleStringProperty arriveMinutes;
    protected SimpleDoubleProperty fuelConsumption;
    protected SimpleIntegerProperty day;
    protected SimpleStringProperty recurrences;
    protected SimpleIntegerProperty capacity;

    private int indexInAccordion;
    private int indexOfOfferedTrip;
    private JavaFXController javaFXController;


    public OfferedTripController()
    {
        this.id = new SimpleIntegerProperty();
        this.owner = new SimpleStringProperty();
        this.route = new SimpleStringProperty();
        this.price = new SimpleIntegerProperty();
        this.hour = new SimpleIntegerProperty();
        this.minutes = new SimpleStringProperty();
        this.arriveHour = new SimpleIntegerProperty();
        this.arriveMinutes = new SimpleStringProperty();
        this.fuelConsumption = new SimpleDoubleProperty();
        this.day = new SimpleIntegerProperty();
        this.recurrences = new SimpleStringProperty();
        this.capacity = new SimpleIntegerProperty();
    }

    @FXML
    private void initialize() {
        idLabel.textProperty().bind(id.asString());
        ownerLabel.textProperty().bind(owner);
        routeLabel.textProperty().bind(route);
        priceLabel.textProperty().bind(price.asString());
        startTimeLabel.textProperty().bind(Bindings.concat(hour, ":", minutes));
        arriveTimeLabel.textProperty().bind(Bindings.concat(arriveHour, ":", arriveMinutes));
        fuelConsumptionLabel.textProperty().bind(Bindings.concat(fuelConsumption.asString(), " liters"));
        dayLabel.textProperty().bind(day.asString());
        recurrencesLabel.textProperty().bind(recurrences);
        capacityLabel.textProperty().bind(capacity.asString());
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setOwner(String owner) {
        this.owner.set(owner);
    }

    public void setRoute(String route) {
        this.route.set(route);
    }

    public void setPrice(int price) {
        this.price.set(price);
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

    public void setDay(int day) {
        this.day.set(day);
    }

    public void setRecurrences(String recurrences) {
        this.recurrences.set(recurrences);
    }

    public void setCapacity(int capacity) {
        this.capacity.set(capacity);
    }

    public void setIndexInAccordion(int indexInAccordion)
    {
        this.indexInAccordion = indexInAccordion;
    }

    public void setIndexOfOfferedTrip(int indexOfOfferedTrip) {
        this.indexOfOfferedTrip = indexOfOfferedTrip;
    }

    public void setJavaFXController(JavaFXController javaFXController) {
        this.javaFXController = javaFXController;
    }

//    public void addPassengerToPassengersMenuButton(Passenger newPassenger)
//    {
//        MenuItem newItem = new MenuItem();
//        newItem.setText(newPassenger.getName());
//        this.passengersMenuButton.getItems().add(newItem);
//    }

    @FXML
    void feedbacksButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApplication/components/feedback/feedbacksShow.fxml");
            loader.setLocation(url);
            Parent root = loader.load();
            FeedbacksShowController feedbacksShowController = loader.getController();
            Trip trip = this.javaFXController.getMainController().getAllOfferdTrips().get(this.indexOfOfferedTrip);
            feedbacksShowController.setAvgRate(String.valueOf(trip.getFeedbacks().getAvgRate()));
            feedbacksShowController.setNumRates(String.valueOf(trip.getFeedbacks().getNumOfRates()));
            for(int i=0; i<trip.getFeedbacks().getFeedbacks().size(); i++)
                if(feedbacksShowController.getFeedbacks()!= null)
                    feedbacksShowController.setFeedbacks(feedbacksShowController.getFeedbacks() + "\n" +trip.getFeedbacks().getFeedbacks().get(i));
                else
                    feedbacksShowController.setFeedbacks(trip.getFeedbacks().getFeedbacks().get(i));

            Stage stage = new Stage();
            stage.setTitle("Show feedback stage");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(JavaFXController.getPrimaryStage());
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

