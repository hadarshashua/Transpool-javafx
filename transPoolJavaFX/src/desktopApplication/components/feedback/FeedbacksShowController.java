package desktopApplication.components.feedback;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class FeedbacksShowController {

    @FXML private Label avgRateLabel;
    @FXML private Label numofRatesLabel;
    @FXML private TextArea feedbacksTextArea;

    protected SimpleStringProperty avgRate;
    protected SimpleStringProperty numRates;
    protected SimpleStringProperty feedbacks;

    public FeedbacksShowController()
    {
        this.avgRate = new SimpleStringProperty();
        this.numRates = new SimpleStringProperty();
        this.feedbacks = new SimpleStringProperty();
        //this.feedbacks.set("No feedbacks yet");
    }

    @FXML
    private void initialize()
    {
        this.avgRateLabel.textProperty().bind(avgRate);
        this.numofRatesLabel.textProperty().bind(numRates);
        this.feedbacksTextArea.textProperty().bind(feedbacks);
        //this.feedbacksTextArea.disableProperty().setValue(true);
    }

    public void setAvgRate(String avgRate) {
        this.avgRate.set(avgRate);
    }

    public void setNumRates(String numRates) {
        this.numRates.set(numRates);
    }

    public void setFeedbacks(String feedbacks) {
        this.feedbacks.set(feedbacks);
    }

    public String getFeedbacks() {
        return feedbacks.get();
    }
}

