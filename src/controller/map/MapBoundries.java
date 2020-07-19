package controller.map;

public class MapBoundries
{
    private int length, width;

    public MapBoundries() { }

    public MapBoundries(int length, int width)
    {
        this.length = length;
        this.width = width;
    }

    public MapBoundries(MapBoundries other)
    {
        this.length = other.length;
        this.width = other.width;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }
}
