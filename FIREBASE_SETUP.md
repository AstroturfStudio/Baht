# Firebase Crashlytics Setup

Firebase Crashlytics has been added to the Baht Android project. To complete the setup, follow these steps:

## 1. Firebase Console Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select an existing project
3. Add an Android app to your project:
   - **Package name**: `studio.astroturf.baht`
   - **App nickname**: Baht (optional)
   - **Debug signing certificate SHA-1**: (optional, for testing)

## 2. Download Configuration File

1. Download the `google-services.json` file from Firebase Console
2. Replace the placeholder `app/google-services.json` file with your downloaded file

## 3. Enable Crashlytics

1. In Firebase Console, go to **Build** > **Crashlytics**
2. Click **Enable Crashlytics**
3. Follow the setup wizard

## 4. Test the Integration

After completing the setup, you can test Crashlytics by:

1. Building and running the app
2. Using the utility methods in `CrashlyticsUtils`:

```kotlin
import studio.astroturf.baht.utils.CrashlyticsUtils

// Log a custom message
CrashlyticsUtils.logMessage("App started successfully")

// Log an exception
try {
    // Some operation that might fail
} catch (e: Exception) {
    CrashlyticsUtils.logException(e)
}

// Log custom events
CrashlyticsUtils.logEvent("button_click", 
    "screen" to "home",
    "button_id" to "lucky_draw"
)

// Set user properties
CrashlyticsUtils.setUserProperty("user_type", "premium")
CrashlyticsUtils.setUserId("user123")
```

## 5. Force a Test Crash (Optional)

To verify Crashlytics is working, you can add a test crash:

```kotlin
// Add this to any button click for testing
throw RuntimeException("Test Crash for Crashlytics")
```

## What's Already Configured

✅ Firebase BoM dependency added  
✅ Crashlytics and Analytics dependencies added  
✅ Google Services plugin applied  
✅ Firebase Crashlytics plugin applied  
✅ Application class (`BahtApplication`) created and configured  
✅ Crashlytics utilities class (`CrashlyticsUtils`) created  
✅ AndroidManifest.xml updated to use custom Application class  

## Next Steps

1. Replace the placeholder `google-services.json` with your actual file
2. Build and test the app
3. Check Firebase Console for crash reports and events
4. Integrate `CrashlyticsUtils` methods throughout your app for better error tracking 