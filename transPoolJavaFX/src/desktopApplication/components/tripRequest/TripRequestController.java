package desktopApplication.components.tripRequest;

import desktopApplication.components.main.JavaFXController;
import desktopApplication.components.matchTrip.MatchTripController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class TripRequestController {

    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label startStationLabel;
    @FXML private Label endStationLabel;
    @FXML private Label timeLabel;
    @FXML private Label dayLabel;
    @FXML private Label timeTextLabel;
    @FXML private Button makeMatchButton;


    protected SimpleIntegerProperty id;
    protected SimpleStringProperty name;
    protected SimpleStringProperty startStation;
    protected SimpleStringProperty endStation;
    protected SimpleIntegerProperty hour;
    protected SimpleStringProperty minutes;
    protected SimpleIntegerProperty day;

    private JavaFXController javaFXController;
    private int indexInAccordion;
    private boolean isStartTime;

    public TripRequestController()
    {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.startStation = new SimpleStringProperty();
        this.endStation = new SimpleStringProperty();
        this.hour = new SimpleIntegerProperty();
        this.minutes = new SimpleStringProperty();
        this.day = new SimpleIntegerProperty();
    }

    @FXML
    private void initialize() {
        idLabel.textProperty().bind(id.asString());
        nameLabel.textProperty().bind(name);
        startStationLabel.textProperty().bind(startStation);
        endStationLabel.textProperty().bind(endStation);
        timeLabel.textProperty().bind(Bindings.concat(hour, ":", minutes));
        dayLabel.textProperty().bind(day.asString());
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

    public void setJavaFXController(JavaFXController javaFXController) {
        this.javaFXController = javaFXController;
    }

    public void setIndexInAccordion(int indexInAccordion) {
        this.indexInAccordion = indexInAccordion;
    }

    public void setTextTimeTextLabel(String text)
    {
        this.timeTextLabel.setText(text);
    }

    public void setIsStartTime(boolean isStartTime)
    {
        this.isStartTime = isStartTime;
    }

    public boolean isStartTime() {
        return isStartTime;
    }

    public int getId() {
        return id.get();
    }

    public String getStartStation() {
        return startStation.get();
    }

    public String getEndStation() {
        return endStation.get();
    }

    public int getIndexInAccordion() {
        return indexInAccordion;
    }


    @FXML
    void makeMatchButtonAction(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApplication/components/matchTrip/matchTrip.fxml");
            loader.setLocation(url);
            Parent root = loader.load();
            MatchTripController matchTripController = loader.getController();
            matchTripController.setJavaFXController(this.javaFXController);
            matchTripController.setTripRequest(this);
            matchTripController.getBorderPane().getStylesheets().clear();
            matchTripController.getBorderPane().getStylesheets().add(this.javaFXController.getCurrTheme());
            matchTripController.setStartTime(this.isStartTime);

            Stage stage = new Stage();
            stage.setTitle("Add ride request stage");
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
