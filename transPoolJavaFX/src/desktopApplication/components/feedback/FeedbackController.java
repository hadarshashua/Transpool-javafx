package desktopApplication.components.feedback;

import desktopApplication.components.main.JavaFXController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

public class FeedbackController {


    @FXML private GridPane gridPane;
    @FXML private RadioButton oneRadioButton;
    @FXML private RadioButton twoRadioButton;
    @FXML private RadioButton threeRadioButton;
    @FXML private RadioButton fourRadioButton;
    @FXML private RadioButton fiveRadioButton;
    @FXML private TextArea feedbackTextArea;
    @FXML private Button cancelButton;
    @FXML private Button addFeedbackButton;
    @FXML private Label errorRateLabel;

    protected SimpleBooleanProperty isRateOne;
    protected SimpleBooleanProperty isRateTwo;
    protected SimpleBooleanProperty isRateThree;
    protected SimpleBooleanProperty isRateFour;
    protected SimpleBooleanProperty isRateFive;
    protected SimpleBooleanProperty isRateSelected;

    private JavaFXController javaFXController;
    private List<Integer> indexOfOfferedTrip;
    private List<Integer> indexOfOfferedTripInAccordion;

    public FeedbackController()
    {
        this.isRateOne = new SimpleBooleanProperty(false);
        this.isRateTwo = new SimpleBooleanProperty(false);
        this.isRateThree = new SimpleBooleanProperty(false);
        this.isRateFour = new SimpleBooleanProperty(false);
        this.isRateFive = new SimpleBooleanProperty(false);
        this.isRateSelected = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize()
    {
        oneRadioButton.disableProperty().bind(Bindings.and(isRateSelected, isRateOne.not()));
        twoRadioButton.disableProperty().bind(Bindings.and(isRateSelected, isRateTwo.not()));
        threeRadioButton.disableProperty().bind(Bindings.and(isRateSelected, isRateThree.not()));
        fourRadioButton.disableProperty().bind(Bindings.and(isRateSelected, isRateFour.not()));
        fiveRadioButton.disableProperty().bind(Bindings.and(isRateSelected, isRateFive.not()));
        feedbackTextArea.disableProperty().bind(isRateSelected.not());
    }

    public void setJavaFXController(JavaFXController javaFXController) {
        this.javaFXController = javaFXController;
    }

    public void setIndexOfOfferedTrip(List<Integer> indexOfOfferedTrip) {
        this.indexOfOfferedTrip = indexOfOfferedTrip;
    }

    public void setIndexOfOfferedTripInAccordion(List<Integer> indexOfOfferedTripInAccordion) {
        this.indexOfOfferedTripInAccordion = indexOfOfferedTripInAccordion;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    @FXML
    void addFeedbackButtonAction(ActionEvent event) {
        int rate;
        if(!isRateSelected.getValue())
            this.errorRateLabel.setText("Error: choose rate");
        else
        {
            if(isRateOne.getValue())
               rate = 1;
            else if(isRateTwo.getValue())
                rate = 2;
            else if(isRateThree.getValue())
                rate = 3;
            else if(isRateFour.getValue())
                rate = 4;
            else // rate=5
                rate = 5;

            for(int i=0; i<this.indexOfOfferedTrip.size(); i++)
                this.javaFXController.addFeedbackToOwner(rate, this.feedbackTextArea.getText(), this.indexOfOfferedTrip.get(i), this.indexOfOfferedTripInAccordion.get(i));
        }
        Stage stage = (Stage) addFeedbackButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void cancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void oneRadioButtonAction(ActionEvent event) {
        if(isRateOne.getValue())
        {
            isRateOne.setValue(false);
            isRateSelected.setValue(false);
        }
        else
        {
            isRateOne.setValue(true);
            isRateSelected.setValue(true);
        }

    }

    @FXML
    void twoRadioButtonAction(ActionEvent event) {
        if(isRateTwo.getValue()) {
            isRateTwo.setValue(false);
            isRateSelected.setValue(false);
        }
        else {
            isRateTwo.setValue(true);
            isRateSelected.setValue(true);
        }
    }

    @FXML
    void threeRadioButtonAction(ActionEvent event) {
        if(isRateThree.getValue()) {
            isRateThree.setValue(false);
            isRateSelected.setValue(false);
        }
        else {
            isRateThree.setValue(true);
            isRateSelected.setValue(true);
        }
    }

    @FXML
    void fourRadioButton(ActionEvent event) {
        if(isRateFour.getValue()) {
            isRateFour.setValue(false);
            isRateSelected.setValue(false);
        }
        else {
            isRateFour.setValue(true);
            isRateSelected.setValue(true);
        }
    }

    @FXML
    void fiveRadioButtonAction(ActionEvent event) {
        if(isRateFive.getValue()) {
            isRateFive.setValue(false);
            isRateSelected.setValue(false);
        }
        else {
            isRateFive.setValue(true);
            isRateSelected.setValue(true);
        }
    }

}
