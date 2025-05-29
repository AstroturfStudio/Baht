package studio.astroturf.baht.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsUtils {
    private val crashlytics = FirebaseCrashlytics.getInstance()

    fun logException(exception: Throwable) {
        crashlytics.recordException(exception)
    }

    fun logMessage(message: String) {
        crashlytics.log(message)
    }

    fun setUserProperty(
        key: String,
        value: String,
    ) {
        crashlytics.setCustomKey(key, value)
    }

    fun setUserId(userId: String) {
        crashlytics.setUserId(userId)
    }

    fun logEvent(
        eventName: String,
        vararg parameters: Pair<String, Any>,
    ) {
        crashlytics.log("Event: $eventName")
        parameters.forEach { (key, value) ->
            when (value) {
                is String -> crashlytics.setCustomKey(key, value)
                is Int -> crashlytics.setCustomKey(key, value)
                is Long -> crashlytics.setCustomKey(key, value)
                is Float -> crashlytics.setCustomKey(key, value)
                is Double -> crashlytics.setCustomKey(key, value)
                is Boolean -> crashlytics.setCustomKey(key, value)
                else -> crashlytics.setCustomKey(key, value.toString())
            }
        }
    }
} 
