# Android Header Removal Demo

This Android app demonstrates how to remove the `X-Requested-With` header from WebView requests using request interception.

## Features

- Intercepts all WebView HTTP requests
- Removes the `X-Requested-With` header automatically
- Shows real-time logs of the interception process
- Tests against httpbin.org to verify header removal
- Visual feedback in WebView showing success/failure

## Project Structure

```
HeaderRemovalDemo/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/headerremoval/
│   │   │   └── MainActivity.java          # Main app logic with WebViewClient
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml     # UI layout
│   │   │   └── values/
│   │   │       ├── strings.xml           # String resources
│   │   │       └── themes.xml            # App theme
│   │   └── AndroidManifest.xml           # Permissions & app config
│   ├── build.gradle                       # App-level Gradle config
│   └── proguard-rules.pro                # ProGuard rules
├── build.gradle                           # Project-level Gradle config
├── settings.gradle                        # Gradle settings
└── gradle.properties                      # Gradle properties
```

## Setup Instructions

### Prerequisites

1. **Install Android Studio**
   - Download from: https://developer.android.com/studio
   - Install with default settings
   - Accept license agreements during first launch

2. **Enable Developer Options on Android Device** (if using physical device)
   - Go to: Settings → About Phone
   - Tap "Build Number" 7 times
   - Go to: Settings → Developer Options
   - Enable "USB Debugging"

### Opening the Project

1. **Open Android Studio**
2. **Select**: File → Open
3. **Navigate to**: `HeaderRemovalDemo` folder
4. **Click**: OK
5. **Wait for Gradle sync** (takes 2-5 minutes on first open)

### Running the App

#### Option A: Physical Device (Recommended)

1. Connect your Android phone via USB
2. Click the green ▶️ "Run" button in Android Studio
3. Select your device from the list
4. Wait for app to install and launch

#### Option B: Emulator

1. Click: Tools → AVD Manager
2. Click: Create Virtual Device
3. Select a phone model (e.g., Pixel 4)
4. Download a system image (API 30+)
5. Click: Finish
6. Click the ▶️ button next to your emulator
7. Once emulator starts, click the green ▶️ "Run" button

## How to Use the App

1. **Launch the app** on your device/emulator
2. **Click** the "🔍 Start Header Removal Test" button
3. **Watch the logs** in the middle section showing:
   ```
   🚀 Starting header removal test...
   📨 Intercepted request to: https://httpbin.org/headers
   🔍 Found X-Requested-With: com.example.headerremoval
   🛠️ Removing X-Requested-With header...
   📤 Sending clean request with X headers
   📥 Received response: 200
   📄 Page loaded - check WebView for results!
   ✅ Header removal completed!
   ```
4. **Check the WebView** at the bottom to see the HTTP headers returned by httpbin.org
5. **Verify** that `X-Requested-With` is NOT in the headers list

## How It Works

The app uses a custom `WebViewClient` that overrides `shouldInterceptRequest()`:

1. **Intercept**: Catches every HTTP request made by the WebView
2. **Filter**: Creates a new request without the `X-Requested-With` header
3. **Forward**: Sends the clean request using `HttpURLConnection`
4. **Return**: Provides the response back to the WebView

### Key Code Components

- **MainActivity.java**: Contains the main logic
  - `HeaderRemovalWebViewClient`: Custom WebView client
  - `shouldInterceptRequest()`: Intercepts and modifies requests
  - `addLog()`: Displays real-time logs to the user

- **AndroidManifest.xml**:
  - `INTERNET` permission for network access
  - `usesCleartextTraffic="true"` to allow HTTP requests

- **activity_main.xml**: UI layout with test button, log view, and WebView

## Troubleshooting

### Build Errors

If you get Gradle sync errors:
```
File → Invalidate Caches and Restart → Invalidate and Restart
```

### App Crashes

1. Check Logcat tab in Android Studio for error details
2. Verify all permissions are in AndroidManifest.xml
3. Ensure `usesCleartextTraffic="true"` is set

### Internet Permission Denied

Make sure AndroidManifest.xml includes:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Cleartext HTTP Traffic Error

Ensure the `<application>` tag includes:
```xml
android:usesCleartextTraffic="true"
```

## Customization

### Test Different URLs

Change the test URL in MainActivity.java:
```java
webView.loadUrl("https://your-target-url.com");
```

### Remove Additional Headers

Modify the header filtering logic in `shouldInterceptRequest()`:
```java
for (Map.Entry<String, String> header : headers.entrySet()) {
    if (!header.getKey().equals("X-Requested-With") &&
        !header.getKey().equals("Another-Header")) {
        connection.setRequestProperty(header.getKey(), header.getValue());
    }
}
```

### Add Custom Headers

Add headers to the request:
```java
connection.setRequestProperty("User-Agent", "Custom-Agent");
connection.setRequestProperty("Custom-Header", "Custom-Value");
```

## Technical Details

- **Min SDK**: Android 5.0 (API 21)
- **Target SDK**: Android 13 (API 33)
- **Language**: Java
- **Dependencies**: AndroidX libraries
- **Network**: HttpURLConnection for request forwarding

## Use Cases

This technique can be used for:
- Web scraping applications
- Bypassing WebView detection
- Custom HTTP client implementations
- Testing server behavior without WebView headers
- Privacy-focused browsing apps

## License

This is a demonstration project for educational purposes.

## Support

For issues or questions:
- Check the troubleshooting section above
- Review Android Studio's Logcat for detailed error messages
- Ensure all dependencies are properly synced
