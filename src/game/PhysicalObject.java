package game;

import interfaces.IDamagable;
import interfaces.IDrawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import java.io.InputStream;

public abstract class PhysicalObject implements IDrawable, IDamagable {
    private Vec2d pos;
    private Vec2d speed;

    private double angle;
    private double radius;

    private double life;

    private Image sprite;

    public PhysicalObject(double centerX, double centerY, double angle, double radius, String spriteSourceAbsolute) {
        this.pos = new Vec2d();
        this.speed = new Vec2d();

        this.radius = radius;
        this.angle = angle;

        this.setCenter(centerX, centerY);

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

        this.speed = new Vec2d(speedX, speedY);
    }

    public double getPosX() {
        return this.pos.getX();
    }

    public double getPosY() {
        return this.pos.getY();
    }

    public Vec2d getPos() { return this.pos; }

    public double getCenterX() {
        return this.pos.getX() + this.radius;
    }

    public double getCenterY() {
        return this.pos.getY() + this.radius;
    }

    public Vec2d getCenter() {
        return new Vec2d(this.pos.getX() + this.radius, this.pos.getY() + this.radius);
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
        return this.speed.getX();
    }

    public double getSpeedY() {
        return this.speed.getY();
    }

    public Vec2d getSpeedVector() { return this.speed; }

    public double getSpeedSize() { return this.speed.getSize(); }

    public void setPosX(double newPosX) {
        this.pos.setX(newPosX);
    }

    public void setPosY(double newPosY) {
        this.pos.setY(newPosY);
    }

    public void setCenter(double newCenterX, double newCenterY)
    {
        this.pos.setX(newCenterX - this.getRadius());
        this.pos.setY(newCenterY - this.getRadius());
    }

    public void moveX(double deltaX) {
        this.pos.setX(this.pos.getX() + deltaX);
    }

    public void moveY(double deltaY) {
        this.pos.setY(this.pos.getY() + deltaY);
    }

    public void moveXY(Vec2d move) { this.pos = this.pos.add(move);}

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
        this.speed.setX(speedX);
    }

    public void setSpeedY(double speedY) {
        this.speed.setY(speedY);
    }

    public void changeSpeedX(double deltaSpeedX) {
        this.speed.setX(this.speed.getX() + deltaSpeedX);
    }

    public void changeSpeedY(double deltaSpeedY) {
        this.speed.setY(this.speed.getY() + deltaSpeedY);
    }

    public boolean isAlive() {
        return this.life > 0;
    }

    private void moveSpeed()
    {
        this.pos = this.pos.add(speed);
    }

    @Override
    public void update(double deltaSecond) {
        this.moveSpeed();
    }

    @Override
    public void draw(GraphicsContext graphicsContext, Camera camera) {
        graphicsContext.save();

        Rotate rotate = new Rotate(this.angle, this.getCenterX() - camera.getTopLeftX(),
                this.getCenterY() - camera.getTopLeftY());
        graphicsContext.setTransform(rotate.getMxx(), rotate.getMyx(), rotate.getMxy(), rotate.getMyy(),
                rotate.getTx(), rotate.getTy());

        graphicsContext.drawImage(this.sprite, this.pos.getX() - camera.getTopLeftX(), this.pos.getY() - camera.getTopLeftY(),
                this.radius * 2, this.radius * 2);

        graphicsContext.restore();
    }
}
