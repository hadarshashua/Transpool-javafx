package controller.time;

public class Schedual
{
    public enum StartOrArrive {START, ARRIVE}

    private Time time;
    private int day;
    StartOrArrive startOrArrive;

    public Schedual(int hour, int minutes, int day, StartOrArrive startOrArrive)
    {
        this.time = new Time(hour, minutes);
        this.day = day;
        this.startOrArrive = startOrArrive;
    }

    public Schedual(Schedual other)
    {
        this.time = new Time(other.time);
        this.day = other.day;
        this.startOrArrive = other.startOrArrive;
    }

    public Time getTime() {
        return time;
    }

    public int getDay() {
        return day;
    }

    public StartOrArrive getStartOrArrive() {
        return startOrArrive;
    }
}
