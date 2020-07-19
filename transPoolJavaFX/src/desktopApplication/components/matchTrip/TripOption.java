package desktopApplication.components.matchTrip;

import controller.trips.TripPartInfo;
import desktopApplication.components.main.JavaFXController;
import desktopApplication.components.tripRequest.TripRequestController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class TripOption {

    @FXML private TextArea routeDescriptionTextArea;
    @FXML private TextField priceTextField;
    @FXML private TextField timeTextField;
    @FXML private TextField fuelConsumptionTextField;
    @FXML private Button matchButton;
    @FXML private Label timeLabel;

    private JavaFXController javaFXController;
    private TripRequestController tripRequest;
    private List<Integer> indexOfOfferedTrip;
    private List<Integer> indexInAccordion;
    private List<TripPartInfo> tripInfo;

    public TripOption()
    {

    }

    @FXML
    public void initialize()
    {

    }

    public void setJavaFXController(JavaFXController javaFXController) {
        this.javaFXController = javaFXController;
    }

    public void setTripRequest(TripRequestController tripRequest) {
        this.tripRequest = tripRequest;
    }

    public void setIndexOfOfferedTrip(List<Integer> indexOfOfferedTrip) {
        this.indexOfOfferedTrip = indexOfOfferedTrip;
    }

    public void setTripInfo(List<TripPartInfo> tripInfo) {
        this.tripInfo = tripInfo;
    }

    public void setTextRouteDescriptionTextArea(String text) {
        this.routeDescriptionTextArea.setText(text);
    }

    public void setTextPriceTextField(String text) {
        this.priceTextField.setText(text);
    }

    public void setTextTimeTextField(String text) {
        this.timeTextField.setText(text);
    }

    public void setTextFuelConsumptionTextField(String text) {
        this.fuelConsumptionTextField.setText(text);
    }

    public void setIndexInAccordion(List<Integer> indexInAccordion) {
        this.indexInAccordion = indexInAccordion;
    }

    public void setTimeLabel(String text)
    {
        this.timeLabel.setText(text);
    }

    @FXML
    void matchButtonAction(ActionEvent event) {
        this.javaFXController.makeMatch(this.tripRequest.getId(), this.indexOfOfferedTrip, this.tripRequest.getIndexInAccordion(), this.indexInAccordion, tripInfo);
        Stage stage = (Stage) matchButton.getScene().getWindow();
        stage.close();
    }




}

