package com.jeremykenedy.canopydrift;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public final class SettingsActivity extends Activity {
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ScrollView scroll = new ScrollView(this);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(40, 20, 40, 20);
        scroll.addView(content);
        TextView title = new TextView(this);
        title.setText("Canopy Drift settings");
        title.setTextSize(24);
        title.setPadding(0, 0, 0, 12);
        content.addView(title);
        Button preview = new Button(this);
        preview.setText("Preview animation");
        preview.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, PreviewActivity.class));
            }
        });
        content.addView(preview);
        addChoice(content, "Tree species", "species", new String[] {"maple", "oak", "birch", "random"},
                new String[] {"Maple", "Oak", "Birch", "Random"}, "maple");
        addChoice(content, "Season and leaf palette", "season",
                new String[] {"spring", "summer", "autumn", "winter", "random"},
                new String[] {"Spring", "Summer", "Autumn", "Winter", "Random"}, "autumn");
        addChoice(content, "Falling leaf density", "density",
                new String[] {"few", "handful", "many", "ton", "schools", "random"},
                new String[] {"A few", "A handful", "Many", "A ton", "Schools", "Random"}, "handful");
        addChoice(content, "Wind strength", "wind_strength",
                new String[] {"calm", "breeze", "windy", "gusts", "random"},
                new String[] {"Calm", "Breeze", "Windy", "Strong gusts", "Random"}, "breeze");
        addChoice(content, "Wind direction", "wind_direction",
                new String[] {"left", "variable", "right", "random"},
                new String[] {"Left", "Variable", "Right", "Random"}, "variable");
        addChoice(content, "Setting", "environment",
                new String[] {"forest", "riverside", "clearing", "random"},
                new String[] {"Forest", "Riverside", "Forest clearing", "Random"}, "forest");
        addChoice(content, "Lighting", "light", new String[] {"day", "dusk", "night", "random"},
                new String[] {"Day", "Dusk", "Night", "Random"}, "day");
        addToggle(content, "Rotate forest settings every 90 seconds", "rotate_scenes", true);
        addToggle(content, "Randomize all settings each time the screensaver starts", "randomize_all", false);
        setContentView(scroll);
    }

    private void addChoice(LinearLayout content, String title, String key, String[] values,
            String[] labels, String fallback) {
        TextView label = new TextView(this);
        label.setText(title);
        label.setTextSize(16);
        label.setPadding(0, 8, 0, 2);
        content.addView(label);
        Spinner spinner = new Spinner(this);
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
        spinner.setGravity(Gravity.CENTER_VERTICAL);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        String selected = preferences.getString(key, fallback);
        for (int i = 0; i < values.length; i++) if (values[i].equals(selected)) spinner.setSelection(i);
        spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                preferences.edit().putString(key, values[position]).apply();
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
        content.addView(spinner);
    }

    private void addToggle(LinearLayout content, String label, String key, boolean fallback) {
        CheckBox checkbox = new CheckBox(this);
        checkbox.setText(label);
        checkbox.setTextSize(16);
        checkbox.setChecked(preferences.getBoolean(key, fallback));
        checkbox.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(android.widget.CompoundButton button, boolean checked) {
                preferences.edit().putBoolean(key, checked).apply();
            }
        });
        content.addView(checkbox);
    }
}
