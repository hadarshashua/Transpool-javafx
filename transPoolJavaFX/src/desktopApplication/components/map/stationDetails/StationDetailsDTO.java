package desktopApplication.components.map.stationDetails;

import java.util.List;

/*
Dummy container to hold information needed to be shown upon clicking a desktopApplication.components.map.station
 */
public class StationDetailsDTO {

    private String name;
    private int x;
    private int y;
    private List<String> passengers;

    public StationDetailsDTO(List<String> passengers) {
        this.passengers = passengers;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getPassengers() {
        return passengers;
    }
}

