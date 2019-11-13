package game;


import interfaces.IDrawable;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import rocks.AI;
import rocks.Player;

import java.util.ArrayList;
import java.util.Random;


public class Game extends Application {
    private final int initialWidth = 800;
    private final int initialHeight = 600;

    private Random r;

    private ArrayList<PhysicalObject> physicalObjects;
    private ArrayList<IDrawable> drawableObjects;

    private Player player;
    private Camera camera;
    private Map map;
    private Physics physics;

    private ArrayList<KeyCode> input = new ArrayList<>();

    public Game() {
        r = new Random(1);

        this.map = new Map();
        this.camera = new Camera(initialWidth, initialHeight);
        this.physics = new Physics();

        this.physicalObjects = new ArrayList<>();
        this.drawableObjects = new ArrayList<>();
    }

    @Override
    public void init() throws Exception {
        super.init();

        for(int i = 0; i < 1; i++)
        {
            AI newObject = new AI(r.nextInt(800), r.nextInt(600), r.nextDouble(),
                    50, r.nextDouble() * 0.25, r.nextDouble() * 0.25, "/images/mars-transparent.png");

            physicalObjects.add(newObject);
            drawableObjects.add(newObject);
        }

        player = new Player(0,0,0,25, "/images/asteroid.png");
        physicalObjects.add(player);
        drawableObjects.add(player);
    }

    private void gameControl(KeyCode e, Stage stage)
    {
        if(e == KeyCode.ESCAPE)
            stage.close();
    }

    private void update(long deltaNano)
    {
        double deltaSec = deltaNano / 1e9;

        player.control(input);

        for(IDrawable object : drawableObjects)
        {
            object.update(deltaSec);
        }

        this.physics.updatePhysics(physicalObjects);
    }

    private void draw(GraphicsContext gc)
    {
        this.camera.setCenter(player.getCenterX(), player.getCenterY());

        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, this.camera.getCameraSizeX(),this.camera.getCameraSizeY());

        this.map.draw(gc, this.camera);

        for(IDrawable object : drawableObjects)
        {
            object.draw(gc, this.camera);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Solar 2 Clone");
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        Scene scene = new Scene(root);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);
        stage.setScene(scene);

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate;

            @Override
            public void start() {
                lastUpdate = System.nanoTime();
                super.start();
            }

            @Override
            public void handle(long l) {
                Game.this.update(System.nanoTime() - lastUpdate);
                this.lastUpdate = System.nanoTime();
                Game.this.draw(graphicsContext);
            }
        };

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode keyPressed = keyEvent.getCode();

                if(!input.contains(keyPressed))
                {
                    player.oneTimeAction(keyPressed);
                    gameControl(keyPressed, stage);
                    // one time actions
                    input.add(keyPressed);
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode keyReleased = keyEvent.getCode();

                if(input.contains(keyReleased))
                {
                    input.remove(keyReleased);
                }
            }
        });

        stage.widthProperty().addListener((observableValue, number, t1) -> {
            canvas.setWidth(t1.intValue());
            this.camera.setSizeX(t1.intValue());
        });

        stage.heightProperty().addListener((observableValue, number, t1) -> {
            canvas.setHeight(t1.intValue());
            this.camera.setSizeY(t1.intValue());
        });


        timer.start();

        stage.show();
    }

    public static void main(String[] args)  {
        launch(args);
    }
}
