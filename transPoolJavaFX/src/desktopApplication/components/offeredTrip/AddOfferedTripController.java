package desktopApplication.components.offeredTrip;

import controller.time.Schedual;
import desktopApplication.components.main.JavaFXController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddOfferedTripController {

    @FXML private GridPane gridPane;
    @FXML private TextField ownerNameTextField;
    @FXML private TextField tripRouteTextField;
    @FXML private TextField dayTextField;
    @FXML private ComboBox<String> hourComboBox;
    @FXML private ComboBox<String> minutesComboBox;
    @FXML private ComboBox<String> recurrencesComboBox;
    @FXML private TextField ppkTextField;
    @FXML private TextField capacityTextField;
    @FXML private Button cancelButton;
    @FXML private Button addButton;
    @FXML private Label errorNameLabel;
    @FXML private Label errorRouteLabel;
    @FXML private Label errorDayLabel;
    @FXML private Label errorTimeLabel;
    @FXML private Label errorPpkLabel;
    @FXML private Label errorCapacityLabel;
    @FXML private Spinner<Integer> daySpinner;
    @FXML private Spinner<Integer> ppkSpinner;
    @FXML private Spinner<Integer> capacitySpinner;

    private JavaFXController javaFXController;

    ObservableList<String> recurrencesList = FXCollections.observableArrayList("One Time", "Daily", "BiDaily", "Weekly", "Monthly");
    ObservableList<String> hoursList = FXCollections.observableArrayList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
    ObservableList<String> minutesList = FXCollections.observableArrayList("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55");

    public AddOfferedTripController()
    {

    }

    @FXML
    private void initialize()
    {
        recurrencesComboBox.setValue("One Time");
        recurrencesComboBox.setItems(recurrencesList);

        hourComboBox.setItems(hoursList);
        minutesComboBox.setItems(minutesList);

        SpinnerValueFactory<Integer> daySpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000,1);
        daySpinner.setValueFactory(daySpinnerValueFactory);

        SpinnerValueFactory<Integer> ppkSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000,1);
        ppkSpinner.setValueFactory(ppkSpinnerValueFactory);

        SpinnerValueFactory<Integer> capacitySpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000,4);
        capacitySpinner.setValueFactory(capacitySpinnerValueFactory);
    }

    public void setJavaFXController(JavaFXController javaFXController) {
        this.javaFXController = javaFXController;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    @FXML
    void addButtonAction(ActionEvent event)
    {
        int hour, minutes;
        boolean isOwnerValid, isRouteValid, isHourValid, isMinutesValid;

        String owner = ownerNameTextField.getText();
        String route = tripRouteTextField.getText();
        int day = daySpinner.getValue();
        String sHour = hourComboBox.getValue();
        String sMinutes = minutesComboBox.getValue();
        String recurrences = recurrencesComboBox.getValue();
        int ppk = ppkSpinner.getValue();
        int capacity = capacitySpinner.getValue();

        errorNameLabel.setText("");
        errorRouteLabel.setText("");
        errorDayLabel.setText("");
        errorTimeLabel.setText("");
        errorPpkLabel.setText("");
        errorCapacityLabel.setText("");

        isOwnerValid = checkOwner(owner);
        isRouteValid = checkRoute(route);
        //isDayValid = checkDay(sDay);
        isHourValid = checkHour(sHour);
        isMinutesValid = checkMinutes(sMinutes);
        //isPpkValid = checkPpk(sPpk);
        //isCapacityValid = checkCapacity(sCapacity);

        if(isOwnerValid && isRouteValid && isHourValid && isMinutesValid)
        {
            //day = Integer.parseInt(sDay);
            hour = Integer.parseInt(sHour);
            minutes = Integer.parseInt(sMinutes);
            //ppk = Integer.parseInt(sPpk);
            //capacity = Integer.parseInt(sCapacity);

            this.javaFXController.makeNewOfferedTrip(owner, route, day, hour, minutes, recurrences, ppk, capacity);

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }

    private boolean checkOwner(String name)
    {
        boolean res = true;

        if(name.equals(""))
        {
            errorNameLabel.setText("Error: enter name");
            res = false;
        }
        return res;
    }

    private boolean checkRoute(String route)
    {
        boolean res = true;

        if(route.equals(""))
        {
            errorRouteLabel.setText("Error: enter route");
            res = false;
        }
        else if(!this.javaFXController.areStationsExist(route))
        {
            errorRouteLabel.setText("Error: station not exists");
            res = false;
        }
        else if(!this.javaFXController.arePathsExist(route))
        {
            errorRouteLabel.setText("Error: path not exists");
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

//    private boolean checkPpk(String ppk)
//    {
//        boolean res = true;
//
//        if(ppk.equals(""))
//        {
//            errorPpkLabel.setText("error: enter ppk");
//            res = false;
//        }
//        return res;
//    }
//
//    private boolean checkCapacity(String capacity)
//    {
//        boolean res = true;
//
//        if(capacity.equals(""))
//        {
//            errorCapacityLabel.setText("error: enter capacity");
//            res = false;
//        }
//        return res;
//    }

    @FXML
    void cancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
