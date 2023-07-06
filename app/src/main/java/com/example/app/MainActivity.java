package com.example.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.content.Intent;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {

    private WebView mWebView;
    private EditText searchEditText;
    private ImageButton settingsButton;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mWebView = findViewById(R.id.activity_main_webview);
        searchEditText = findViewById(R.id.searchEditText);
        settingsButton = findViewById(R.id.settingsButton);
        
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());

        // Set OnEditorActionListener for searchEditText
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
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

    private void performSearch() {
        String searchText = searchEditText.getText().toString().trim();

        if (isValidUrl(searchText)) {
            // Load URL in the WebView
            webView.loadUrl(searchText);
        } else {
            // Perform a Google search
            String googleSearchUrl = "https://www.google.com/search?q=" + searchText;
            webView.loadUrl(googleSearchUrl);
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
