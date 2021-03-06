package rocks;

import game.Game;

import java.util.ArrayList;

public class RockType {
    public static int rockTypeCount = 10;
    public static ArrayList<RockType> rockTypes;

    public static int planetsFrom = 1;
    public static int planetsTo = 3;
    public static int starsFrom = 4;
    public static int starsTo = 7;
    public static int blackHoleIndex = 8;

    private double toNext;
    private double mass;
    private double radius;
    private int orbits;
    private String name;
    private String[] textureNames;


    public RockType(String name, double mass, double radius, int orbits, double toNext, String[] textureNames) {
        this.toNext = toNext;
        this.mass = mass;
        this.radius = radius;
        this.name = name;
        this.textureNames = textureNames;
        this.orbits = orbits;
    }

    public double getToNext() {
        return toNext;
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public int getOrbits() {
        return orbits;
    }

    public String getName() {
        return name;
    }

    public String getTextureName() {
        return this.textureNames[Game.r.nextInt(this.textureNames.length)];
    }

    public static void initRockTypes()
    {
        rockTypes = new ArrayList<>();

        rockTypes.add(new RockType("Asteroid", 25, 10, 0, 100,
                new String[] { "/images/asteroid.png", "/images/asteroid-2.png" } ));

        rockTypes.add(new RockType("Small Planet", 50, 15, 3, 200,
                new String[] { "/images/medium-planet.png" } ));
        rockTypes.add(new RockType("Medium Planet", 75, 17, 4, 300,
                new String[] { "/images/medium-planet.png" } ));
        rockTypes.add(new RockType("Planet With Life", 100, 20, 6, 450,
                new String[] { "/images/life-planet.png" } ));

        rockTypes.add(new RockType("Small Star", 500, 30, 3, 1000,
                new String[] { "/images/small-star.png" } ));
        rockTypes.add(new RockType("Medium Star", 1000, 40, 4, 2000,
                new String[] { "/images/medium-star.png" } ));
        rockTypes.add(new RockType("Large Star", 1500, 60, 6, 5000,
                new String[] { "/images/medium-star.png" } ));
        rockTypes.add(new RockType("Neutron Star", 2500, 15, 8, 10000,
                new String[] { "/images/medium-star.png" } ));

        rockTypes.add(new RockType("Black Hole", 10000, 5, 0, 50000,
                new String[] { "/images/medium-star.png" } ));

        rockTypes.add(new RockType("Big Bang", 0, 0, 0, 0,
                new String[] { "/images/medium-star.png" } ));
    }
}
