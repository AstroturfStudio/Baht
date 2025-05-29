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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.semantics.Role
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

enum class NumberType {
    INTEGER,
    DECIMAL,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberGeneratorScreen(onBackClick: () -> Unit) {
    var minValue by remember { mutableStateOf("1") }
    var maxValue by remember { mutableStateOf("100") }
    var quantity by remember { mutableStateOf("1") }
    var numberType by remember { mutableStateOf(NumberType.INTEGER) }
    var isGenerating by remember { mutableStateOf(false) }
    var generatedNumbers by remember { mutableStateOf(listOf<String>()) }
    var showResult by remember { mutableStateOf(false) }
    var animatingNumbers by remember { mutableStateOf(listOf<String>()) }

    val scrollState = rememberScrollState()

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
                    .verticalScroll(scrollState)
                    .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // Number Type Selection
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    Text(
                        text = "Number Type",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        color = Color(0xFF121417),
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        Row(
                            modifier =
                                Modifier
                                    .selectable(
                                        selected = numberType == NumberType.INTEGER,
                                        onClick = { numberType = NumberType.INTEGER },
                                        role = Role.RadioButton,
                                    ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = numberType == NumberType.INTEGER,
                                onClick = { numberType = NumberType.INTEGER },
                                colors =
                                    RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFF3D99F5),
                                    ),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Integer",
                                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                fontWeight = FontWeight.Medium,
                            )
                        }

                        Row(
                            modifier =
                                Modifier
                                    .selectable(
                                        selected = numberType == NumberType.DECIMAL,
                                        onClick = { numberType = NumberType.DECIMAL },
                                        role = Role.RadioButton,
                                    ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = numberType == NumberType.DECIMAL,
                                onClick = { numberType = NumberType.DECIMAL },
                                colors =
                                    RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFF3D99F5),
                                    ),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Decimal",
                                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }

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
                        text = "Set Range & Quantity",
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
                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType =
                                        if (numberType == NumberType.DECIMAL) {
                                            KeyboardType.Decimal
                                        } else {
                                            KeyboardType.Number
                                        },
                                ),
                            modifier = Modifier.weight(1f),
                            enabled = !isGenerating,
                        )

                        OutlinedTextField(
                            value = maxValue,
                            onValueChange = { maxValue = it },
                            label = { Text("Max") },
                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType =
                                        if (numberType == NumberType.DECIMAL) {
                                            KeyboardType.Decimal
                                        } else {
                                            KeyboardType.Number
                                        },
                                ),
                            modifier = Modifier.weight(1f),
                            enabled = !isGenerating,
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isGenerating,
                    )
                }
            }

            // Number Display (only show for single number or during generation)
            if (generatedNumbers.size <= 1) {
                AnimatedNumberDisplay(
                    isGenerating = isGenerating,
                    displayNumber = if (generatedNumbers.isNotEmpty()) generatedNumbers.first() else "",
                    showResult = showResult,
                    animatingNumbers = animatingNumbers,
                    modifier = Modifier.size(180.dp),
                )
            }

            // Result Card
            AnimatedVisibility(
                visible = showResult,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                ResultCard(generatedNumbers = generatedNumbers)
            }

            // Generate Button
            Button(
                onClick = {
                    if (!isGenerating && isValidInput(minValue, maxValue, quantity, numberType)) {
                        isGenerating = true
                        showResult = false
                        animatingNumbers = generateAnimationNumbers(minValue, maxValue, numberType)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isGenerating && isValidInput(minValue, maxValue, quantity, numberType),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3D99F5),
                    ),
                shape = RoundedCornerShape(12.dp),
            ) {
                GenerateButton(isGenerating = isGenerating)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    LaunchedEffect(isGenerating) {
        if (isGenerating) {
            delay(2000) // Animation duration
            generatedNumbers = generateNumbers(minValue, maxValue, quantity, numberType)
            isGenerating = false
            showResult = true
        }
    }
}

@Composable
fun AnimatedNumberDisplay(
    isGenerating: Boolean,
    displayNumber: String,
    showResult: Boolean,
    animatingNumbers: List<String>,
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
                    rotationY = rotation * 0.5f
                },
        contentAlignment = Alignment.Center,
    ) {
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
                        text = displayNumber,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        textAlign = TextAlign.Center,
                    )
                }
                isGenerating && animatingNumbers.isNotEmpty() -> {
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
                        text = animatingNumbers[cycleIndex.toInt() % animatingNumbers.size],
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        textAlign = TextAlign.Center,
                    )
                }
                else -> {
                    Text(
                        text = "?",
                        fontSize = 64.sp,
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
            text = if (isGenerating) "Generating..." else "Generate Numbers",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
        )
    }
}

@Composable
fun ResultCard(generatedNumbers: List<String>) {
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
                    scaleX = 1f + (glow - 0.8f) * 0.1f
                    scaleY = 1f + (glow - 0.8f) * 0.1f
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
                text = if (generatedNumbers.size == 1) "🎲 Your Number! 🎲" else "🎲 Your Numbers! 🎲",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (generatedNumbers.size == 1) {
                Text(
                    text = "Generated: ${generatedNumbers.first()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(generatedNumbers.chunked(3)) { rowNumbers ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        ) {
                            rowNumbers.forEach { number ->
                                Card(
                                    colors =
                                        CardDefaults.cardColors(
                                            containerColor = Color.White.copy(alpha = 0.2f),
                                        ),
                                    shape = RoundedCornerShape(8.dp),
                                ) {
                                    Text(
                                        text = number,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                        modifier = Modifier.padding(8.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun isValidInput(
    minValue: String,
    maxValue: String,
    quantity: String,
    numberType: NumberType,
): Boolean {
    val qty = quantity.toIntOrNull()
    return when (numberType) {
        NumberType.INTEGER -> {
            val min = minValue.toIntOrNull()
            val max = maxValue.toIntOrNull()
            min != null && max != null && min <= max && qty != null && qty > 0 && qty <= 50
        }
        NumberType.DECIMAL -> {
            val min = minValue.toDoubleOrNull()
            val max = maxValue.toDoubleOrNull()
            min != null && max != null && min <= max && qty != null && qty > 0 && qty <= 50
        }
    }
}

private fun generateNumbers(
    minValue: String,
    maxValue: String,
    quantity: String,
    numberType: NumberType,
): List<String> {
    val qty = quantity.toIntOrNull() ?: 1
    return when (numberType) {
        NumberType.INTEGER -> {
            val min = minValue.toIntOrNull() ?: 1
            val max = maxValue.toIntOrNull() ?: 100
            (1..qty).map { Random.nextInt(min, max + 1).toString() }
        }
        NumberType.DECIMAL -> {
            val min = minValue.toDoubleOrNull() ?: 1.0
            val max = maxValue.toDoubleOrNull() ?: 100.0
            (1..qty).map {
                val randomDouble = min + Random.nextDouble() * (max - min)
                String.format("%.2f", randomDouble)
            }
        }
    }
}

private fun generateAnimationNumbers(
    minValue: String,
    maxValue: String,
    numberType: NumberType,
): List<String> =
    when (numberType) {
        NumberType.INTEGER -> {
            val min = minValue.toIntOrNull() ?: 1
            val max = maxValue.toIntOrNull() ?: 100
            (1..10).map { Random.nextInt(min, max + 1).toString() }
        }
        NumberType.DECIMAL -> {
            val min = minValue.toDoubleOrNull() ?: 1.0
            val max = maxValue.toDoubleOrNull() ?: 100.0
            (1..10).map {
                val randomDouble = min + Random.nextDouble() * (max - min)
                String.format("%.2f", randomDouble)
            }
        }
    } 
