package studio.astroturf.baht.ui.diceRoll

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
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import studio.astroturf.baht.R
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceRollScreen(onBackClick: () -> Unit) {
    var numberOfDice by remember { mutableIntStateOf(1) }
    var isRolling by remember { mutableStateOf(false) }
    var diceResults by remember { mutableStateOf(listOf<Int>()) }
    var showResults by remember { mutableStateOf(false) }
    var animatingDice by remember { mutableStateOf(listOf<Int>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Dice Roll",
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
            // Dice Count Selection
            if (!isRolling) {
                Text(
                    text = "Number of Dice",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 32.dp),
                ) {
                    for (i in 1..3) {
                        DiceCountButton(
                            count = i,
                            isSelected = numberOfDice == i,
                            onClick = { numberOfDice = i },
                        )
                    }
                }
            }

            // Dice Display
            if (numberOfDice == 1) {
                AnimatedSingleDice(
                    isRolling = isRolling,
                    result = diceResults.firstOrNull() ?: 1,
                    showResult = showResults,
                    modifier = Modifier.size(200.dp),
                )
            } else {
                AnimatedMultipleDice(
                    numberOfDice = numberOfDice,
                    isRolling = isRolling,
                    results = diceResults,
                    showResults = showResults,
                    modifier = Modifier.height(150.dp),
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Result Display
            AnimatedVisibility(
                visible = showResults && diceResults.size == numberOfDice,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                ResultCard(
                    diceResults = diceResults,
                    numberOfDice = numberOfDice,
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Roll Button
            Button(
                onClick = {
                    if (!isRolling) {
                        isRolling = true
                        showResults = false
                        diceResults = emptyList()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isRolling,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3D99F5),
                    ),
                shape = RoundedCornerShape(12.dp),
            ) {
                RollButton(isRolling = isRolling)
            }
        }
    }

    LaunchedEffect(isRolling) {
        if (isRolling) {
            delay(2000) // Animation duration
            diceResults = (1..numberOfDice).map { Random.nextInt(1, 7) }
            isRolling = false
            showResults = true
        }
    }
}

@Composable
fun DiceCountButton(
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .size(50.dp)
                .selectable(
                    selected = isSelected,
                    onClick = onClick,
                ),
        colors =
            CardDefaults.cardColors(
                containerColor = if (isSelected) Color(0xFF3D99F5) else Color(0xFFE0E0E0),
            ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = count.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color(0xFF61758A),
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
            )
        }
    }
}

@Composable
fun AnimatedSingleDice(
    isRolling: Boolean,
    result: Int,
    showResult: Boolean,
    modifier: Modifier = Modifier,
) {
    val rotation by animateFloatAsState(
        targetValue = if (isRolling) 1800f else 0f, // 5 full rotations
        animationSpec =
            tween(
                durationMillis = if (isRolling) 2000 else 0,
                easing = LinearEasing,
            ),
        label = "rotation",
    )

    val scale by animateFloatAsState(
        targetValue = if (isRolling) 1.1f else 1f,
        animationSpec = tween(300),
        label = "scale",
    )

    Box(
        modifier =
            modifier
                .graphicsLayer {
                    rotationX = rotation * 0.5f
                    rotationY = rotation * 0.3f
                    rotationZ = rotation
                    scaleX = scale
                    scaleY = scale
                },
        contentAlignment = Alignment.Center,
    ) {
        // Dice background
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        color =
                            if (showResult) {
                                Color(0xFF4CAF50)
                            } else if (isRolling) {
                                Color(0xFF3D99F5)
                            } else {
                                Color(0xFFE0E0E0)
                            },
                        shape = RoundedCornerShape(24.dp),
                    ),
            contentAlignment = Alignment.Center,
        ) {
            if (showResult) {
                Text(
                    text = getDiceEmoji(result),
                    fontSize = 72.sp,
                    textAlign = TextAlign.Center,
                )
            } else if (isRolling) {
                // Show cycling dice during animation
                val infiniteTransition = rememberInfiniteTransition(label = "diceCycle")
                val cycleValue by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 7f,
                    animationSpec =
                        infiniteRepeatable(
                            animation = tween(150, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart,
                        ),
                    label = "cycleValue",
                )

                Text(
                    text = getDiceEmoji(cycleValue.toInt()),
                    fontSize = 72.sp,
                    textAlign = TextAlign.Center,
                )
            } else {
                Text(
                    text = "🎲",
                    fontSize = 72.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun AnimatedMultipleDice(
    numberOfDice: Int,
    isRolling: Boolean,
    results: List<Int>,
    showResults: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(numberOfDice) { index ->
            AnimatedSingleDice(
                isRolling = isRolling,
                result = results.getOrNull(index) ?: 1,
                showResult = showResults,
                modifier = Modifier.size(120.dp),
            )
        }
    }
}

@Composable
fun RollButton(isRolling: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "rollAnimation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isRolling) 360f else 0f,
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
        if (isRolling) {
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
            text = if (isRolling) "Rolling..." else "Roll Dice",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
        )
    }
}

@Composable
fun ResultCard(
    diceResults: List<Int>,
    numberOfDice: Int,
) {
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
                text = "🎲 Dice Rolled! 🎲",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (numberOfDice == 1) {
                Text(
                    text = "Result: ${diceResults.firstOrNull() ?: 0}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                )
            } else {
                Text(
                    text = "Results: ${diceResults.joinToString(", ")}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Total: ${diceResults.sum()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                )
            }
        }
    }
}

private fun getDiceEmoji(value: Int): String =
    when (value) {
        1 -> "⚀"
        2 -> "⚁"
        3 -> "⚂"
        4 -> "⚃"
        5 -> "⚄"
        6 -> "⚅"
        else -> "🎲"
    } 
