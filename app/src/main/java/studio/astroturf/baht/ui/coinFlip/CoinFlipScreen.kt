package studio.astroturf.baht.ui.coinFlip

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import studio.astroturf.baht.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinFlipScreen(onBackClick: () -> Unit) {
    var isFlipping by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<CoinSide?>(null) }
    var showResult by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Coin Flip",
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Coin Animation
            AnimatedCoin(
                isFlipping = isFlipping,
                result = result,
                showResult = showResult,
                modifier = Modifier.size(200.dp),
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Result Display
            AnimatedVisibility(
                visible = showResult && result != null,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                ResultCard(result = result ?: CoinSide.HEADS)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Flip Button
            Button(
                onClick = {
                    if (!isFlipping) {
                        isFlipping = true
                        showResult = false
                        result = null
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isFlipping,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3D99F5),
                    ),
                shape = RoundedCornerShape(12.dp),
            ) {
                FlipButton(isFlipping = isFlipping)
            }
        }
    }

    LaunchedEffect(isFlipping) {
        if (isFlipping) {
            delay(2000) // Animation duration
            result = if (kotlin.random.Random.nextBoolean()) CoinSide.HEADS else CoinSide.TAILS
            isFlipping = false
            showResult = true
        }
    }
}

@Composable
fun AnimatedCoin(
    isFlipping: Boolean,
    result: CoinSide?,
    showResult: Boolean,
    modifier: Modifier = Modifier,
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipping) 1800f else 0f, // 5 full rotations
        animationSpec =
            tween(
                durationMillis = if (isFlipping) 2000 else 0,
                easing = LinearEasing,
            ),
        label = "rotation",
    )

    val scale by animateFloatAsState(
        targetValue = if (isFlipping) 1.1f else 1f,
        animationSpec = tween(300),
        label = "scale",
    )

    Box(
        modifier =
            modifier
                .graphicsLayer {
                    rotationY = rotation
                    scaleX = scale
                    scaleY = scale
                },
        contentAlignment = Alignment.Center,
    ) {
        // Coin background
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        color =
                            if (showResult && result != null) {
                                when (result) {
                                    CoinSide.HEADS -> Color(0xFFFFD700) // Gold
                                    CoinSide.TAILS -> Color(0xFFC0C0C0) // Silver
                                }
                            } else {
                                Color(0xFFE0E0E0)
                            },
                        shape = CircleShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            if (showResult && result != null) {
                Text(
                    text =
                        when (result) {
                            CoinSide.HEADS -> "👑"
                            CoinSide.TAILS -> "🪙"
                        },
                    fontSize = 72.sp,
                    textAlign = TextAlign.Center,
                )
            } else {
                Text(
                    text = "🪙",
                    fontSize = 72.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun FlipButton(isFlipping: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "flipAnimation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isFlipping) 360f else 0f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "rotation",
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp),
    ) {
        if (isFlipping) {
            Box(
                modifier =
                    Modifier
                        .size(24.dp)
                        .rotate(rotation)
                        .background(
                            Color.White.copy(alpha = 0.3f),
                            CircleShape,
                        ),
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        Text(
            text = if (isFlipping) "Flipping..." else "Flip Coin",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
        )
    }
}

@Composable
fun ResultCard(result: CoinSide) {
    val infiniteTransition = rememberInfiniteTransition(label = "resultGlow")
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "glow",
    )

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    alpha = glow
                    scaleX = 1f + (glow - 0.8f) * 0.2f
                    scaleY = 1f + (glow - 0.8f) * 0.2f
                },
        colors =
            CardDefaults.cardColors(
                containerColor =
                    when (result) {
                        CoinSide.HEADS -> Color(0xFF4CAF50)
                        CoinSide.TAILS -> Color(0xFF2196F3)
                    },
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text =
                    when (result) {
                        CoinSide.HEADS -> "👑 HEADS! 👑"
                        CoinSide.TAILS -> "🪙 TAILS! 🪙"
                    },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text =
                    when (result) {
                        CoinSide.HEADS -> "The coin landed on heads!"
                        CoinSide.TAILS -> "The coin landed on tails!"
                    },
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
            )
        }
    }
}

enum class CoinSide {
    HEADS,
    TAILS,
} 
