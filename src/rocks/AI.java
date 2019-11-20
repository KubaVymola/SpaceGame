package rocks;

public class AI extends Rock {
    public AI(double posX, double posY, double angle, double speedX, double speedY, int rockType) {
        super(posX, posY, angle, speedX, speedY, rockType);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }
}
