package physics;

import java.util.ArrayList;

public class Orbit {
    private double radius;
    private double speed;

    public Orbit(double radius, double speed) {
        this.radius = radius;
        this.speed = speed;
    }

    public double getRadius() {
        return radius;
    }

    public double getSpeed() {
        return speed;
    }
}
