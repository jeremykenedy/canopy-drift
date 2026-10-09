package com.jeremykenedy.canopydrift;

import java.util.Random;

public final class CanopyOptions {
    public final int leafCount;
    public final float wind;
    public final float direction;
    public final int species;
    public final int season;
    public final int environment;
    public final int light;
    public final boolean rotateScenes;

    private CanopyOptions(int leafCount, float wind, float direction, int species, int season,
            int environment, int light, boolean rotateScenes) {
        this.leafCount = leafCount;
        this.wind = wind;
        this.direction = direction;
        this.species = species;
        this.season = season;
        this.environment = environment;
        this.light = light;
        this.rotateScenes = rotateScenes;
    }

    public static CanopyOptions resolve(String density, String strength, String direction,
            String species, String season, String environment, String light, boolean rotateScenes,
            boolean randomizeAll, Random random) {
        String d = choose(density, randomizeAll, random, "few", "handful", "many", "ton", "schools");
        String w = choose(strength, randomizeAll, random, "calm", "breeze", "windy", "gusts");
        String dir = choose(direction, randomizeAll, random, "left", "variable", "right");
        String tree = choose(species, randomizeAll, random, "maple", "oak", "birch");
        String palette = choose(season, randomizeAll, random, "spring", "summer", "autumn", "winter");
        String place = choose(environment, randomizeAll, random, "forest", "riverside", "clearing");
        String lighting = choose(light, randomizeAll, random, "day", "dusk", "night");
        boolean rotate = randomizeAll ? random.nextBoolean() : rotateScenes;
        return new CanopyOptions(countFor(d), windFor(w), directionFor(dir), speciesFor(tree),
                seasonFor(palette), environmentFor(place), lightFor(lighting), rotate);
    }

    private static String choose(String selected, boolean randomizeAll, Random random, String... values) {
        if (randomizeAll || "random".equals(selected)) return values[random.nextInt(values.length)];
        for (String value : values) if (value.equals(selected)) return selected;
        return values[0];
    }

    static int countFor(String value) {
        if ("few".equals(value)) return 18;
        if ("many".equals(value)) return 64;
        if ("ton".equals(value)) return 112;
        if ("schools".equals(value)) return 170;
        return 38;
    }

    static float windFor(String value) {
        if ("calm".equals(value)) return 0.18f;
        if ("windy".equals(value)) return 0.8f;
        if ("gusts".equals(value)) return 1.25f;
        return 0.48f;
    }

    static float directionFor(String value) {
        if ("left".equals(value)) return -1f;
        if ("right".equals(value)) return 1f;
        return 0f;
    }

    static int speciesFor(String value) {
        if ("oak".equals(value)) return 1;
        if ("birch".equals(value)) return 2;
        return 0;
    }

    static int seasonFor(String value) {
        if ("spring".equals(value)) return 0;
        if ("summer".equals(value)) return 1;
        if ("winter".equals(value)) return 3;
        return 2;
    }

    static int environmentFor(String value) {
        if ("riverside".equals(value)) return 1;
        if ("clearing".equals(value)) return 2;
        return 0;
    }

    static int lightFor(String value) {
        if ("dusk".equals(value)) return 1;
        if ("night".equals(value)) return 2;
        return 0;
    }
}
