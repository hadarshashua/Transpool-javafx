package desktopApplication.components.map.coordinate;

import com.fxgraph.cells.AbstractCell;
import com.fxgraph.graph.Graph;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.net.URL;
/*
Represents a node (called 'cell' in the fxGraph library) that is the coordinate on the grid
A Coordinate has a tooltip showing her  X ; Y
Coordinate is created from an independent fxml file along with its controller and style sheet
*/
public class CoordinateNode extends AbstractCell {

    private int x;
    private int y;

    public CoordinateNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public Region getGraphic(Graph graph) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("coordinateView.fxml");
            fxmlLoader.setLocation(url);
            VBox root = fxmlLoader.load(url.openStream());

            CoordinateController controller = fxmlLoader.getController();
            controller.setLocation(x, y);

            return root;
        } catch (Exception e) {
            return new Label("Error when tried to create graphic node !");
        }

    }

}
