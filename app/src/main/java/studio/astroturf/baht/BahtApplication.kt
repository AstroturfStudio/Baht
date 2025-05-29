package studio.astroturf.baht

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics

class BahtApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Enable Crashlytics collection
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        // Log app startup
        FirebaseCrashlytics.getInstance().log("Baht app started successfully")
        FirebaseCrashlytics.getInstance().setCustomKey("app_version", "1.0")
        FirebaseCrashlytics.getInstance().setCustomKey("startup_time", System.currentTimeMillis())
    }
} 
