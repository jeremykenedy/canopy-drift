package com.jeremykenedy.canopydrift;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.preference.PreferenceManager;

public final class SettingsProvider extends ContentProvider {
    public static final String AUTHORITY = "com.jeremykenedy.canopydrift.settings";
    private static final int SCHEMA = 1;
    private static final int SETTINGS = 2;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, "schema", SCHEMA);
        URI_MATCHER.addURI(AUTHORITY, "settings", SETTINGS);
    }

    @Override public boolean onCreate() { return true; }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = URI_MATCHER.match(uri);
        if (match == SCHEMA) return schemaCursor();
        if (match == SETTINGS) return settingsCursor();
        throw new IllegalArgumentException("Unknown settings URI");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (URI_MATCHER.match(uri) != SETTINGS || values == null) throw new IllegalArgumentException("Unknown settings URI");
        String key = values.getAsString("key");
        String value = values.getAsString("value");
        if (!SettingsValues.isSupported(key, value)) throw new IllegalArgumentException("Unsupported setting value");
        Context context = getContext();
        if (context == null) return 0;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        if ("randomize_all".equals(key) || "rotate_scenes".equals(key)) editor.putBoolean(key, Boolean.parseBoolean(value));
        else editor.putString(key, value);
        editor.apply();
        return 1;
    }

    private Cursor schemaCursor() {
        MatrixCursor cursor = new MatrixCursor(new String[] {"key", "title", "type", "default", "choices", "randomAllowed"});
        cursor.addRow(new Object[] {"species", "Tree species", "choice", "maple", "maple|oak|birch|random", true});
        cursor.addRow(new Object[] {"season", "Season and leaf palette", "choice", "autumn", "spring|summer|autumn|winter|random", true});
        cursor.addRow(new Object[] {"density", "Falling leaf density", "choice", "handful", "few|handful|many|ton|schools|random", true});
        cursor.addRow(new Object[] {"wind_strength", "Wind strength", "choice", "breeze", "calm|breeze|windy|gusts|random", true});
        cursor.addRow(new Object[] {"wind_direction", "Wind direction", "choice", "variable", "left|variable|right|random", true});
        cursor.addRow(new Object[] {"environment", "Setting", "choice", "forest", "forest|riverside|clearing|random", true});
        cursor.addRow(new Object[] {"light", "Lighting", "choice", "day", "day|dusk|night|random", true});
        cursor.addRow(new Object[] {"rotate_scenes", "Rotate forest settings", "boolean", "true", "true|false", false});
        cursor.addRow(new Object[] {"randomize_all", "Randomize all settings each start", "boolean", "false", "true|false", false});
        return cursor;
    }

    private Cursor settingsCursor() {
        MatrixCursor cursor = new MatrixCursor(new String[] {"key", "value"});
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getContext());
        add(cursor, p, "species", "maple");
        add(cursor, p, "season", "autumn");
        add(cursor, p, "density", "handful");
        add(cursor, p, "wind_strength", "breeze");
        add(cursor, p, "wind_direction", "variable");
        add(cursor, p, "environment", "forest");
        add(cursor, p, "light", "day");
        cursor.addRow(new Object[] {"rotate_scenes", Boolean.toString(p.getBoolean("rotate_scenes", true))});
        cursor.addRow(new Object[] {"randomize_all", Boolean.toString(p.getBoolean("randomize_all", false))});
        return cursor;
    }

    private static void add(MatrixCursor cursor, SharedPreferences preferences, String key, String fallback) {
        cursor.addRow(new Object[] {key, preferences.getString(key, fallback)});
    }

    @Override public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        if (match == SCHEMA) return "vnd.android.cursor.dir/vnd.canopy.setting-schema.v1";
        if (match == SETTINGS) return "vnd.android.cursor.dir/vnd.canopy.setting.v1";
        return null;
    }
    @Override public Uri insert(Uri uri, ContentValues values) { throw new UnsupportedOperationException("Insert is not supported"); }
    @Override public int delete(Uri uri, String selection, String[] selectionArgs) { throw new UnsupportedOperationException("Delete is not supported"); }
}
