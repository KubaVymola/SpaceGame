package game;

import rocks.Rock;

import java.util.ArrayList;

public class Physics {
    public void updatePhysics(ArrayList<PhysicalObject> spaceObjects)
    {
        updateCollisions(spaceObjects);
    }

    private void updateCollisions(ArrayList<PhysicalObject> spaceObjects)
    {
        for (int i = 0; i < spaceObjects.size(); i++)
        {
            if(!Rock.class.isAssignableFrom(spaceObjects.get(i).getClass()))
                continue;

            for(int x = i + 1; x < spaceObjects.size(); x++)
            {
                double objectDistance = Math.sqrt(
                        Math.pow(spaceObjects.get(i).getCenterX() - spaceObjects.get(x).getCenterX(), 2) +
                        Math.pow(spaceObjects.get(i).getCenterY() - spaceObjects.get(x).getCenterY(), 2));

                double radiusSum = (spaceObjects.get(i).getRadius() + spaceObjects.get(x).getRadius()) * 0.8;

                if(objectDistance > radiusSum)
                    continue;

                // objects collided
                collideObjects((Rock)spaceObjects.get(i), (Rock)spaceObjects.get(x));
            }
        }
    }

    private void collideObjects(Rock object1, Rock object2)
    {
        double collisionLoss = 0.5; //TODO change this to global parameter

        object1.moveX(-object1.getSpeedX());
        object1.moveY(-object1.getSpeedY());

        object2.moveX(-object2.getSpeedX());
        object2.moveY(-object2.getSpeedY());

        double relSpeedX = Math.abs(object1.getSpeedX() - object2.getSpeedX());
        double relSpeedY = Math.abs(object1.getSpeedY() - object2.getSpeedY());

        double totalMass = object1.getMass() + object2.getMass();

        object1.setSpeedX(relSpeedX * (1 - object1.getMass() / totalMass) *
                (-object1.getSpeedX() / Math.abs(object1.getSpeedX())));
        object1.setSpeedY(relSpeedY * (1 - object1.getMass() / totalMass) *
                (-object1.getSpeedY() / Math.abs(object1.getSpeedY())));

        object2.setSpeedX(relSpeedX * (1 - object2.getMass() / totalMass) *
                (-object2.getSpeedX() / Math.abs(object2.getSpeedX())));
        object2.setSpeedY(relSpeedY * (1 - object2.getMass() / totalMass) *
                (-object2.getSpeedY() / Math.abs(object2.getSpeedY())));
    }
}
