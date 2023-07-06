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

import android.graphics.Bitmap;

public class ModWebViewClient extends WebViewClient {
//  private Boolean isAdBlocked;
//  private boolean isPopulatingHosts;
//  private TreeMap<String, Object> blockedHosts;


      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (url.matches("^(https?://(www|m)\\.youtube\\.com/.*)|(https?://.*\\.youtube-nocookie\\.com/embed/.*)|(https?://youtube\\.googleapis\\.com/embed/.*)|(https?://raingart\\.github\\.io/options\\.html.*)$") &&
          !url.matches("^(https?://.*\\.youtube\\.com/.*\\.xml.*)|(https?://.*\\.youtube\\.com/error.*)|(https?://music\\.youtube\\.com/.*)|(https?://accounts\\.youtube\\.com/.*)|(https?://studio\\.youtube\\.com/.*)|(https?://.*\\.youtube\\.com/redirect\\?.*)$")) {
    	    String script = "let defaultObj = {\r\n  \"square-avatars\": \"on\",\r\n  \"user-api-key\": \"\",\r\n  \"lang_code\": \"en\"\r\n};\r\nconst user_settings = defaultObj;\r\n\r\nwindow.nova_plugins = [];\r\nwindow.nova_plugins.push({\r\n   id: \'square-avatars\',\r\n   title: \'Square avatars\',\r\n   run_on_pages: \'*, -live_chat\',\r\n   section: \'comments\',\r\n   desc: \'Make user images squared\',\r\n   _runtime: user_settings => {\r\n      NOVA.css.push(\r\n         [\r\n            \'yt-img-shadow\',\r\n            \'.ytp-title-channel-logo\',\r\n            \'#player .ytp-title-channel\',\r\n            \'ytm-profile-icon\',\r\n            \'a.ytd-thumbnail\',\r\n         ]\r\n            .join(\',\\n\') + ` {\r\n               border-radius: 0 !important;\r\n            }`);\r\n      NOVA.waitUntil(() => {\r\n         if (window.yt && (obj = yt?.config_?.EXPERIMENT_FLAGS) && Object.keys(obj).length) {\r\n            yt.config_.EXPERIMENT_FLAGS.web_rounded_thumbnails = false;\r\n            return true;\r\n         }\r\n      });\r\n   },\r\n});\r\nconst NOVA = {\r\n   waitSelector(selector = required(), limit_data) {\r\n      if (typeof selector !== \'string\') return console.error(\'wait > selector:\', typeof selector);\r\n      if (limit_data?.container && !(limit_data.container instanceof HTMLElement)) return console.error(\'wait > container not HTMLElement:\', limit_data.container);\r\n      if (selector.includes(\':has(\') && !CSS.supports(\'selector(:has(*))\')) {\r\n         return new Promise((resolve, reject) => {\r\n            console.warn(\'CSS \":has()\" unsupported\');\r\n            reject(\'CSS \":has()\" unsupported\');\r\n         });\r\n      }\r\n      return new Promise(resolve => {\r\n         if (element = (limit_data?.container || document.body || document).querySelector(selector)) {\r\n            return resolve(element);\r\n         }\r\n         const observer1 = new MutationObserver((mutationRecordsArray, observer) => {\r\n            for (const record of mutationRecordsArray) {\r\n               for (const node of record.addedNodes) {\r\n                  if (![1, 3, 8].includes(node.nodeType) || !(node instanceof HTMLElement)) continue;\r\n                  if (node.matches && node.matches(selector)) {\r\n                     observer.disconnect();\r\n                     return resolve(node);\r\n                  }\r\n                  else if (\r\n                     (parentEl = node.parentElement || node)\r\n                     && (parentEl instanceof HTMLElement)\r\n                     && (element = parentEl.querySelector(selector))\r\n                  ) {\r\n                     observer.disconnect();\r\n                     return resolve(element);\r\n                  }\r\n               }\r\n            }\r\n            if (document?.readyState != \'loading\'\r\n               && (element = (limit_data?.container || document?.body || document).querySelector(selector))\r\n            ) {\r\n               observer.disconnect();\r\n               return resolve(element);\r\n            }\r\n         })\r\n         observer1\r\n            .observe(limit_data?.container || document.body || document.documentElement || document, {\r\n               childList: true,\r\n               subtree: true,\r\n               attributes: true,\r\n            });\r\n         if (limit_data?.stop_on_page_change) {\r\n            isURLChange();\r\n            window.addEventListener(\'transitionend\', ({ target }) => {\r\n               if (isURLChange()) {\r\n                  observer1.disconnect();\r\n               }\r\n            });\r\n            function isURLChange() {\r\n               return (this.prevURL === location.href) ? false : this.prevURL = location.href;\r\n            }\r\n         }\r\n      });\r\n   },\r\n   waitUntil(condition = required(), timeout = 100) {\r\n      if (typeof condition !== \'function\') return console.error(\'waitUntil > condition is not fn:\', typeof condition);\r\n      return new Promise((resolve) => {\r\n         if (result = condition()) {\r\n            resolve(result);\r\n         }\r\n         else {\r\n            const interval = setInterval(() => {\r\n               if (result = condition()) {\r\n                  clearInterval(interval);\r\n                  resolve(result);\r\n               }\r\n            }, timeout);\r\n         }\r\n      });\r\n   },\r\n\r\n   delay(ms = 100) {\r\n      return new Promise(resolve => setTimeout(resolve, ms));\r\n   },\r\n\r\n   css: {\r\n      push(css = required(), selector, important) {\r\n         if (typeof css === \'object\') {\r\n            if (!selector) return console.error(\'injectStyle > empty json-selector:\', ...arguments);\r\n            injectCss(selector + json2css(css));\r\n            function json2css(obj) {\r\n               let css = \'\';\r\n               Object.entries(obj)\r\n                  .forEach(([key, value]) => {\r\n                     css += key + \':\' + value + (important ? \' !important\' : \'\') + \';\';\r\n                  });\r\n               return `{ ${css} }`;\r\n            }\r\n         }\r\n         else if (css && typeof css === \'string\') {\r\n            if (document.head) {\r\n               injectCss(css);\r\n            }\r\n            else {\r\n               window.addEventListener(\'load\', () => injectCss(css), { capture: true, once: true });\r\n            }\r\n         }\r\n         else {\r\n            console.error(\'addStyle > css:\', typeof css);\r\n         }\r\n         function injectCss(source = required()) {\r\n            let sheet;\r\n            if (source.endsWith(\'.css\')) {\r\n               sheet = document.createElement(\'link\');\r\n               sheet.rel = \'sheet\';\r\n               sheet.href = source;\r\n            }\r\n            else {\r\n               const sheetId = \'NOVA-style\';\r\n               sheet = document.getElementById(sheetId) || (function () {\r\n                  const style = document.createElement(\'style\');\r\n                  style.type = \'text/css\';\r\n                  style.id = sheetId;\r\n                  return (document.head || document.documentElement).appendChild(style);\r\n               })();\r\n            }\r\n            sheet.textContent += \'\\n\' + source\r\n               .replace(/\\n+\\s{2,}/g, \' \')\r\n               + \'\\n\';\r\n         }\r\n      },\r\n      getValue(selector = required(), prop_name = required()) {\r\n         return (el = (selector instanceof HTMLElement) ? selector : document.body?.querySelector(selector))\r\n            ? getComputedStyle(el).getPropertyValue(prop_name) : null;\r\n      },\r\n   },\r\n\r\n   getPlayerState(state) {\r\n      return {\r\n         \'-1\': \'UNSTARTED\',\r\n         0: \'ENDED\',\r\n         1: \'PLAYING\',\r\n         2: \'PAUSED\',\r\n         3: \'BUFFERING\',\r\n         5: \'CUED\'\r\n      }[state || movie_player.getPlayerState()];\r\n   },\r\n\r\n   videoElement: (() => {\r\n      const videoSelector = \'#movie_player:not(.ad-showing) video\';\r\n      document.addEventListener(\'canplay\', ({ target }) => {\r\n         target.matches(videoSelector) && (NOVA.videoElement = target);\r\n      }, { capture: true, once: true });\r\n      document.addEventListener(\'play\', ({ target }) => {\r\n         target.matches(videoSelector) && (NOVA.videoElement = target);\r\n      }, true);\r\n   })(),\r\n\r\n   isFullscreen: () => (\r\n      movie_player.classList.contains(\'ytp-fullscreen\')\r\n      || (movie_player.hasOwnProperty(\'isFullscreen\') && movie_player.isFullscreen())\r\n   ),\r\n\r\n   log() {\r\n      if (this.DEBUG && arguments.length) {\r\n         console.groupCollapsed(...arguments);\r\n         console.trace();\r\n         console.groupEnd();\r\n      }\r\n   }\r\n}\r\n\r\n\r\nconst Plugins = {\r\n   run: ({ user_settings, app_ver }) => {\r\n      if (!window.nova_plugins?.length) return console.error(\'nova_plugins empty\', window.nova_plugins);\r\n      if (!user_settings) return console.error(\'user_settings empty\', user_settings);\r\n      NOVA.currentPage = (function () {\r\n         const pathnameArray = location.pathname.split(\'/\').filter(Boolean);\r\n         const [page, channelTab] = [pathnameArray[0], pathnameArray.pop()];\r\n         NOVA.channelTab = [\'featured\', \'videos\', \'shorts\', \'streams\', \'playlists\', \'community\', \'channels\', \'about\'].includes(channelTab) ? channelTab : false;\r\n         return (page != \'live_chat\')\r\n            && ([\'channel\', \'c\', \'user\'].includes(page)\r\n               || page?.startsWith(\'@\')\r\n               || /[A-Z\\d_]/.test(page)\r\n               || NOVA.channelTab\r\n            ) ? \'channel\' : (page == \'clip\') ? \'watch\' : page || \'home\';\r\n      })();\r\n      NOVA.isMobile = location.host == \'m.youtube.com\';\r\n      let logTableArray = [],\r\n         logTableStatus,\r\n         logTableTime;\r\n      window.nova_plugins?.forEach(plugin => {\r\n         const pagesAllowList = plugin?.run_on_pages?.split(\',\').map(p => p.trim().toLowerCase()).filter(Boolean);\r\n         logTableTime = 0;\r\n         logTableStatus = false;\r\n         if (!pluginChecker(plugin)) {\r\n            console.error(\'Plugin invalid\\n\', plugin);\r\n            alert(\'Plugin invalid: \' + plugin?.id);\r\n            logTableStatus = \'INVALID\';\r\n         }\r\n         else if (plugin.was_init && !plugin.restart_on_location_change) {\r\n            logTableStatus = \'skiped\';\r\n         }\r\n         else if (!user_settings.hasOwnProperty(plugin.id)) {\r\n            logTableStatus = \'off\';\r\n         }\r\n         else if (\r\n            (\r\n               pagesAllowList?.includes(NOVA.currentPage)\r\n               || (pagesAllowList?.includes(\'*\') && !pagesAllowList?.includes(\'-\' + NOVA.currentPage))\r\n            )\r\n            && (!NOVA.isMobile || (NOVA.isMobile && !pagesAllowList?.includes(\'-mobile\')))\r\n         ) {\r\n            try {\r\n               const startTableTime = performance.now();\r\n               plugin.was_init = true;\r\n               plugin._runtime(user_settings);\r\n               logTableTime = (performance.now() - startTableTime).toFixed(2);\r\n               logTableStatus = true;\r\n            } catch (err) {\r\n               console.groupEnd(\'plugins status\');\r\n               console.error(`[ERROR PLUGIN] ${plugin.id}\\n${err.stack}\\n\\nPlease report the bug: https://github.com/raingart/Nova-YouTube-extension/issues/new?body=` + encodeURIComponent(app_ver + \' | \' + navigator.userAgent));\r\n               if (user_settings.report_issues && _pluginsCaptureException) {\r\n                  _pluginsCaptureException({\r\n                     \'trace_name\': plugin.id,\r\n                     \'err_stack\': err.stack,\r\n                     \'app_ver\': app_ver,\r\n                     \'confirm_msg\': `ERROR in Nova YouTube™\\n\\nCrash plugin: \"${plugin.title || plugin.id}\"\\nPlease report the bug or disable the plugin\\n\\nSend the bug raport to developer?`,\r\n                  });\r\n               }\r\n               console.groupCollapsed(\'plugins status\');\r\n               logTableStatus = \'ERROR\';\r\n            }\r\n         }\r\n         logTableArray.push({\r\n            \'launched\': logTableStatus,\r\n            \'name\': plugin?.id,\r\n            \'time init (ms)\': logTableTime,\r\n         });\r\n      });\r\n      console.table(logTableArray);\r\n      console.groupEnd(\'plugins status\');\r\n      function pluginChecker(plugin) {\r\n         const result = plugin?.id && plugin.run_on_pages && \'function\' === typeof plugin._runtime;\r\n         if (!result) {\r\n            console.error(\'plugin invalid:\\n\', {\r\n               \'id\': plugin?.id,\r\n               \'run_on_pages\': plugin?.run_on_pages,\r\n               \'_runtime\': \'function\' === typeof plugin?._runtime,\r\n            });\r\n         }\r\n         return result;\r\n      }\r\n   },\r\n}\r\nlanderPlugins();\r\nfunction landerPlugins() {\r\n   processLander();\r\n   function processLander() {\r\n      const plugins_lander = setInterval(() => {\r\n         const domLoaded = document?.readyState != \'loading\';\r\n         if (!domLoaded) return console.debug(\'waiting, page loading..\');\r\n         clearInterval(plugins_lander);\r\n         console.groupCollapsed(\'plugins status\');\r\n         Plugins.run({\r\n            \'user_settings\': user_settings,\r\n            \'app_ver\': \'0.43.0\',\r\n         });\r\n      }, 500);\r\n   }\r\n   let prevURL = location.href;\r\n   const isURLChanged = () => prevURL == location.href ? false : prevURL = location.href;\r\n   if (isMobile = (location.host == \'m.youtube.com\')) {\r\n      window.addEventListener(\'transitionend\', ({ target }) => target.id == \'progress\' && isURLChange() && processLander());\r\n   }\r\n   else {\r\n      document.addEventListener(\'yt-navigate-start\', () => isURLChanged() && processLander());\r\n   }\r\n}\r\nfunction _pluginsCaptureException({ trace_name, err_stack, confirm_msg, app_ver }) {\r\n}";
          view.evaluateJavascript(script, null);
        }
      }

//  private boolean getAdBlockSetting(Context context) {
//    SharedPreferences sharedPreferences = context.getSharedPreferences("AdBlockPref", context.MODE_PRIVATE);
//    return sharedPreferences.getBoolean("adBlock", false);
//  }
 
//  @Override
//  public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//    isAdBlocked = getAdBlockSetting(view.getContext());
//    if (isAdBlocked) {
//      if (blockedHosts == null) {
//        isPopulatingHosts = true;
//        populateBlockedHosts(view.getContext());
//        return super.shouldInterceptRequest(view, url);
//       }
//      return shouldBlockRequest(url);
//    }
//    return super.shouldInterceptRequest(view, url);
//  }

//  @Override
//  public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//    String url = request.getUrl().toString();
//    isAdBlocked = getAdBlockSetting(view.getContext());
//    if (isAdBlocked) {
//      if (blockedHosts == null) {
//        isPopulatingHosts = true;
//        populateBlockedHosts(view.getContext());
//        return super.shouldInterceptRequest(view, request);
//      }
//      return shouldBlockRequest(url);
//    }
//    return super.shouldInterceptRequest(view, request);
//  }

//  private void populateBlockedHosts(Context context) {
//    InputStream is = context.getResources().openRawResource(R.raw.adblock_serverlist);
//    BufferedReader br = new BufferedReader(new InputStreamReader(is));
//    String line;

//    if (is != null) {
//      try {
//        blockedHosts = new TreeMap<String, Object>();

//        while ((line = br.readLine()) != null) {
//          line = line.toLowerCase().trim();

//          if (!line.isEmpty() && !line.startsWith("#")) {
//            blockedHosts.put(line, null);
//          }
//        }
//        br.close();
//        isPopulatingHosts = false;
//      } catch (IOException e) {
//        blockedHosts = null;
//      }
//    }
//  }

//  private boolean isHostBlocked(String url) {
//    if ((blockedHosts == null) || isPopulatingHosts) return false;
//
//    try {
//      Uri uri = Uri.parse(url);
//      String host = uri.getHost().toLowerCase().trim();
//      return blockedHosts.containsKey(host);
//    }
//    catch(Exception e) {
//      return false;
//    }
//  }

//  private WebResourceResponse shouldBlockRequest(String url) {
//    if (isHostBlocked(url)) {
//      ByteArrayInputStream EMPTY = new ByteArrayInputStream("".getBytes());
//      return new WebResourceResponse("text/plain", "utf-8", EMPTY);
//    }
//    else {
//      return null;
//    }
//  }
}
