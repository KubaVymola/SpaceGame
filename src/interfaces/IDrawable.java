package interfaces;

import game.Camera;
import javafx.scene.canvas.GraphicsContext;

public interface IDrawable {
    public void draw(GraphicsContext graphicsContext, Camera camera);
}
