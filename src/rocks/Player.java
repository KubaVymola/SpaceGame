package rocks;

import game.Game;
import interfaces.IControllable;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public class Player extends Rock implements IControllable {
    private double maxSpeed = 1.5;
    private double speedup = 0.02;

    private Game game;

    public Player(double posX, double posY, double angle, double radius, String spriteSource) {
        super(posX, posY, angle, radius, 250, spriteSource);
    }

    @Override
    public void oneTimeAction(KeyCode pressedKey) {
        if (pressedKey == KeyCode.SHIFT)
            this.setRadius(this.getRadius() == 50 ? 100 : 50);
    }

    @Override
    public void control(ArrayList<KeyCode> downKeys) {
        //this.setSpeedX(0);
        //this.setSpeedY(0);

        for(KeyCode key: downKeys)
        {
            switch (key)
            {
                case LEFT:
                    this.changeSpeedX(-0.01);
                    break;
                case RIGHT:
                    this.changeSpeedX(0.01);
                    break;
                case UP:
                    this.changeSpeedY(-0.01);
                    break;
                case DOWN:
                    this.changeSpeedY(0.01);
                    break;
                default:
                    break;
            }
        }
    }
}
