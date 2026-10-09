package com.jeremykenedy.canopydrift;

public final class SettingsValues {
    private SettingsValues() {}

    public static boolean isSupported(String key, String value) {
        if (key == null || value == null) return false;
        if ("species".equals(key)) return oneOf(value, "maple", "oak", "birch", "random");
        if ("season".equals(key)) return oneOf(value, "spring", "summer", "autumn", "winter", "random");
        if ("density".equals(key)) return oneOf(value, "few", "handful", "many", "ton", "schools", "random");
        if ("wind_strength".equals(key)) return oneOf(value, "calm", "breeze", "windy", "gusts", "random");
        if ("wind_direction".equals(key)) return oneOf(value, "left", "variable", "right", "random");
        if ("environment".equals(key)) return oneOf(value, "forest", "riverside", "clearing", "random");
        if ("light".equals(key)) return oneOf(value, "day", "dusk", "night", "random");
        if ("rotate_scenes".equals(key) || "randomize_all".equals(key)) return oneOf(value, "true", "false");
        return false;
    }

    private static boolean oneOf(String value, String... allowed) {
        for (String option : allowed) if (option.equals(value)) return true;
        return false;
    }
}
