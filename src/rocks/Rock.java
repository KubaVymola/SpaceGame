package rocks;

import fighters.Cannon;
import game.PhysicalObject;
import interfaces.IDamagable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Rock extends PhysicalObject implements IDamagable {
    private ArrayList<Cannon> cannons;
    private ArrayList<Rock> childBodies;
    private double mass;

    private double rotationSpeed;

    public Rock(double posX, double posY, double angle, double radius, double mass, String spriteSource) {
        this(posX, posY, angle, radius, 0, 0, mass, spriteSource);
    }

    public Rock(double posX, double posY, double angle, double radius, double speedX, double speedY, double mass, String spriteSourceAbsolute) {
        super(posX, posY, angle, radius, speedX, speedY, spriteSourceAbsolute);

        this.mass = mass;
        this.rotationSpeed = 0.02;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    @Override
    public void update(double deltaSecond) {
        super.update(deltaSecond);

        super.rotate(this.rotationSpeed);
    }
}
