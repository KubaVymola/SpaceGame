package fighters;

import game.PhysicalObject;
import interfaces.IDamagable;

public class Ship extends PhysicalObject {
    private double life;

    public Ship(double posX, double posY, double angle, double radius, String spriteSourceAbsolute) {
        super(posX, posY, angle, radius, spriteSourceAbsolute);
    }

    @Override
    public void receiveDMG(double amount) {
        this.life -= amount;
    }
}
