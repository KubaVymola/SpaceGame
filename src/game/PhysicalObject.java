package game;

import interfaces.IDamagable;
import interfaces.IDrawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import physics.Vec2d;

import java.io.InputStream;

public abstract class PhysicalObject implements IDrawable, IDamagable {
    private Vec2d center;
    private Vec2d speed;

    private double angle;
    private double radius;
    private double life;

    private boolean destroyed;

    private Image sprite;

    public PhysicalObject(double centerX, double centerY, double angle, double radius, String spriteSourceAbsolute) {
        this.center = new Vec2d();
        this.speed = new Vec2d();

        this.radius = radius;
        this.angle = angle;
        this.destroyed = false;

        this.setCenter(centerX, centerY);

        this.loadImage(spriteSourceAbsolute);
    }

    public PhysicalObject(double posX, double posY, double angle, double radius, double speedX, double speedY, String spriteSourceAbsolute) {
        this(posX, posY, angle, radius, spriteSourceAbsolute);

        this.speed = new Vec2d(speedX, speedY);
    }

    public void loadImage(String spriteSourceAbsolute)
    {
        try {
            InputStream resource = getClass().getResource(spriteSourceAbsolute).openStream();
            sprite = new Image(resource);
        }
        catch (Exception e) {
            sprite = new Image("not-found.png");
        }
    }

    public double getPosX() {
        return this.getCenterX() - this.getRadius();
    }

    public double getPosY() {
        return this.getCenterY() - this.getRadius();
    }

    public Vec2d getPos() { return new Vec2d(getPosX(), getPosY()); }

    public double getCenterX() {
        return this.center.getX();
    }

    public double getCenterY() {
        return this.center.getY();
    }

    public Vec2d getCenter() {
        return this.center;
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
        this.center.setX(newPosX + this.getRadius());
    }

    public void setPosY(double newPosY) {
        this.center.setY(newPosY + this.getRadius());
    }

    public void setCenter(double newCenterX, double newCenterY)
    {
        this.center.setX(newCenterX);
        this.center.setY(newCenterY);
    }

    public void moveX(double deltaX) {
        this.center.setX(this.center.getX() + deltaX);
    }

    public void moveY(double deltaY) {
        this.center.setY(this.center.getY() + deltaY);
    }

    public void moveXY(Vec2d move) { this.center = this.center.add(move);}

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

    public void destroy()
    {
        this.destroyed = true;
    }
    public boolean isDestroyed()
    {
        return this.destroyed;
    }

    public void addScore(int score)
    {
        this.life += score;

        if(this.life <= 0)
        {
            this.life = 0;
            this.destroy();
        }
    }
    public int getScore()
    {
        return (int)this.life;
    }

    public void moveSpeed()
    {
        this.center = this.center.add(speed);
    }

    //@Override
    /*public void update(double deltaSecond) {
        this.moveSpeed();
    }*/

    @Override
    public void draw(GraphicsContext graphicsContext, Camera camera) {
        graphicsContext.save();

        Rotate rotate = new Rotate(this.angle, this.getCenterX() - camera.getTopLeftX(),
                this.getCenterY() - camera.getTopLeftY());
        graphicsContext.setTransform(rotate.getMxx(), rotate.getMyx(), rotate.getMxy(), rotate.getMyy(),
                rotate.getTx(), rotate.getTy());

        graphicsContext.drawImage(this.sprite, this.getCenterX() - this.getRadius() - camera.getTopLeftX(),
                this.getCenterY() - this.getRadius() - camera.getTopLeftY(),
                this.radius * 2, this.radius * 2);

        graphicsContext.restore();
    }
}
