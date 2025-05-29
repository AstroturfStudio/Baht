package studio.astroturf.baht.ui.weightedWheelOfFortune

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import studio.astroturf.baht.R
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class WeightedItem(
    val name: String,
    val contribution: Double,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightedWheelOfFortuneScreen(onBackClick: () -> Unit) {
    var items by remember { mutableStateOf(listOf<WeightedItem>()) }
    var isSpinning by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<WeightedItem?>(null) }
    var newItemName by remember { mutableStateOf("") }
    var newItemContribution by remember { mutableStateOf("10.0") }
    var isAddingItem by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableIntStateOf(-1) }
    var editingName by remember { mutableStateOf("") }
    var editingContribution by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Weighted Wheel",
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isAddingItem = true },
                containerColor = Color(0xFF3D99F5),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Explanation
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    text = "Items with higher contributions have a better chance of being selected. Contributions are proportional to probability.",
                    fontSize = 14.sp,
                    color = Color(0xFF1E40AF),
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    modifier = Modifier.padding(12.dp),
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = isAddingItem,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
            ) {
                AddWeightedItemCard(
                    name = newItemName,
                    contribution = newItemContribution,
                    onNameChange = { newItemName = it },
                    onContributionChange = { newItemContribution = it },
                    onSave = {
                        val contributionValue = newItemContribution.toDoubleOrNull()
                        if (newItemName.isNotBlank() && contributionValue != null && contributionValue > 0) {
                            items = items + WeightedItem(newItemName.trim(), contributionValue)
                            newItemName = ""
                            newItemContribution = "10.0"
                            isAddingItem = false
                            keyboardController?.hide()
                        }
                    },
                    onCancel = {
                        newItemName = ""
                        newItemContribution = "10.0"
                        isAddingItem = false
                        keyboardController?.hide()
                    },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (items.isEmpty()) {
                // Empty state
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "No weighted items added yet!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E40AF),
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add items with different contributions to spin the wheel",
                            fontSize = 14.sp,
                            color = Color(0xFF1E40AF),
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { isAddingItem = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D99F5)),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Add First Item",
                                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                            )
                        }
                    }
                }
            } else {
                // Wheel
                Box(
                    modifier = Modifier.size(300.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    WeightedWheelCanvas(
                        items = items,
                        rotation = rotation.value,
                        modifier = Modifier.fillMaxSize(),
                    )

                    // Pointer
                    Canvas(
                        modifier =
                            Modifier
                                .size(20.dp)
                                .align(Alignment.TopCenter),
                    ) {
                        val trianglePath =
                            androidx.compose.ui.graphics.Path().apply {
                                moveTo(size.width / 2f, size.height)
                                lineTo(0f, 0f)
                                lineTo(size.width, 0f)
                                close()
                            }
                        drawPath(trianglePath, Color.Red)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Result Display
                AnimatedVisibility(
                    visible = showResult && selectedItem != null,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut(),
                ) {
                    selectedItem?.let { winner ->
                        WinnerCard(
                            winner = winner,
                            totalContribution = items.sumOf { it.contribution },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Spin Button
                Button(
                    onClick = {
                        if (!isSpinning && items.isNotEmpty()) {
                            scope.launch {
                                isSpinning = true
                                showResult = false
                                selectedItem = null

                                // Ensure minimum 2 full rotations + random extra
                                val minRotations = 720f // 2 full rotations minimum
                                val extraRotation = Random.nextFloat() * 720f // 0-2 additional rotations
                                val baseRotation = minRotations + extraRotation

                                // First select the weighted winner
                                val winner = selectWeightedItem(items)
                                if (winner == null) {
                                    isSpinning = false
                                    return@launch
                                }
                                val winnerIndex = items.indexOf(winner)

                                // Calculate segment angles and positions based on contribution
                                val totalContribution = items.sumOf { it.contribution }.toFloat()
                                var currentAngle = 0f
                                var winnerSegmentCenter = 0f

                                items.forEachIndexed { index, item ->
                                    val segmentAngle = (item.contribution.toFloat() / totalContribution) * 360f
                                    if (index == winnerIndex) {
                                        winnerSegmentCenter = currentAngle + segmentAngle / 2
                                    }
                                    currentAngle += segmentAngle
                                }

                                // The pointer is at the top (270° in wheel coordinates)
                                // To land the winner under the pointer, we need to rotate so that
                                // the winner segment center aligns with 270°
                                val targetAngle = 270f - winnerSegmentCenter
                                val targetRotation = baseRotation + targetAngle

                                // Dynamic duration based on total rotation
                                val totalRotationAmount = Math.abs(targetRotation - rotation.value)
                                val duration = (2000 + (totalRotationAmount / 360f) * 500).toInt().coerceIn(2000, 5000)

                                rotation.animateTo(
                                    targetValue = targetRotation,
                                    animationSpec =
                                        tween(
                                            durationMillis = duration,
                                            easing = LinearEasing,
                                        ),
                                )

                                selectedItem = winner
                                showResult = true
                                isSpinning = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSpinning && items.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D99F5)),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        text = if (isSpinning) "Spinning..." else "Spin!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        modifier = Modifier.padding(8.dp),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Items List
            if (items.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Items (${items.size})",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                            )
                            Row {
                                Text(
                                    text = "Total: ${items.sumOf { it.contribution }.toInt()}",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                    modifier = Modifier.padding(end = 8.dp),
                                )
                                TextButton(
                                    onClick = {
                                        items = emptyList()
                                        showResult = false
                                        selectedItem = null
                                    },
                                ) {
                                    Text(
                                        "Clear All",
                                        color = Color.Red,
                                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items.forEachIndexed { index, item ->
                                WeightedItemRow(
                                    item = item,
                                    isEditing = editingIndex == index,
                                    editingName = editingName,
                                    editingContribution = editingContribution,
                                    onEditingNameChange = { editingName = it },
                                    onEditingContributionChange = { editingContribution = it },
                                    onEdit = {
                                        editingIndex = index
                                        editingName = item.name
                                        editingContribution = item.contribution.toString()
                                    },
                                    onSaveEdit = {
                                        val contribution = editingContribution.toDoubleOrNull()
                                        if (editingName.isNotBlank() && contribution != null && contribution > 0) {
                                            items =
                                                items.toMutableList().apply {
                                                    set(index, WeightedItem(editingName.trim(), contribution))
                                                }
                                            editingIndex = -1
                                            editingName = ""
                                            editingContribution = ""
                                            keyboardController?.hide()
                                        }
                                    },
                                    onCancelEdit = {
                                        editingIndex = -1
                                        editingName = ""
                                        editingContribution = ""
                                        keyboardController?.hide()
                                    },
                                    onDelete = {
                                        items = items.toMutableList().apply { removeAt(index) }
                                        if (items.isEmpty()) {
                                            showResult = false
                                            selectedItem = null
                                        }
                                        if (editingIndex == index) {
                                            editingIndex = -1
                                            editingName = ""
                                            editingContribution = ""
                                        }
                                    },
                                    totalContribution = items.sumOf { it.contribution },
                                )
                            }
                        }
                    }
                }
            }

            // Add bottom padding for FAB
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun AddWeightedItemCard(
    name: String,
    contribution: String,
    onNameChange: (String) -> Unit,
    onContributionChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = Color(0xFFF8F9FA),
            ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            Text(
                text = "Add Weighted Item",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                modifier = Modifier.padding(bottom = 16.dp),
            )

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Item name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape = RoundedCornerShape(8.dp),
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = contribution,
                onValueChange = onContributionChange,
                label = { Text("Contribution Amount") },
                supportingText = { Text("Higher amounts = better chances") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions = KeyboardActions(onDone = { onSave() }),
                shape = RoundedCornerShape(8.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE0E0E0),
                        ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        "Cancel",
                        color = Color(0xFF61758A),
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    )
                }

                Button(
                    onClick = onSave,
                    modifier = Modifier.weight(1f),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3D99F5),
                        ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        "Add",
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    )
                }
            }
        }
    }
}

@Composable
fun WeightedItemRow(
    item: WeightedItem,
    isEditing: Boolean,
    editingName: String,
    editingContribution: String,
    onEditingNameChange: (String) -> Unit,
    onEditingContributionChange: (String) -> Unit,
    onEdit: () -> Unit,
    onSaveEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onDelete: () -> Unit,
    totalContribution: Double = 0.0,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isEditing) Color(0xFFF0F8FF) else Color.White,
        animationSpec = tween(300),
        label = "backgroundColor",
    )

    val scale by animateFloatAsState(
        targetValue = if (isEditing) 1.02f else 1f,
        animationSpec = tween(300),
        label = "scale",
    )

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .scale(scale),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = if (isEditing) 4.dp else 1.dp,
            ),
        shape = RoundedCornerShape(8.dp),
    ) {
        if (isEditing) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = editingName,
                    onValueChange = onEditingNameChange,
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editingContribution,
                    onValueChange = onEditingContributionChange,
                    label = { Text("Contribution") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(8.dp),
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(
                        onClick = onCancelEdit,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE0E0E0),
                            ),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text(
                            "Cancel",
                            color = Color(0xFF61758A),
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        )
                    }

                    Button(
                        onClick = onSaveEdit,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50),
                            ),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text(
                            "Save",
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        )
                    }
                }
            }
        } else {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        color = Color(0xFF121417),
                    )

                    val probability =
                        if (totalContribution > 0) {
                            (item.contribution / totalContribution * 100)
                        } else {
                            0.0
                        }

                    Text(
                        text = "Contribution: ${item.contribution}",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        color = Color(0xFF61758A),
                    )

                    Text(
                        text = "Probability: ${String.format("%.1f", probability)}%",
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        color = Color(0xFF3D99F5),
                        fontWeight = FontWeight.Medium,
                    )
                }

                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color(0xFF3D99F5),
                        )
                    }

                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFE57373),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WinnerCard(
    winner: WeightedItem,
    totalContribution: Double = 0.0,
) {
    val probability =
        if (totalContribution > 0) {
            (winner.contribution / totalContribution * 100)
        } else {
            0.0
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
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
                text = "🏆 WINNER! 🏆",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = winner.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Contribution: ${winner.contribution}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f),
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                textAlign = TextAlign.Center,
            )

            Text(
                text = "Had ${String.format("%.1f", probability)}% chance",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.8f),
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun WeightedWheelCanvas(
    items: List<WeightedItem>,
    rotation: Float,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier.rotate(rotation)) {
        if (items.isEmpty()) return@Canvas

        val center = size.center
        val radius = size.minDimension / 2f
        val totalContribution = items.sumOf { it.contribution }.toFloat()

        var currentAngle = 0f

        items.forEachIndexed { index, item ->
            val segmentAngle = (item.contribution.toFloat() / totalContribution) * 360f
            val color = getSegmentColor(index)

            // Draw segment
            drawArc(
                color = color,
                startAngle = currentAngle,
                sweepAngle = segmentAngle,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size =
                    androidx.compose.ui.geometry
                        .Size(radius * 2, radius * 2),
            )

            // Draw text if segment is large enough
            if (segmentAngle > 20f) {
                val textAngle = currentAngle + segmentAngle / 2
                val textRadius = radius * 0.7f
                val textX = center.x + textRadius * cos(Math.toRadians(textAngle.toDouble())).toFloat()
                val textY = center.y + textRadius * sin(Math.toRadians(textAngle.toDouble())).toFloat()

                rotate(degrees = textAngle + 90f, pivot = Offset(textX, textY)) {
                    val textLayoutResult =
                        textMeasurer.measure(
                            text = item.name,
                            style =
                                TextStyle(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                ),
                        )

                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft =
                            Offset(
                                textX - textLayoutResult.size.width / 2f,
                                textY - textLayoutResult.size.height / 2f,
                            ),
                    )
                }
            }

            currentAngle += segmentAngle
        }

        // Draw border
        drawCircle(
            color = Color.Black,
            radius = radius,
            center = center,
            style =
                androidx.compose.ui.graphics.drawscope
                    .Stroke(width = 4.dp.toPx()),
        )
    }
}

private fun selectWeightedItem(items: List<WeightedItem>): WeightedItem? {
    if (items.isEmpty()) return null

    val totalContribution = items.sumOf { it.contribution }
    val randomValue = Random.nextDouble(totalContribution)

    var currentContribution = 0.0
    for (item in items) {
        currentContribution += item.contribution
        if (randomValue <= currentContribution) {
            return item
        }
    }

    return items.last()
}

private fun getSegmentColor(index: Int): Color {
    val colors =
        listOf(
            Color(0xFFE53E3E),
            Color(0xFF3182CE),
            Color(0xFF38A169),
            Color(0xFFD69E2E),
            Color(0xFF805AD5),
            Color(0xFFDD6B20),
            Color(0xFF319795),
            Color(0xFFE53E3E),
        )
    return colors[index % colors.size]
}
