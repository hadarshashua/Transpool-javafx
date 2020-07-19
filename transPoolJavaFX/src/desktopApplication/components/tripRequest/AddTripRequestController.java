package desktopApplication.components.tripRequest;

import controller.time.Schedual;
import desktopApplication.components.main.JavaFXController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddTripRequestController {


    @FXML private GridPane gridPane;
    @FXML private TextField nameTextField;
    @FXML private TextField dayTextField;
    @FXML private RadioButton departureTimeRadioButton;
    @FXML private RadioButton arrivalTimeRadioButton;
    @FXML private Button cancelButton;
    @FXML private Button addTripButton;
    @FXML private ComboBox<String> firstStationComboBox;
    @FXML private ComboBox<String> lastStationComboBox;
    @FXML private ComboBox<String> hourComboBox;
    @FXML private ComboBox<String> minutesComboBox;
    @FXML private Label errorNameLabel;
    @FXML private Label errorFirstStationLabel;
    @FXML private Label errorLastStationLabel;
    @FXML private Label errorDayLabel;
    @FXML private Label errorTimeLabel;
    @FXML private Label errorSearchByLabel;
    @FXML private Spinner<Integer> daySpinner;

    protected SimpleBooleanProperty isDepartureTime;
    protected SimpleBooleanProperty isArrivalTime;

    private JavaFXController javaFXController;

    ObservableList<String> hoursList = FXCollections.observableArrayList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
    ObservableList<String> minutesList = FXCollections.observableArrayList("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55");

    public AddTripRequestController()
    {
        this.isArrivalTime = new SimpleBooleanProperty(false);
        this.isDepartureTime = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize()
    {
        hourComboBox.setItems(hoursList);
        minutesComboBox.setItems(minutesList);

        departureTimeRadioButton.disableProperty().bind(isArrivalTime);
        arrivalTimeRadioButton.disableProperty().bind(isDepartureTime);

        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000,1);
        daySpinner.setValueFactory(spinnerValueFactory);
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public void setJavaFXController(JavaFXController javaFXController) {
        this.javaFXController = javaFXController;
    }

    @FXML
    void addTripButtonAction(ActionEvent event) {
        int hour, minutes;
        boolean isNameValid, isFirstStationValid, isLastStationValid, isStationsDifferent, isHourValid, isMinutesValid, isSearchByValid = true;
        String name = nameTextField.getText();
        String firstStation = firstStationComboBox.getValue();
        String lastStation = lastStationComboBox.getValue();
        int day = daySpinner.getValue();
        //String sDay = dayTextField.getText();
        String sHour = hourComboBox.getValue();
        String sMinutes = minutesComboBox.getValue();

        errorNameLabel.setText("");
        errorFirstStationLabel.setText("");
        errorLastStationLabel.setText("");
        //errorDayLabel.setText("");
        errorTimeLabel.setText("");
        errorSearchByLabel.setText("");

        Schedual.StartOrArrive startOrArrive = Schedual.StartOrArrive.START; //check this!!
        if(isDepartureTime.getValue())
            startOrArrive = Schedual.StartOrArrive.START;
        else if(isArrivalTime.getValue())
            startOrArrive = Schedual.StartOrArrive.ARRIVE;
        else {
            errorSearchByLabel.setText("Error: choose option");
            isSearchByValid = false;
        }

        isNameValid = checkName(name);
        isFirstStationValid = checkFirstStation(firstStation);
        isLastStationValid = checkLastStation(lastStation);
        isStationsDifferent = checkStations(firstStation, lastStation);
        //isDayValid = checkDay(sDay);
        isHourValid = checkHour(sHour);
        isMinutesValid = checkMinutes(sMinutes);

        if(isNameValid && isFirstStationValid && isLastStationValid && isStationsDifferent && isHourValid && isMinutesValid && isSearchByValid)
        {
            //day = Integer.parseInt(sDay);
            hour = Integer.parseInt(sHour);
            minutes = Integer.parseInt(sMinutes);

            this.javaFXController.makeNewTripRequest(name, firstStation, lastStation, day, hour, minutes, startOrArrive);

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }

    private boolean checkName(String name)
    {
        boolean res = true;

        if(name.equals(""))
        {
            errorNameLabel.setText("Error: enter name");
            res = false;
        }
        return res;
    }

    private boolean checkFirstStation(String firstStation)
    {
        boolean res = true;

        if(firstStation == null)
        {
            errorFirstStationLabel.setText("Error: choose station");
            res = false;
        }
        return res;
    }

    private boolean checkLastStation(String lastStation)
    {
        boolean res = true;

        if(lastStation == null)
        {
            errorLastStationLabel.setText("Error: choose station");
            res = false;
        }
        return res;
    }

    private boolean checkStations(String firstStation, String lastStation)
    {
        boolean res = true;
        if(checkFirstStation(firstStation) && checkLastStation(lastStation))
            if(firstStation.equals(lastStation)){
                errorLastStationLabel.setText("Error: choose different station from " + firstStation);
                res = false;
            }
        return res;
    }

//    private boolean checkDay(String day)
//    {
//        boolean res = true;
//
//        if(day.equals(""))
//        {
//            errorDayLabel.setText("error: enter day");
//            res = false;
//        }
//        return res;
//    }

    private boolean checkHour(String hour)
    {
        boolean res = true;

        if(hour == null)
        {
            errorTimeLabel.setText("Error: choose hour");
            res = false;
        }
        return res;
    }

    private boolean checkMinutes(String minutes)
    {
        boolean res = true;

        if(minutes == null)
        {
            errorTimeLabel.setText("Error: choose minutes");
            res = false;
        }
        return res;
    }

    @FXML
    void cancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void arrivalTimeRadioButtonAction(ActionEvent event) {
        if(isArrivalTime.getValue())
            isArrivalTime.setValue(false);
        else
            isArrivalTime.setValue(true);
    }

    @FXML
    void departureTimeRadioButtonAction(ActionEvent event) {
        if(isDepartureTime.getValue())
            isDepartureTime.setValue(false);
        else
            isDepartureTime.setValue(true);
    }


    public ComboBox<String> getFirstStationComboBox() {
        return firstStationComboBox;
    }

    public ComboBox<String> getLastStationComboBox() {
        return lastStationComboBox;
    }
}

