package desktopApplication.components.main;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class LoadXMLController {

    @FXML private ProgressBar taskProgressBar;
    @FXML private Label taskMessageLabel;
    @FXML private Label errorLabel;
    @FXML private Button backButton;


    public LoadXMLController()
    {

    }

    @FXML
    private void initialize()
    {

    }

    public void setErrorLabel(String message)
    {
        this.errorLabel.setText(message);
    }

    public ProgressBar getTaskProgressBar() {
        return taskProgressBar;
    }

    public Label getTaskMessageLabel() {
        return taskMessageLabel;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }


    @FXML
    void backButtonAction(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}

