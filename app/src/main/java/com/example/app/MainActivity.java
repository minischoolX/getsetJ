package com.example.app;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.webkit.WebViewClient;
import android.content.SharedPreferences;

import android.content.Context;
import android.content.Intent;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.text.InputType;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private EditText searchEditText;
    private ImageButton settingsButton;
    private Boolean isAdBlocked;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mWebView = findViewById(R.id.activity_main_webview);
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        settingsButton = findViewById(R.id.settingsButton);

        isAdBlocked = getAdBlockSetting();
        
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new ModWebViewClient());

        // Set OnEditorActionListener for searchEditText
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        // Set click listener for settingsButton
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });
        
        mWebView.loadUrl("https://google.com");
    }

    private boolean getAdBlockSetting() {
        SharedPreferences adBlockSharedPreferences = getSharedPreferences("AdBlockPref", Context.MODE_PRIVATE);
        return adBlockSharedPreferences.getBoolean("adBlock", false);
    }


    private void performSearch() {
        String searchText = searchEditText.getText().toString().trim();

        if (isValidUrl(searchText)) {
            // Load URL in the WebView
            mWebView.loadUrl(searchText);
        } else {
            // Perform a Google search
            String googleSearchUrl = "https://www.google.com/search?q=" + searchText;
            mWebView.loadUrl(googleSearchUrl);
        }
    }

    // Helper method to check if the entered text is a valid URL
    private boolean isValidUrl(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }

    private void openSettings() {
        // Navigate to the settings screen
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    
    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
