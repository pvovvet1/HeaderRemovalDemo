package com.example.headerremoval;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "HeaderRemovalDemo";
    private WebView webView;
    private TextView logTextView;
    private StringBuilder logBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        webView = findViewById(R.id.webView);
        logTextView = findViewById(R.id.logTextView);
        Button testButton = findViewById(R.id.testButton);
        logBuilder = new StringBuilder();

        // Configure WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // Set custom WebViewClient that removes headers
        webView.setWebViewClient(new HeaderRemovalWebViewClient());

        // Test button click listener
        testButton.setOnClickListener(v -> startHeaderRemovalTest());
    }

    private void startHeaderRemovalTest() {
        addLog("🚀 Starting header removal test...");
        addLog("📍 Target URL: https://httpbin.org/headers");
        addLog("⏳ Loading request...\n");

        // Load the test URL
        webView.loadUrl("https://httpbin.org/headers");
    }

    private void addLog(String message) {
        logBuilder.append(message).append("\n");
        logTextView.setText(logBuilder.toString());
        Log.d(TAG, message);
    }

    // Custom WebViewClient that intercepts requests and removes X-Requested-With header
    private class HeaderRemovalWebViewClient extends WebViewClient {

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            Map<String, String> headers = request.getRequestHeaders();

            addLog("📨 Intercepted request to: " + url);

            // Check if X-Requested-With header exists
            if (headers.containsKey("X-Requested-With")) {
                addLog("🔍 Found X-Requested-With: " + headers.get("X-Requested-With"));
                addLog("🛠️ Removing X-Requested-With header...");
            } else {
                addLog("ℹ️ No X-Requested-With header found");
            }

            // Create new request without X-Requested-With header
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod(request.getMethod());

                // Copy all headers EXCEPT X-Requested-With
                int headerCount = 0;
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    if (!header.getKey().equals("X-Requested-With")) {
                        connection.setRequestProperty(header.getKey(), header.getValue());
                        headerCount++;
                    }
                }

                addLog("📤 Sending clean request with " + headerCount + " headers");

                // Get response
                connection.connect();
                int responseCode = connection.getResponseCode();
                addLog("📥 Received response: " + responseCode);

                // Read response
                InputStream inputStream = connection.getInputStream();
                String mimeType = connection.getContentType();
                String encoding = connection.getContentEncoding();

                addLog("📄 Page loaded - check WebView for results!");
                addLog("✅ Header removal completed!\n");

                // Return the response
                return new WebResourceResponse(
                    mimeType != null ? mimeType : "text/html",
                    encoding != null ? encoding : "utf-8",
                    inputStream
                );

            } catch (IOException e) {
                addLog("❌ Error: " + e.getMessage());
                Log.e(TAG, "Request failed", e);
                return null;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            addLog("🎉 Page fully loaded!");
        }
    }
}
