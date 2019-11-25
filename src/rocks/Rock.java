package rocks;

import fighters.Cannon;
import game.Game;
import game.PhysicalObject;
import interfaces.IDamagable;
import physics.Orbit;
import physics.Physics;
import physics.Vec2d;

import java.util.ArrayList;

public class Rock extends PhysicalObject implements IDamagable {

    private ArrayList<Cannon> cannons;
    private ArrayList<Rock> childBodies;
    private ArrayList<Orbit> orbits;
    private Rock parent;

    private double mass;
    private double rotationSpeed;

    private int parentOrbitIndex;
    private int rockTypeIndex;

    private boolean clockwise;


    public Rock(double centerX, double centerY, double angle, double speedX, double speedY, int rockTypeIndex) {
        super(centerX, centerY, angle, RockType.rockTypes.get(rockTypeIndex).getRadius(), speedX, speedY,
                RockType.rockTypes.get(rockTypeIndex).getTextureName());

        this.orbits = new ArrayList<>();
        this.childBodies = new ArrayList<>();
        this.cannons = new ArrayList<>();

        this.rockTypeIndex = rockTypeIndex;
        if(this.rockTypeIndex != 0)
            this.addScore(Game.r.nextInt((int)RockType.rockTypes.get(this.rockTypeIndex).getToNext() -
                    (int)RockType.rockTypes.get(this.rockTypeIndex - 1).getToNext()) +
                    (int)RockType.rockTypes.get(this.rockTypeIndex - 1).getToNext());

        this.rotationSpeed = 0.02;
        this.parentOrbitIndex = -1;
        this.parent = null;
        this.mass = RockType.rockTypes.get(this.rockTypeIndex).getMass();

        this.calculateOrbits();
    }

    public void reload()
    {
        this.loadImage(RockType.rockTypes.get(this.rockTypeIndex).getTextureName());

        this.setRadius(RockType.rockTypes.get(this.rockTypeIndex).getRadius());
        this.mass = RockType.rockTypes.get(this.rockTypeIndex).getMass();
        this.dropAsteroids();
        this.calculateOrbits();
    }

    private void dropAsteroids()
    {
        if(this.rockTypeIndex != RockType.starsFrom)
            return;

        for(int i = this.childBodies.size() - 1; i >= 0; i--)
        {
            this.childBodies.get(i).removeFromOrbit();
            //this.childBodies.remove(i);
        }
    }

    private void calculateOrbits()
    {
        this.orbits = new ArrayList<>();

        for(int i = 0; i < RockType.rockTypes.get(this.getRockTypeIndex()).getOrbits(); i++)
        {
            double radius = this.getRadius() * 3 + i * 2 * this.getRadius();
            double velocity = Math.sqrt(Physics.gravitationConstant * this.getMass() / radius);

            orbits.add(new Orbit(radius, velocity));

            for(Rock child : this.childBodies)
            {
                if(child.parentOrbitIndex == i)
                    orbits.get(i).setOccupied(true);
            }
        }
    }

    public boolean eatSmallestChild()
    {
        if(this.childBodies.isEmpty()) //has no children, will be eaten
            return false;

        Rock smallestDirectChild = this.childBodies.get(this.childBodies.size() - 1);
        for(int i = this.childBodies.size() - 2; i >= 0; i--)
        {
            if(this.childBodies.get(i).getMass() < smallestDirectChild.getMass())
                smallestDirectChild = this.childBodies.get(i);
        }

        if(!smallestDirectChild.eatSmallestChild()) //child has children, will be eaten
        {
            smallestDirectChild.removeFromOrbit();
            this.addScore((int)smallestDirectChild.getMass());
            smallestDirectChild.destroy();
            this.checkIncreaseRockTypeIndex();
        }
        return true;
    }

    public double getMass()
    {
        return mass;
    }

    public int getRockTypeIndex()
    {
        return rockTypeIndex;
    }

    public ArrayList<Orbit> getOrbits()
    {
        return orbits;
    }

    public ArrayList<Rock> getChildBodies()
    {
        return childBodies;
    }

    public void setMass(double mass)
    {
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
    public boolean isInOrbit()
    {
        return this.parentOrbitIndex != -1;
    }

    public void setRotationSpeed(double newRotationSpeed)
    {
        this.rotationSpeed = newRotationSpeed;
    }

    public void checkIncreaseRockTypeIndex()
    {
        if(this.getScore() < RockType.rockTypes.get(this.getRockTypeIndex()).getToNext())
            return;

        this.rockTypeIndex++;
        this.reload();
    }
    public void checkDecreaseRockTypeIndex()
    {
        if(this.getScore() >= RockType.rockTypes.get(this.rockTypeIndex - 1).getToNext())
            return;
        
        for(int i = this.childBodies.size() - 1; i >= 0; i--)
        {
            this.childBodies.get(i).removeFromOrbit();
        }

        this.rockTypeIndex--;
        this.reload();
    }

    public void addDmg(int dmg)
    {
        this.addScore(dmg);

        if(this.isAlive() && this.rockTypeIndex > 0)
        {
            checkDecreaseRockTypeIndex();
        }
    }

    public void moveToOrbit(Rock parent, int parentOrbitIndex, boolean clockwise)
    {
        this.parent = parent;
        this.parentOrbitIndex = parentOrbitIndex;
        this.clockwise = clockwise;
        this.setSpeedX(0);
        this.setSpeedY(0);
        this.normalizeDistance();
    }
    public void removeFromOrbit()
    {
        this.parent.childRemovedFromOrbit(this, parentOrbitIndex);

        this.parentOrbitIndex = -1;
        this.parent = null;
    }

    public void childRemovedFromOrbit(Rock childBody, int orbitIndex)
    {
        this.orbits.get(orbitIndex).setOccupied(false);
        this.childBodies.remove(childBody);
    }

    private void moveInOrbit()
    {
        //this.moveXY(this.parent.getSpeedVector());
        this.setSpeedX(this.parent.getSpeedX());
        this.setSpeedY(this.parent.getSpeedY());

        Orbit orbit = this.parent.getOrbits().get(this.parentOrbitIndex);
        Vec2d relativePos = this.getCenter().subtract(this.parent.getCenter());

        Vec2d velocity = new Vec2d(Math.abs((relativePos.getY() / orbit.getRadius()) * orbit.getSpeed()),
                Math.abs((relativePos.getX() / orbit.getRadius()) * orbit.getSpeed()));

        if((this.clockwise && relativePos.getX() < 0) || (!this.clockwise && relativePos.getX() > 0))
            velocity.setY(-velocity.getY());
        if((this.clockwise && relativePos.getY() > 0) || (!this.clockwise && relativePos.getY() < 0))
            velocity.setX(-velocity.getX());

        this.setSpeedX(this.getSpeedX() + velocity.getX());
        this.setSpeedY(this.getSpeedY() + velocity.getY());
    }

    private void normalizeDistance()
    {
        Vec2d currentRelativePos = this.getCenter().subtract(this.parent.getCenter());
        Vec2d relativePosNorm = currentRelativePos.normalize();
        Vec2d newRelativePos = relativePosNorm.multiply(this.parent.getOrbits().get(this.parentOrbitIndex).getRadius());

        Vec2d tweakVec = newRelativePos.subtract(currentRelativePos).multiply(1d / 20);

        this.moveXY(tweakVec);

        //this.setPosX(this.parent.getCenterX() + newRelativePos.getX());
       // this.setPosY(this.parent.getCenterY() + newRelativePos.getY());
    }

    private void checkCaptureOrbit(ArrayList<Rock> allRocks)
    {
        for(Rock r : allRocks)
        {
            if(r == this)
                continue;

            if(r.parent != null)
                continue;

            if(r.isPlayer())
                continue;

            if(r.isDestroyed())
                continue;

            if((this.isPlanet() && !r.isAsteroid() || (this.isStar() && !r.isPlanet())))
                continue;

            Vec2d relativePos = r.getCenter().subtract(this.getCenter());
            Vec2d relativeSpeedVect = r.getSpeedVector().subtract(this.getSpeedVector());
            double distance = relativePos.getSize();
            double relativeSpeedSize = relativeSpeedVect.getSize();
            double cosAngle = Vec2d.dot(relativePos, relativeSpeedVect) / (distance * relativeSpeedSize);

            for(int i = 0; i < this.orbits.size(); i++)
            {
                if(this.orbits.get(i).isOccupied())
                    continue;

                if(this.orbits.get(i).getRadius() - Orbit.orbitRadiusMargin > distance ||
                        this.orbits.get(i).getRadius() + Orbit.orbitRadiusMargin < distance)
                    continue;

                if(this.orbits.get(i).getSpeed() * Orbit.orbitSpeedMargin > relativeSpeedSize ||
                        this.orbits.get(i).getSpeed() / Orbit.orbitSpeedMargin < relativeSpeedSize)
                    continue;

                //if(Math.abs(cosAngle) > Orbit.orbitAngleMargin)
                //    continue;

                //All checks good; adding to orbit
                r.moveToOrbit(this, i, true);
                this.childBodies.add(r);
                this.orbits.get(i).setOccupied(true);
                break;
            }
        }
    }

    public void update(double deltaSecond, ArrayList<Rock> allRocks)
    {
        if(this.isDestroyed())
            return;

        if(this.isInOrbit())
            this.moveInOrbit();

        super.moveSpeed();

        if(this.isInOrbit())
            this.normalizeDistance();

        if(this.isStar() || this.isPlanet())
        {
            checkCaptureOrbit(allRocks);
        }

        super.rotate(this.rotationSpeed);
    }
}
