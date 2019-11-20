package physics;

public class Vec2d {
    private double x;
    private double y;

    public Vec2d()
    {
        this.setX(0);
        this.setY(0);
    }

    public Vec2d(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void set(double x, double y)
    {
        this.setX(x);
        this.setY(y);
    }

    public Vec2d add(Vec2d vec2)
    {
        return new Vec2d(this.getX() + vec2.getX(), this.getY() + vec2.getY());
    }

    public Vec2d subtract(Vec2d vec2)
    {
        return new Vec2d(this.getX() - vec2.getX(), this.getY() - vec2.getY());
    }

    public Vec2d multiply(double constant)
    {
        return new Vec2d(this.getX() * constant, this.getY() * constant);
    }

    public double getSize()
    {
        return Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2));
    }

    public double dot(Vec2d vec2)
    {
        return Vec2d.dot(this, vec2);
    }

    public Vec2d normalize()
    {
        return new Vec2d(this.getX() / this.getSize(), this.getY() / this.getSize());
    }

    public static double dot(Vec2d vec1, Vec2d vec2)
    {
        return (vec1.getX() * vec2.getX() + vec1.getY() * vec2.getY());
    }

    public static Vec2d addVectors(Vec2d vec1, Vec2d vec2)
    {
        return new Vec2d(vec1.getX() + vec2.getX(), vec1.getY() + vec2.getY());
    }
    public static Vec2d subtractVectors(Vec2d vec1, Vec2d vec2)
    {
        return new Vec2d(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY());
    }
}
