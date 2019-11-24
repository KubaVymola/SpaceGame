package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import physics.Orbit;
import rocks.Player;
import rocks.Rock;
import rocks.RockType;

public class UI {
    private static final int xAlign = 95;


    public static void predraw(GraphicsContext gc, Camera camera, Player player)
    {
        if(!player.isAlive())
            return;

        for(Orbit o : player.getOrbits())
        {
            if(o.isOccupied())
                continue;

            gc.strokeOval(player.getCenterX() - camera.getTopLeftX() - o.getRadius(),
                    player.getCenterY() - camera.getTopLeftY() - o.getRadius(),
                    o.getRadius() * 2, o.getRadius() * 2);
        }
        for(Rock r : player.getChildBodies())
        {
            gc.strokeLine(player.getCenterX() - camera.getTopLeftX(), player.getCenterY() - camera.getTopLeftY(),
                    r.getCenterX() - camera.getTopLeftX(), r.getCenterY() - camera.getTopLeftY());
        }
    }

    public static void draw(GraphicsContext gc, Camera camera, Player player)
    {
        gc.setFont(Font.font("Impact", 15));
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);

        gc.drawImage(player.getSprite(), 25,40, 50, 50);

        gc.fillText(RockType.rockTypes.get(player.getRockTypeIndex()).getName(), UI.xAlign, 40);
        gc.fillText("Mass: " + player.getScore(), UI.xAlign, 60);

        gc.fillText("To " + RockType.rockTypes.get(player.getRockTypeIndex() + 1).getName(), UI.xAlign, 90);
        gc.strokeRect(UI.xAlign, 100,50, 10);
        gc.fillRect(UI.xAlign, 100,
                ((double)player.getScore() / RockType.rockTypes.get(player.getRockTypeIndex()).getToNext()) * 50, 10);


        gc.setLineWidth(0.5);
    }
}
