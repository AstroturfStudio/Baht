package studio.astroturf.baht.ads

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdManager {
    // Detect debug mode by checking if debuggable flag is set
    private fun isDebugMode(context: Context): Boolean =
        (context.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0

    // Use test ads in debug, real ads in release
    private fun getBannerAdUnitId(context: Context): String =
        if (isDebugMode(context)) {
            "ca-app-pub-3940256099942544/6300978111" // Test ID
        } else {
            "ca-app-pub-6439815249309596/9853512088" // Real ID
        }

    private fun getInterstitialAdUnitId(context: Context): String =
        if (isDebugMode(context)) {
            "ca-app-pub-3940256099942544/1033173712" // Test ID
        } else {
            "ca-app-pub-6439815249309596/3136060494" // Real ID
        }

    private var interstitialAd: InterstitialAd? = null
    private var isInterstitialLoading = false
    var interstitialAdCounter = mutableStateOf(0)

    fun initialize(
        context: Context,
        onComplete: () -> Unit = {},
    ) {
        MobileAds.initialize(context) {
            onComplete()
        }
        loadInterstitialAd(context)
    }

    private fun loadInterstitialAd(context: Context) {
        if (isInterstitialLoading) return

        isInterstitialLoading = true
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            getInterstitialAdUnitId(context),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isInterstitialLoading = false

                    ad.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                interstitialAd = null
                                loadInterstitialAd(context) // Load next ad
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                                interstitialAd = null
                                loadInterstitialAd(context)
                            }
                        }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    isInterstitialLoading = false
                }
            },
        )
    }

    fun showInterstitialAd(context: Context) {
        interstitialAdCounter.value++

        // Show ad every 3rd action
        if (interstitialAdCounter.value % 3 == 0) {
            if (interstitialAd != null) {
                interstitialAd?.show(context as androidx.activity.ComponentActivity)
            } else {
                loadInterstitialAd(context)
            }
        }
    }

    fun createBannerAdView(context: Context): AdView =
        AdView(context).apply {
            setAdSize(com.google.android.gms.ads.AdSize.BANNER)
            adUnitId = getBannerAdUnitId(context)
            loadAd(AdRequest.Builder().build())
        }
}
