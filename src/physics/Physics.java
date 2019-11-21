package physics;

import game.Game;
import game.PhysicalObject;
import rocks.Rock;
import rocks.RockType;

import java.util.ArrayList;

public class Physics {
    public static final double gravitationConstant = 0.02;
    private final double bounceConstant = 1;
    private final double restitution = 0.9;
    private final double mergeThreshold = 0.2d;

    public void updatePhysics(ArrayList<PhysicalObject> spaceObjects, ArrayList<Rock> rocks)
    {
        updateGravity(rocks);
        updateCollisions(spaceObjects);
    }

    private void updateGravity(ArrayList<Rock> rocks)
    {
        for(int i = 0; i < rocks.size(); i++)
        {
            for(int y = i + 1; y < rocks.size(); y++)
            {
                double objectDistance = getObjectDistance(rocks.get(i), rocks.get(y));
                Vec2d multi = rocks.get(i).getCenter().subtract(rocks.get(y).getCenter()).multiply(1 / objectDistance);

                double actingForce = (Physics.gravitationConstant * (rocks.get(i).getMass() * rocks.get(y).getMass())) /
                        Math.pow(objectDistance, 2);

                if(!rocks.get(i).isInOrbit())
                {
                    rocks.get(i).changeSpeedX(-(actingForce / rocks.get(i).getMass()) * multi.getX());
                    rocks.get(i).changeSpeedY(-(actingForce / rocks.get(i).getMass()) * multi.getY());
                }
                if(!rocks.get(y).isInOrbit())
                {
                    rocks.get(y).changeSpeedX((actingForce / rocks.get(y).getMass()) * multi.getX());
                    rocks.get(y).changeSpeedY((actingForce / rocks.get(y).getMass()) * multi.getY());
                }
            }
        }
    }

    private void updateCollisions(ArrayList<PhysicalObject> spaceObjects) {
        for (int i = 0; i < spaceObjects.size(); i++) {
            if (!Rock.class.isAssignableFrom(spaceObjects.get(i).getClass()))
                continue;

            for (int x = i + 1; x < spaceObjects.size(); x++) {
                if (!getObjectsColided(spaceObjects.get(i), spaceObjects.get(x)))
                    continue;

                // objects collided
                collideObjects((Rock) spaceObjects.get(i), (Rock) spaceObjects.get(x));
            }
        }
    }

    private void collideObjects(Rock object1, Rock object2)
    {
        Vec2d delta = Vec2d.subtractVectors(object1.getSpeedVector(), object2.getSpeedVector());
        double speedDiff = delta.getSize() * Vec2d.dot(object1.getSpeedVector(), object2.getSpeedVector());

        if(object1.isInOrbit())
            object1.removeFromOrbit();
        if(object2.isInOrbit())
            object2.removeFromOrbit();

        if(Math.abs(speedDiff) > this.mergeThreshold && object1.isAsteroid() && object2.isAsteroid())
        {
            mergeObjects(object1, object2);
        }
        else
        {
            slowObjectsCollision(object1, object2);
        }
        bounceObjects(object1, object2);
    }

    private void mergeObjects(Rock object1, Rock object2)
    {
        object1.addScore((int)object1.getMass());
        object2.destroy();

        object1.checkIncreaseRockTypeIndex();
    }
    private void slowObjectsCollision(Rock object1, Rock object2)
    {
        double relSpeed = object1.getSpeedVector().subtract(object2.getSpeedVector()).getSize();

        if(!object2.isAsteroid() && !object2.isBlackHole()) //object 2 will get dmg
        {
            object2.addDmg(-(int)(object1.getMass() * relSpeed));

            if(relSpeed > 1 && object1.isAsteroid() && !object2.isPlayer())
                object1.destroy();
        }
        if(!object1.isAsteroid() && !object1.isBlackHole())
        {
            object1.addDmg(-(int)(object2.getMass() * relSpeed));

            if(relSpeed > 1 && object2.isAsteroid() && !object2.isPlayer())
                object2.destroy();
        }
    }

    private void bounceObjects(Rock object1, Rock object2)
    {
        Vec2d delta = Vec2d.subtractVectors(object1.getCenter(), object2.getCenter());

        Vec2d mtd = delta.multiply((object1.getRadius() + object2.getRadius() - delta.getSize()) / delta.getSize());

        double im1 = 1d / object1.getMass();
        double im2 = 1d / object2.getMass();

        object1.moveXY(mtd.multiply(im1 / (im1 + im2)));
        object2.moveXY(mtd.multiply(im2 / (im1 + im2)));

        Vec2d impactSpeed = object1.getSpeedVector().subtract(object2.getSpeedVector());

        double vn = impactSpeed.dot(mtd.normalize());
        if(vn > 0.0f)
            return;

        double i = (-(1.0f + restitution) * vn) / (im1 + im2);

        Vec2d impulse = new Vec2d(mtd.normalize().getX() * i, mtd.normalize().getY() * i);

        object1.changeSpeedX(impulse.getX() * im1);
        object1.changeSpeedY(impulse.getY() * im1);
        object1.setRotationSpeed(Game.r.nextDouble() / 5 - 0.1);

        object2.changeSpeedX(-impulse.getX() * im2);
        object2.changeSpeedY(-impulse.getY() * im2);
        object2.setRotationSpeed(Game.r.nextDouble() / 5 - 0.1);
    }


    private double getObjectDistance(PhysicalObject object1, PhysicalObject object2)
    {
        return object1.getCenter().subtract(object2.getCenter()).getSize();
    }

    private boolean getObjectsColided(PhysicalObject object1, PhysicalObject object2)
    {
        double objectDistance = getObjectDistance(object1, object2);
        double radiusSum = (object1.getRadius() + object2.getRadius());

        return objectDistance <= radiusSum;
    }

}
