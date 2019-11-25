package rocks;

import game.Game;
import interfaces.IControllable;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public class Player extends Rock implements IControllable {
    private double speedup;
    private double maxSpeed;
    private double targetSpeedX;
    private double targetSpeedY;

    public Player(double posX, double posY, double angle, double speedX, double speedY, int rockType) {
        super(posX, posY, angle, speedX, speedY, rockType);

        this.targetSpeedX = 0;
        this.targetSpeedY = 0;
        this.maxSpeed = 1.5;
        this.speedup = 0.01;
    }

    @Override
    public void oneTimeAction(ArrayList<KeyCode> pressedKeys)
    {
        if (pressedKeys.contains(KeyCode.SHIFT))
        {
            this.eatSmallestChild();
            //this.setRadius(this.getRadius() == 50 ? 100 : 50);
        }
    }



    @Override
    public void control(ArrayList<KeyCode> downKeys) {
        //this.targetSpeedX = 0;
        //this.targetSpeedY = 0;

        for(KeyCode key: downKeys)
        {
            switch (key)
            {
                case LEFT:
                    if(this.getSpeedX() > -this.maxSpeed)
                        this.changeSpeedX(-this.speedup);
                    break;
                case RIGHT:
                    if(this.getSpeedX() < this.maxSpeed)
                        this.changeSpeedX(this.speedup);
                    break;
                case UP:
                    if(this.getSpeedY() > -this.maxSpeed)
                        this.changeSpeedY(-this.speedup);
                    break;
                case DOWN:
                    if(this.getSpeedY() < this.maxSpeed)
                        this.changeSpeedY(this.speedup);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    private void updateSpeed()
    {
        if(this.getSpeedX() < this.targetSpeedX)
            this.changeSpeedX(this.speedup);
        if(this.getSpeedX() > this.targetSpeedX)
            this.changeSpeedX(-this.speedup);

        if(this.getSpeedY() < this.targetSpeedY)
            this.changeSpeedY(this.speedup);
        if(this.getSpeedY() > this.targetSpeedY)
            this.changeSpeedY(-this.speedup);
    }

    public void update(ArrayList<KeyCode> downKeys, ArrayList<KeyCode> pressedKeys)
    {
        this.oneTimeAction(pressedKeys);
        this.control(downKeys);
        //this.updateSpeed();
    }
}
