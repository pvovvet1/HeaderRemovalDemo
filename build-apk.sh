#!/bin/bash

echo "================================"
echo "Android APK Builder Script"
echo "================================"
echo ""

# Check if we need to download Gradle wrapper
if [ ! -f "gradlew" ]; then
    echo "📥 Downloading Gradle wrapper..."

    # Create gradle wrapper files
    mkdir -p gradle/wrapper

    # Download gradle wrapper jar
    curl -L https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar \
         -o gradle/wrapper/gradle-wrapper.jar

    # Create gradle wrapper properties
    cat > gradle/wrapper/gradle-wrapper.properties <<EOF
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.0-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
EOF

    # Download gradlew script
    curl -L https://raw.githubusercontent.com/gradle/gradle/master/gradlew -o gradlew
    chmod +x gradlew
fi

echo "🔨 Building APK..."
echo ""

# Make gradlew executable
chmod +x gradlew

# Build the APK
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ BUILD SUCCESSFUL!"
    echo ""
    echo "📦 APK Location:"
    echo "   app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "📱 To install on your phone:"
    echo "   1. Transfer the APK to your Android phone"
    echo "   2. Open the APK file on your phone"
    echo "   3. Allow 'Install from Unknown Sources' if prompted"
    echo "   4. Tap 'Install'"
    echo ""
else
    echo ""
    echo "❌ BUILD FAILED"
    echo "Check the error messages above"
    echo ""
fi
