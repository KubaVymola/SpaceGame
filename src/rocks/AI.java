package rocks;

public class AI extends Rock {
    public AI(double posX, double posY, double angle, double radius, String spriteSource) {
        super(posX, posY, angle, radius, 100, spriteSource);
    }

    public AI(double posX, double posY, double angle, double radius, double speedX, double speedY, String spriteSourceAbsolute) {
        super(posX, posY, angle, radius, speedX, speedY, 100, spriteSourceAbsolute);
    }
}
