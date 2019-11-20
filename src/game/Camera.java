package game;

public class Camera {
    private double cameraCenterX;
    private double cameraCenterY;
    private double cameraSizeX;
    private double cameraSizeY;

    public Camera(double cameraCenterX, double cameraCenterY) {
        this.cameraCenterX = cameraCenterX;
        this.cameraCenterY = cameraCenterY;
    }

    public void setCenter(double centerX, double centerY)
    {
        this.cameraCenterX = centerX;
        this.cameraCenterY = centerY;
    }

    public void setSizeX(double sizeX)
    {
        this.cameraSizeX = sizeX;
    }

    public void setSizeY(double sizeY)
    {
        this.cameraSizeY = sizeY;
    }

    public double getTopLeftX()
    {
        return this.cameraCenterX - (this.cameraSizeX / 2);
    }

    public double getTopLeftY()
    {
        return this.cameraCenterY - (this.cameraSizeY / 2);
    }

    public double getCameraSizeX()
    {
        return this.cameraSizeX;
    }

    public double getCameraSizeY()
    {
        return this.cameraSizeY;
    }
}
