package studio.astroturf.baht.ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun BannerAdView(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    AndroidView(
        modifier =
            modifier
                .fillMaxWidth()
                .height(50.dp),
        factory = { context ->
            AdManager.createBannerAdView(context)
        },
    )
} 
