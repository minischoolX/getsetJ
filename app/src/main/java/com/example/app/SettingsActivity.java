package com.example.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.CompoundButton;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private Switch adBlockSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View titleBar = inflater.inflate(R.layout.custom_title_bar, null);
        getSupportActionBar().setCustomView(titleBar);
        
        adBlockSwitch = findViewById(R.id.adBlockSwitch);

        loadSettings();
        
        adBlockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveAdBlockSetting(isChecked);
            }
        });
    }

    private void loadSettings() {
        SharedPreferences adBlockSharedPreferences = getSharedPreferences("AdBlockPref", Context.MODE_PRIVATE);
        boolean isAdBlockEnabled = sharedPreferences.getBoolean("adBlock", false);
        adBlockSwitch.setChecked(isAdBlockEnabled);
    }

    private void saveAdBlockSetting(boolean isEnabled) {
        SharedPreferences adBlocksharedPreferences = getSharedPreferences("AdBlockPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor adBlockeditor = sharedPreferences.edit();
        adBlockeditor.putBoolean("adBlock", isEnabled);
        adBlockeditor.apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }    
}
