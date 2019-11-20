package game;

import interfaces.IDrawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Map implements IDrawable {
    private Image backGroundTexture;

    public Map() {
        this.backGroundTexture = new Image("seamless_space.png");
    }

    @Override
    public void update(double deltaSecond) {

    }

    private int setDrawBegining(double cameraPoint, double imageSize)
    {
        /*double toReturn = 0;
        while(toReturn < cameraPoint)
            toReturn += imageSize;
        while(toReturn > cameraPoint)
            toReturn -= imageSize;

        return toReturn;*/

        int multiplier = (int)(cameraPoint / imageSize);
        if(multiplier <= 0)
            multiplier-=1;

        return (int)(multiplier * imageSize);
    }

    @Override
    public void draw(GraphicsContext graphicsContext, Camera camera) {
        double initialX = setDrawBegining(camera.getTopLeftX(), this.backGroundTexture.getWidth());
        double initialY = setDrawBegining(camera.getTopLeftY(), this.backGroundTexture.getHeight());

        for(double x = initialX; x < camera.getTopLeftX() + camera.getCameraSizeX();
            x += this.backGroundTexture.getWidth())
        {
            for(double y = initialY; y < camera.getTopLeftY() + camera.getCameraSizeY();
                y += this.backGroundTexture.getHeight())
            {
                graphicsContext.drawImage(this.backGroundTexture, x - camera.getTopLeftX(),
                        y - camera.getTopLeftY(), this.backGroundTexture.getWidth(),
                        this.backGroundTexture.getHeight());
            }
        }
    }

}
