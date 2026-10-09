package com.jeremykenedy.canopydrift;

import java.util.Random;

public final class CanopyOptionsTest {
    public static void main(String[] args) {
        check(CanopyOptions.countFor("few") == 18, "few leaves");
        check(CanopyOptions.countFor("handful") == 38, "handful of leaves");
        check(CanopyOptions.countFor("many") == 64, "many leaves");
        check(CanopyOptions.countFor("ton") == 112, "large leaf count");
        check(CanopyOptions.countFor("schools") == 170, "maximum leaf count");
        check(CanopyOptions.countFor("invalid") == 38, "density fallback");
        check(CanopyOptions.windFor("calm") == 0.18f, "calm wind");
        check(CanopyOptions.windFor("breeze") == 0.48f, "breeze");
        check(CanopyOptions.windFor("windy") == 0.8f, "windy");
        check(CanopyOptions.windFor("gusts") == 1.25f, "gusts");
        check(CanopyOptions.windFor("invalid") == 0.48f, "wind fallback");
        check(CanopyOptions.directionFor("left") == -1f, "left direction");
        check(CanopyOptions.directionFor("right") == 1f, "right direction");
        check(CanopyOptions.directionFor("variable") == 0f, "variable direction");
        check(CanopyOptions.speciesFor("maple") == 0 && CanopyOptions.speciesFor("oak") == 1
                && CanopyOptions.speciesFor("birch") == 2, "tree species resolution");
        check(CanopyOptions.seasonFor("spring") == 0 && CanopyOptions.seasonFor("summer") == 1
                && CanopyOptions.seasonFor("autumn") == 2 && CanopyOptions.seasonFor("winter") == 3,
                "season resolution");
        check(CanopyOptions.environmentFor("forest") == 0 && CanopyOptions.environmentFor("riverside") == 1
                && CanopyOptions.environmentFor("clearing") == 2, "environment resolution");
        check(CanopyOptions.lightFor("day") == 0 && CanopyOptions.lightFor("dusk") == 1
                && CanopyOptions.lightFor("night") == 2, "lighting resolution");

        CanopyOptions selected = CanopyOptions.resolve("few", "gusts", "left", "birch", "winter",
                "riverside", "night", false, false, new Random(7));
        check(selected.leafCount == 18 && selected.wind == 1.25f && selected.direction == -1f,
                "explicit movement settings");
        check(selected.species == 2 && selected.season == 3 && selected.environment == 1
                && selected.light == 2 && !selected.rotateScenes, "explicit scene settings");

        CanopyOptions random = CanopyOptions.resolve("random", "random", "random", "random", "random",
                "random", "random", true, false, new Random(3));
        check(random.leafCount >= 18 && random.leafCount <= 170, "random leaf density bounds");
        check(random.wind >= 0.18f && random.wind <= 1.25f, "random wind bounds");
        check(random.direction >= -1f && random.direction <= 1f, "random direction bounds");
        check(random.species >= 0 && random.species <= 2 && random.season >= 0 && random.season <= 3,
                "random tree and season bounds");
        check(random.environment >= 0 && random.environment <= 2 && random.light >= 0 && random.light <= 2,
                "random environment and light bounds");

        CanopyOptions all = CanopyOptions.resolve("few", "calm", "left", "maple", "spring",
                "forest", "day", false, true, new Random(91));
        check(all.leafCount >= 18 && all.leafCount <= 170 && all.wind >= 0.18f && all.wind <= 1.25f,
                "randomize all movement settings");
        check(all.species >= 0 && all.species <= 2 && all.season >= 0 && all.season <= 3
                && all.environment >= 0 && all.environment <= 2 && all.light >= 0 && all.light <= 2,
                "randomize all scene settings");

        check(SettingsValues.isSupported("density", "schools"), "valid density");
        check(SettingsValues.isSupported("wind_strength", "gusts"), "valid wind");
        check(SettingsValues.isSupported("wind_direction", "right"), "valid direction");
        check(SettingsValues.isSupported("species", "birch"), "valid species");
        check(SettingsValues.isSupported("season", "autumn"), "valid season");
        check(SettingsValues.isSupported("environment", "riverside"), "valid environment");
        check(SettingsValues.isSupported("light", "night"), "valid lighting");
        check(SettingsValues.isSupported("rotate_scenes", "true"), "valid scene rotation");
        check(SettingsValues.isSupported("randomize_all", "false"), "valid randomize switch");
        check(SettingsValues.isSupported("season", "random"), "per-setting random accepted");
        check(!SettingsValues.isSupported("wind_strength", "hurricane"), "invalid wind rejected");
        check(!SettingsValues.isSupported("density", "infinite"), "invalid density rejected");
        check(!SettingsValues.isSupported("species", "pine"), "unsupported species rejected");
        check(!SettingsValues.isSupported("rotate_scenes", "sometimes"), "invalid switch rejected");
        check(!SettingsValues.isSupported("unknown", "value"), "unknown option rejected");
        check(!SettingsValues.isSupported(null, "autumn") && !SettingsValues.isSupported("season", null),
                "null values rejected");
    }

    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
    }
}
