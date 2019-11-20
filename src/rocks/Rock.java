package rocks;

import fighters.Cannon;
import game.PhysicalObject;
import interfaces.IDamagable;
import physics.Orbit;
import physics.Physics;

import java.util.ArrayList;

public class Rock extends PhysicalObject implements IDamagable {

    private ArrayList<Cannon> cannons;
    private ArrayList<Rock> childBodies;
    private ArrayList<Orbit> orbits;

    private double mass;
    private double rotationSpeed;

    private int rockTypeIndex;

    private boolean destroyed;


    public Rock(double centerX, double centerY, double angle, double speedX, double speedY, int rockTypeIndex) {
        super(centerX, centerY, angle, RockType.rockTypes.get(rockTypeIndex).getRadius(), speedX, speedY,
                RockType.rockTypes.get(rockTypeIndex).getTextureName());

        this.rockTypeIndex = rockTypeIndex;

        this.rotationSpeed = 0.02;
        this.destroyed = false;

        this.mass = RockType.rockTypes.get(this.rockTypeIndex).getMass();
        this.calculateOrbits();
    }

    public void reload()
    {
        this.loadImage(RockType.rockTypes.get(this.rockTypeIndex).getTextureName());

        this.setRadius(RockType.rockTypes.get(this.rockTypeIndex).getRadius());
        this.mass = RockType.rockTypes.get(this.rockTypeIndex).getMass();
        this.calculateOrbits();
    }

    private void calculateOrbits()
    {
        this.orbits.clear();

        for(int i = 0; i < RockType.rockTypes.get(this.getRockTypeIndex()).getOrbits(); i++)
        {
            double radius = this.getRadius() * 2 + i * this.getRadius();
            double velocity = Math.sqrt(Physics.gravitationConstant * this.getMass() / radius);

            orbits.add(new Orbit(radius, velocity));
        }
    }

    public double getMass() {
        return mass;
    }

    public int getRockTypeIndex() {
        return rockTypeIndex;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public boolean isAsteroid()
    {
        return this.rockTypeIndex < RockType.planetsFrom;
    }
    public boolean isPlanet()
    {
        return this.rockTypeIndex >= RockType.planetsFrom && this.rockTypeIndex <= RockType.planetsTo;
    }
    public boolean isStar()
    {
        return this.rockTypeIndex >= RockType.starsFrom && this.rockTypeIndex <= RockType.starsTo;
    }
    public boolean isBlackHole()
    {
        return this.rockTypeIndex > RockType.starsTo;
    }
    public boolean isPlayer()
    {
        return false;
    }

    public void destroy()
    {
        this.destroyed = true;
    }
    public boolean isDestroyed()
    {
        return this.destroyed;
    }

    public void setRotationSpeed(double newRotationSpeed)
    {
        this.rotationSpeed = newRotationSpeed;
    }

    public void increaseRockTypeIndex()
    {
        this.rockTypeIndex++;
        this.reload();
    }

    @Override
    public void update(double deltaSecond) {
        super.update(deltaSecond);

        super.rotate(this.rotationSpeed);
    }
}
