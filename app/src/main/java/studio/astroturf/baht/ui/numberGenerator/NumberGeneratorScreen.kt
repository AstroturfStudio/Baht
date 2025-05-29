package studio.astroturf.baht.ui.numberGenerator

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import studio.astroturf.baht.R
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberGeneratorScreen(onBackClick: () -> Unit) {
    var minValue by remember { mutableStateOf("1") }
    var maxValue by remember { mutableStateOf("100") }
    var isGenerating by remember { mutableStateOf(false) }
    var generatedNumber by remember { mutableIntStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var animatingNumbers by remember { mutableStateOf(listOf<Int>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Number Generator",
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
                    .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Input Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    Text(
                        text = "Set Range",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        color = Color(0xFF121417),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        OutlinedTextField(
                            value = minValue,
                            onValueChange = { minValue = it },
                            label = { Text("Min") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            enabled = !isGenerating,
                        )
                        
                        OutlinedTextField(
                            value = maxValue,
                            onValueChange = { maxValue = it },
                            label = { Text("Max") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            enabled = !isGenerating,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Number Display
            AnimatedNumberDisplay(
                isGenerating = isGenerating,
                generatedNumber = generatedNumber,
                showResult = showResult,
                animatingNumbers = animatingNumbers,
                modifier = Modifier.size(200.dp),
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Result Card
            AnimatedVisibility(
                visible = showResult,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                ResultCard(generatedNumber = generatedNumber)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Generate Button
            Button(
                onClick = {
                    val min = minValue.toIntOrNull() ?: 1
                    val max = maxValue.toIntOrNull() ?: 100
                    if (min <= max && !isGenerating) {
                        isGenerating = true
                        showResult = false
                        // Generate random numbers for animation
                        animatingNumbers = (1..10).map { Random.nextInt(min, max + 1) }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isGenerating && isValidRange(minValue, maxValue),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3D99F5),
                    ),
                shape = RoundedCornerShape(12.dp),
            ) {
                GenerateButton(isGenerating = isGenerating)
            }
        }
    }

    LaunchedEffect(isGenerating) {
        if (isGenerating) {
            delay(2000) // Animation duration
            val min = minValue.toIntOrNull() ?: 1
            val max = maxValue.toIntOrNull() ?: 100
            generatedNumber = Random.nextInt(min, max + 1)
            isGenerating = false
            showResult = true
        }
    }
}

@Composable
fun AnimatedNumberDisplay(
    isGenerating: Boolean,
    generatedNumber: Int,
    showResult: Boolean,
    animatingNumbers: List<Int>,
    modifier: Modifier = Modifier,
) {
    val scale by animateFloatAsState(
        targetValue = if (isGenerating) 1.1f else 1f,
        animationSpec = tween(300),
        label = "scale",
    )

    val rotation by animateFloatAsState(
        targetValue = if (isGenerating) 360f else 0f,
        animationSpec =
            tween(
                durationMillis = if (isGenerating) 2000 else 0,
                easing = LinearEasing,
            ),
        label = "rotation",
    )

    Box(
        modifier =
            modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    rotationY = rotation * 0.5f // Subtle 3D rotation effect
                },
        contentAlignment = Alignment.Center,
    ) {
        // Background circle
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        color =
                            if (showResult) {
                                Color(0xFF4CAF50)
                            } else if (isGenerating) {
                                Color(0xFF3D99F5)
                            } else {
                                Color(0xFFE0E0E0)
                            },
                        shape = CircleShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            when {
                showResult -> {
                    Text(
                        text = generatedNumber.toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        textAlign = TextAlign.Center,
                    )
                }
                isGenerating && animatingNumbers.isNotEmpty() -> {
                    // Show cycling numbers during animation
                    val infiniteTransition = rememberInfiniteTransition(label = "numberCycle")
                    val cycleIndex by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = animatingNumbers.size.toFloat(),
                        animationSpec =
                            infiniteRepeatable(
                                animation = tween(200, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart,
                            ),
                        label = "cycleIndex",
                    )
                    
                    Text(
                        text = animatingNumbers[cycleIndex.toInt() % animatingNumbers.size].toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        textAlign = TextAlign.Center,
                    )
                }
                else -> {
                    Text(
                        text = "?",
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF61758A),
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
fun GenerateButton(isGenerating: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "generateAnimation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isGenerating) 360f else 0f,
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
        if (isGenerating) {
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
            text = if (isGenerating) "Generating..." else "Generate Number",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
        )
    }
}

@Composable
fun ResultCard(generatedNumber: Int) {
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
                containerColor = Color(0xFF4CAF50),
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
                text = "🎲 Your Number! 🎲",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Generated: $generatedNumber",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
            )
        }
    }
}

private fun isValidRange(minValue: String, maxValue: String): Boolean {
    val min = minValue.toIntOrNull()
    val max = maxValue.toIntOrNull()
    return min != null && max != null && min <= max
} 