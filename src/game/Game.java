package game;


import interfaces.IDrawable;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import physics.Physics;
import rocks.AI;
import rocks.Player;
import rocks.Rock;
import rocks.RockType;

import java.util.ArrayList;
import java.util.Random;


public class Game extends Application {
    public static Random r;

    private final int initialWidth = 1360;
    private final int initialHeight = 768;

    private int currentWidth;
    private int currentHeight;

    private ArrayList<PhysicalObject> physicalObjects;
    private ArrayList<IDrawable> drawableObjects;
    private ArrayList<Rock> gravityObjects;

    private Player player;
    private Camera camera;
    private Map map;
    private Physics physics;

    private ArrayList<KeyCode> input = new ArrayList<>();

    public Game() {
        r = new Random(2);
        RockType.initRockTypes();

        this.map = new Map();
        this.camera = new Camera(0, 0);
        this.physics = new Physics();

        this.physicalObjects = new ArrayList<>();
        this.drawableObjects = new ArrayList<>();
        this.gravityObjects = new ArrayList<>();
    }

    @Override
    public void init() throws Exception {
        super.init();

        player = new Player(0,0,0,0, 0.1, 0);
        physicalObjects.add(player);
        drawableObjects.add(player);
        gravityObjects.add(player);

        for(int i = 0; i < 15; i++)
        {
            AI newObject = new AI(r.nextInt(800) - 400, r.nextInt(600) - 300, r.nextDouble(),
                    r.nextDouble() - 0.5d, r.nextDouble() - 0.5d, 0);

            physicalObjects.add(newObject);
            drawableObjects.add(newObject);
            gravityObjects.add(newObject);
        }

    }


    private void optimize()
    {
        for(int i = this.gravityObjects.size() - 1; i >= 0; i--)
        {
            Rock r = this.gravityObjects.get(i);
            if(r.isDestroyed()) {
                this.gravityObjects.remove(r);
                this.drawableObjects.remove(r);
                this.physicalObjects.remove(r);
                continue;
            }
        }
    }

    private void gameControl(KeyCode e, Stage stage)
    {
        if(e == KeyCode.ESCAPE)
            stage.close();
    }

    private void update(long deltaNano)
    {
        double deltaSec = deltaNano / 1e9;

        player.update(input);

        for(IDrawable object : drawableObjects)
        {
            object.update(deltaSec);
        }

        this.physics.updatePhysics(physicalObjects, gravityObjects);

        this.optimize();
    }

    private void draw(GraphicsContext gc)
    {
        this.camera.setCenter(player.getCenterX(), player.getCenterY());

        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, this.currentWidth,this.currentHeight);

        this.map.draw(gc, this.camera);

        for(IDrawable object : drawableObjects)
        {
            object.draw(gc, this.camera);
        }

        UI.draw(gc, this.camera, this.player);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Solar 2 Clone");
        Group root = new Group();
        Canvas canvas = new Canvas(this.initialWidth, this.initialHeight);
        this.currentWidth = this.initialWidth;
        this.currentHeight = this.initialHeight;

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

        scene.setOnKeyPressed(keyEvent -> {
            KeyCode keyPressed = keyEvent.getCode();

            if(!input.contains(keyPressed))
            {
                player.oneTimeAction(keyPressed);
                gameControl(keyPressed, stage);
                // one time actions
                input.add(keyPressed);
            }
        });

        scene.setOnKeyReleased(keyEvent -> {
            KeyCode keyReleased = keyEvent.getCode();

            if(input.contains(keyReleased))
            {
                input.remove(keyReleased);
            }
        });

        stage.widthProperty().addListener((observableValue, number, t1) -> {
            canvas.setWidth(t1.intValue());
            this.currentWidth = t1.intValue();
            this.camera.setSizeX(t1.intValue());
        });

        stage.heightProperty().addListener((observableValue, number, t1) -> {
            canvas.setHeight(t1.intValue());
            this.currentHeight = t1.intValue();
            this.camera.setSizeY(t1.intValue());
        });


        timer.start();

        stage.show();
    }

    public static void main(String[] args)  {
        launch(args);
    }
}
