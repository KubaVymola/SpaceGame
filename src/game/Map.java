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

    @Override
    public void draw(GraphicsContext graphicsContext, Camera camera) {
        graphicsContext.drawImage(this.backGroundTexture, 0 - camera.getTopLeftX(),
                0 - camera.getTopLeftY());
    }

}
