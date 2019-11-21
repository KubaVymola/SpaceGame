package fighters;

import game.PhysicalObject;

public class Projectile extends PhysicalObject {
    private PhysicalObject parentBody;

    public Projectile(double posX, double posY, double angle, double radius, String spriteSourceAbsolute) {
        super(posX, posY, angle, radius, spriteSourceAbsolute);
    }

    public void update(double deltaSecond)
    {
    }
}
