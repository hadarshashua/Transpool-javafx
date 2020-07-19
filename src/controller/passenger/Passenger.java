package controller.passenger;

public class Passenger
{
    private String name;
    private static int numOfPassengers = 1;
    private int id;

    public Passenger() { }

    public Passenger(String name)
    {
        this.id = numOfPassengers;
        numOfPassengers++;
        this.name = name;
    }

    public Passenger(Passenger other)
    {
        this.id = other.id;
        this.name = other.name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
