import java.util.Objects;

public class Point {
    public int x;
    public int y;
    public Point neighborLeft;
    public Point neighborRight;
    public Point neighborUp;
    public Point neighborBottom;
    public String name;

    Point(int x, int y, String name){
        this.x = x;
        this.y = y;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public String toString() {
        return "Point " + "x=" + x + ", y=" + y + ", name=" + name;

    }
}