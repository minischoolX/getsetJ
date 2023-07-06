package com.example.app;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

public class ModWebViewClient extends WebViewClient {
  private Boolean isAdBlocked = getAdBlockSetting();
  private boolean isPopulatingHosts;
  private TreeMap<String, Object> blockedHosts;

  private boolean getAdBlockSetting() {
    SharedPreferences adBlockSharedPreferences = getSharedPreferences("AdBlockPref", Context.MODE_PRIVATE);
    return adBlockSharedPreferences.getBoolean("adBlock", false);
  }
 
  @Override
  public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
    if (isAdBlocked) {
      populateBlockedHosts(view.context);
      return shouldBlockRequest(url);
    }
    return false;
  }

  @Override
  public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
    String url = request.getUrl().toString();
    if (isAdBlocked) {
      populateBlockedHosts(view.context);
      return shouldBlockRequest(url);
    }
    return false;
  }

  private void populateBlockedHosts(Context context) {
    InputStream is = context.getResources().openRawResource(R.raw.adblock_serverlist);
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    String line;

    if (is != null) {
      try {
        blockedHosts = new TreeMap<String, Object>();

        while ((line = br.readLine()) != null) {
          line = line.toLowerCase().trim();

          if (!line.isEmpty() && !line.startsWith("#")) {
            blockedHosts.put(line, null);
          }
        }
      } catch (IOException e) {
        blockedHosts = null;
      }
    }
  }

  private boolean isHostBlocked(String url) {
    if ((blockedHosts == null) || isPopulatingHosts) return false;

    try {
      Uri uri = Uri.parse(url);
      String host = uri.getHost().toLowerCase().trim();
      return blockedHosts.containsKey(host);
    }
    catch(Exception e) {
      return false;
    }
  }

  private WebResourceResponse shouldBlockRequest(String url) {
    if (isHostBlocked(url)) {
      ByteArrayInputStream EMPTY = new ByteArrayInputStream("".getBytes());
      return new WebResourceResponse("text/plain", "utf-8", EMPTY);
    }
    else {
      return null;
    }
  }
}
