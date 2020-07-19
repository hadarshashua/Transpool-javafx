package controller.time;

public class Time
{
    private int hour, minutes;

    public Time() { }

    public Time(int hour, int minutes)
    {
        this.hour = hour;
        this.minutes = minutes;
    }

    public Time(Time other)
    {
        this.hour = other.hour;
        this.minutes = other.minutes;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
