package desktopApplication.components.map.stationDetails;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class StationDetailsController {

    private final String STATION_NAME_FORMAT = "%s ( %s , %s)";

    @FXML private Label nameLabel;
    @FXML private TextArea passengersTextArea;

    public void setData(StationDetailsDTO data) {
        nameLabel.setText(String.format(STATION_NAME_FORMAT, data.getName(), String.valueOf(data.getX()), String.valueOf(data.getY())));
        for(int i=0; i<data.getPassengers().size(); i++)
            if(passengersTextArea.getText()!="")
                passengersTextArea.setText(passengersTextArea.getText() + data.getPassengers().get(i) + "\n");
            else
                passengersTextArea.setText(data.getPassengers().get(i) + "\n" );
    }
}
