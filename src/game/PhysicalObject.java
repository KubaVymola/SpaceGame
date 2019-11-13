package game;

import interfaces.IDamagable;
import interfaces.IDrawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import java.io.InputStream;

public abstract class PhysicalObject implements IDrawable, IDamagable {
    private double posX;
    private double posY;
    private double angle;
    private double radius;

    private double speedX;
    private double speedY;


    private double life;

    private Image sprite;

    public PhysicalObject(double posX, double posY, double angle, double radius, String spriteSourceAbsolute) {
        this.posX = posX;
        this.posY = posY;
        this.angle = angle;
        this.radius = radius;
        this.speedX = 0;
        this.speedY = 0;

        try {
            InputStream resource = getClass().getResource(spriteSourceAbsolute).openStream();
            sprite = new Image(resource);
        }
        catch (Exception e) {
            sprite = new Image("not-found.png");
        }
    }

    public PhysicalObject(double posX, double posY, double angle, double radius, double speedX, double speedY, String spriteSourceAbsolute) {
        this(posX, posY, angle, radius, spriteSourceAbsolute);

        this.speedX = speedX;
        this.speedY = speedY;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getCenterX() {
        return this.posX + this.radius;
    }

    public double getCenterY() {
        return this.posY + this.radius;
    }

    public double getAngle() {
        return angle;
    }

    public double getRadius() {
        return radius;
    }

    public Image getSprite() {
        return sprite;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public double getSpeed() { return Math.sqrt(Math.pow(this.speedX, 2) + Math.pow(this.speedY, 2)); }

    public void setPosX(double newPosX) {
        this.posX = newPosX;
    }

    public void setPosY(double newPosY) {
        this.posY = newPosY;
    }

    public void moveX(double deltaX) {
        this.posX += deltaX;
    }

    public void moveY(double deltaY) {
        this.posY += deltaY;
    }

    public void setAngle(double newAngle) {
        this.angle = newAngle;
    }

    public void rotate(double deltaAngle) {
        this.angle += deltaAngle;
    }

    public void setRadius(double newRadius) {
        this.radius = newRadius;
    }

    public void grow(double deltaRadius) {
        this.radius += deltaRadius;
    }

    public void receiveDMG(double amount) {
        this.life -= amount;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public void changeSpeedX(double deltaSpeedX) {
        this.speedX += deltaSpeedX;
    }

    public void changeSpeedY(double deltaSpeedY) {
        this.speedY += deltaSpeedY;
    }

    public boolean isAlive() {
        return this.life > 0;
    }

    private void move()
    {
        this.posX += speedX;
        this.posY += speedY;
    }

    @Override
    public void update(double deltaSecond) {
        this.move();
    }

    @Override
    public void draw(GraphicsContext graphicsContext, Camera camera) {
        graphicsContext.save();

        Rotate rotate = new Rotate(this.angle, this.getCenterX() - camera.getTopLeftX(),
                this.getCenterY() - camera.getTopLeftY());
        graphicsContext.setTransform(rotate.getMxx(), rotate.getMyx(), rotate.getMxy(), rotate.getMyy(),
                rotate.getTx(), rotate.getTy());

        graphicsContext.drawImage(this.sprite, this.posX - camera.getTopLeftX(), this.posY - camera.getTopLeftY(),
                this.radius * 2, this.radius * 2);

        graphicsContext.restore();
    }
}
