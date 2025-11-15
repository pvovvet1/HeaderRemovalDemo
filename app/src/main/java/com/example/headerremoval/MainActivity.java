package com.example.headerremoval;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "HeaderRemovalDemo";
    private WebView webView;
    private TextView logTextView;
    private StringBuilder logBuilder;
    private EditText customUrlInput;
    private EditText customHeaderKey;
    private EditText customHeaderValue;
    private EditText postDataInput;
    private RadioGroup requestMethodGroup;
    private CheckBox removeXRequestedWith;
    private CheckBox removeUserAgent;
    private CheckBox removeReferer;
    private CheckBox removeSecHeaders;
    private List<String> headersToRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        webView = findViewById(R.id.webView);
        logTextView = findViewById(R.id.logTextView);
        customUrlInput = findViewById(R.id.customUrlInput);
        customHeaderKey = findViewById(R.id.customHeaderKey);
        customHeaderValue = findViewById(R.id.customHeaderValue);
        postDataInput = findViewById(R.id.postDataInput);
        requestMethodGroup = findViewById(R.id.requestMethodGroup);

        // Checkboxes for header removal
        removeXRequestedWith = findViewById(R.id.removeXRequestedWith);
        removeUserAgent = findViewById(R.id.removeUserAgent);
        removeReferer = findViewById(R.id.removeReferer);
        removeSecHeaders = findViewById(R.id.removeSecHeaders);

        // Pre-check X-Requested-With by default
        removeXRequestedWith.setChecked(true);

        logBuilder = new StringBuilder();
        headersToRemove = new ArrayList<>();

        // Configure WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // Set custom WebViewClient
        webView.setWebViewClient(new HeaderRemovalWebViewClient());

        // Button listeners
        findViewById(R.id.testButton).setOnClickListener(v -> testCustomUrl());
        findViewById(R.id.testHttpBinHeaders).setOnClickListener(v -> testUrl("https://httpbin.org/headers"));
        findViewById(R.id.testHttpBinGet).setOnClickListener(v -> testUrl("https://httpbin.org/get"));
        findViewById(R.id.testHttpBinUserAgent).setOnClickListener(v -> testUrl("https://httpbin.org/user-agent"));
        findViewById(R.id.testIpify).setOnClickListener(v -> testUrl("https://api.ipify.org?format=json"));
        findViewById(R.id.clearLogs).setOnClickListener(v -> clearLogs());

        // Show/hide POST data field based on method selection
        requestMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioPost) {
                postDataInput.setVisibility(View.VISIBLE);
            } else {
                postDataInput.setVisibility(View.GONE);
            }
        });
    }

    private void testCustomUrl() {
        String url = customUrlInput.getText().toString().trim();
        if (url.isEmpty()) {
            addLog("❌ Please enter a URL");
            return;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        testUrl(url);
    }

    private void testUrl(String url) {
        clearLogs();
        addLog("🚀 Starting header removal test...");
        addLog("📍 Target URL: " + url);

        // Build list of headers to remove based on checkboxes
        headersToRemove.clear();
        if (removeXRequestedWith.isChecked()) {
            headersToRemove.add("X-Requested-With");
        }
        if (removeUserAgent.isChecked()) {
            headersToRemove.add("User-Agent");
        }
        if (removeReferer.isChecked()) {
            headersToRemove.add("Referer");
        }

        if (!headersToRemove.isEmpty()) {
            addLog("🛠️ Will remove headers: " + String.join(", ", headersToRemove));
        }

        String customKey = customHeaderKey.getText().toString().trim();
        String customValue = customHeaderValue.getText().toString().trim();
        if (!customKey.isEmpty() && !customValue.isEmpty()) {
            addLog("➕ Will add custom header: " + customKey + ": " + customValue);
        }

        addLog("⏳ Loading request...\n");
        webView.loadUrl(url);
    }

    private void clearLogs() {
        logBuilder.setLength(0);
        logTextView.setText("");
    }

    private void addLog(String message) {
        logBuilder.append(message).append("\n");
        logTextView.setText(logBuilder.toString());
        Log.d(TAG, message);

        // Auto-scroll to bottom
        final TextView tv = logTextView;
        tv.post(() -> {
            int scrollAmount = tv.getLayout().getLineTop(tv.getLineCount()) - tv.getHeight();
            if (scrollAmount > 0) {
                tv.scrollTo(0, scrollAmount);
            }
        });
    }

    private class HeaderRemovalWebViewClient extends WebViewClient {

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            Map<String, String> headers = request.getRequestHeaders();

            addLog("📨 Intercepted: " + url);

            try {
                // Determine request method
                String method = "GET";
                int checkedId = requestMethodGroup.getCheckedRadioButtonId();
                if (checkedId == R.id.radioPost) {
                    method = "POST";
                }

                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod(method);
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);

                // Count removed headers
                List<String> removedHeaders = new ArrayList<>();

                // Copy headers, removing specified ones and Sec-Ch-* if requested
                int headerCount = 0;
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    String headerName = header.getKey();
                    boolean shouldRemove = false;

                    // Check if this header should be removed
                    if (headersToRemove.contains(headerName)) {
                        shouldRemove = true;
                        removedHeaders.add(headerName);
                    }

                    // Check for Sec-Ch-* headers if that option is checked
                    if (removeSecHeaders.isChecked() && headerName.startsWith("Sec-Ch-")) {
                        shouldRemove = true;
                        if (!removedHeaders.contains(headerName)) {
                            removedHeaders.add(headerName);
                        }
                    }

                    if (!shouldRemove) {
                        connection.setRequestProperty(headerName, header.getValue());
                        headerCount++;
                    }
                }

                // Add custom header if provided
                String customKey = customHeaderKey.getText().toString().trim();
                String customValue = customHeaderValue.getText().toString().trim();
                if (!customKey.isEmpty() && !customValue.isEmpty()) {
                    connection.setRequestProperty(customKey, customValue);
                    headerCount++;
                    addLog("➕ Added custom header: " + customKey);
                }

                // Log removed headers
                if (!removedHeaders.isEmpty()) {
                    addLog("🗑️ Removed headers: " + String.join(", ", removedHeaders));
                }

                addLog("📤 Sending " + method + " request with " + headerCount + " headers");

                // Handle POST data if applicable
                if (method.equals("POST")) {
                    String postData = postDataInput.getText().toString().trim();
                    if (!postData.isEmpty()) {
                        connection.setDoOutput(true);
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                        OutputStream os = connection.getOutputStream();
                        os.write(postData.getBytes("UTF-8"));
                        os.flush();
                        os.close();

                        addLog("📝 POST data sent: " + postData);
                    }
                }

                // Get response
                connection.connect();
                int responseCode = connection.getResponseCode();
                addLog("📥 Response: " + responseCode + " " + connection.getResponseMessage());

                // Read response
                InputStream inputStream;
                if (responseCode >= 200 && responseCode < 400) {
                    inputStream = connection.getInputStream();
                } else {
                    inputStream = connection.getErrorStream();
                }

                String mimeType = connection.getContentType();
                if (mimeType != null && mimeType.contains(";")) {
                    mimeType = mimeType.split(";")[0].trim();
                }

                String encoding = connection.getContentEncoding();

                addLog("✅ Request completed!\n");

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
