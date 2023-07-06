package com.example.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.app.Activity;

public class SettingsActivity extends Activity {
    private CheckBox darkModeCheckbox;
    // Add references to other settings UI elements as needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Find views
        darkModeCheckbox = findViewById(R.id.darkModeCheckbox);
        // Find other settings UI elements

        // Load and apply saved settings
        loadSettings();

        // Set listeners or handlers for settings UI elements
        darkModeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the dark mode setting
                saveDarkModeSetting(isChecked);

                // Apply the dark mode setting immediately (if desired)
                applyDarkModeSetting(isChecked);
            }
        });

        // Set listeners or handlers for other settings UI elements
    }

    private void loadSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("darkMode", false);
        // Load and apply other settings as needed

        // Set the UI elements based on the loaded settings
        darkModeCheckbox.setChecked(isDarkModeEnabled);
        // Set other UI elements based on the loaded settings
    }

    private void saveDarkModeSetting(boolean isEnabled) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("darkMode", isEnabled);
        editor.apply();
    }

    private void applyDarkModeSetting(boolean isEnabled) {
        // Apply the dark mode setting in your WebView or app-wide as per your implementation
    }

    // Add other helper methods and settings handling logic as needed
}
