#!/bin/bash

echo "🎲 Building Baht for Play Store Release"
echo "======================================"

# Check if keystore exists
if [ ! -f "release-key.keystore" ]; then
    echo "❌ Release keystore not found!"
    echo "Run ./generate-keystore.sh first to create signing key"
    exit 1
fi

# Check if signing properties are set
if [ -z "$RELEASE_STORE_PASSWORD" ]; then
    echo "❌ Signing properties not found!"
    echo "Make sure you have set these in ~/.gradle/gradle.properties or local.properties:"
    echo "RELEASE_STORE_FILE=release-key.keystore"
    echo "RELEASE_STORE_PASSWORD=your_password"
    echo "RELEASE_KEY_ALIAS=your_alias"
    echo "RELEASE_KEY_PASSWORD=your_password"
    exit 1
fi

# Clean previous builds
echo "🧹 Cleaning previous builds..."
./gradlew clean

# Build release AAB (recommended for Play Store)
echo "📦 Building release AAB..."
./gradlew bundleRelease

# Build release APK (for testing)
echo "📱 Building release APK..."
./gradlew assembleRelease

# Check if builds succeeded
if [ -f "app/build/outputs/bundle/release/app-release.aab" ]; then
    echo "✅ AAB build successful!"
    echo "📍 Location: app/build/outputs/bundle/release/app-release.aab"
else
    echo "❌ AAB build failed!"
    exit 1
fi

if [ -f "app/build/outputs/apk/release/app-release.apk" ]; then
    echo "✅ APK build successful!"
    echo "📍 Location: app/build/outputs/apk/release/app-release.apk"
else
    echo "❌ APK build failed!"
    exit 1
fi

# Get file sizes
AAB_SIZE=$(du -h "app/build/outputs/bundle/release/app-release.aab" | cut -f1)
APK_SIZE=$(du -h "app/build/outputs/apk/release/app-release.apk" | cut -f1)

echo ""
echo "🎉 Build Summary"
echo "==============="
echo "✅ AAB (Play Store): $AAB_SIZE"
echo "✅ APK (Testing): $APK_SIZE"
echo ""
echo "Next Steps:"
echo "1. Test the APK on a device: adb install app/build/outputs/apk/release/app-release.apk"
echo "2. Upload AAB to Play Console: app/build/outputs/bundle/release/app-release.aab"
echo "3. Complete store listing with assets from PLAY_STORE_RELEASE.md" 