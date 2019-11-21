package physics;

import java.util.ArrayList;

public class Orbit {
    public static double orbitRadiusMargin = 50;
    public static double orbitSpeedMargin = 0.7;
    public static double orbitAngleMargin = 0.7;

    private double radius;
    private double speed;
    private boolean isOccupied;

    public Orbit(double radius, double speed) {
        this.radius = radius;
        this.speed = speed;
        this.isOccupied = false;
    }

    public double getRadius() {
        return radius;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isOccupied()
    {
        return isOccupied;
    }

    public void setOccupied(boolean occupied)
    {
        isOccupied = occupied;
    }
}
