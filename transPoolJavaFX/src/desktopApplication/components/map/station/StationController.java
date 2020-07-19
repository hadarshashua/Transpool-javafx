package desktopApplication.components.map.station;

import animatefx.animation.Swing;
import animatefx.animation.Wobble;
import desktopApplication.components.map.stationDetails.StationDetailsController;
import desktopApplication.components.map.stationDetails.StationDetailsDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.Supplier;

public class StationController {

    private Supplier<StationDetailsDTO> detailsDTOSupplier;
    private String name;
    private int x;
    private int y;

    @FXML private Circle stationCircle;

    public StationController()
    {

    }

    @FXML
    private void initialize()
    {

    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setName(String stationName) {
        this.name = stationName;

        Tooltip tooltip = new Tooltip(stationName);
        Tooltip.install(this.stationCircle, tooltip);
    }

    public void setDetailsDTOSupplier(Supplier<StationDetailsDTO> detailsDTOSupplier) {
        this.detailsDTOSupplier = detailsDTOSupplier;
    }


    @FXML
    void showStationDetails(MouseEvent event) {
        //new Swing(this.stationCircle).play();

        try {
            StationDetailsDTO stationDetailsDTO = detailsDTOSupplier.get();
            stationDetailsDTO.setName(name);
            stationDetailsDTO.setX(x);
            stationDetailsDTO.setY(y);

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApplication/components/map/stationDetails/stationDetailsView.fxml");
            fxmlLoader.setLocation(url);
            GridPane root = fxmlLoader.load(url.openStream());

            StationDetailsController controller = fxmlLoader.getController();
            controller.setData(stationDetailsDTO);

            Stage details = new Stage();
            details.initModality(Modality.APPLICATION_MODAL);
            final Scene scene = new Scene(root, 200, 200);
            details.setScene(scene);
            details.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
