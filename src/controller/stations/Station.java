package controller.stations;

public class Station
{
    private int x,y;
    private String name;

    public Station() { }

    public Station(int x, int y, String name)
    {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public Station(Station other)
    {
        this.x = other.x;
        this.y = other.y;
        this.name = other.name;
    }

    public String getName()
    {
        return name;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
