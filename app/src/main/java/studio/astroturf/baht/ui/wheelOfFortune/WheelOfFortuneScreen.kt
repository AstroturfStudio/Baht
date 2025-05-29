package studio.astroturf.baht.ui.wheelOfFortune

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import studio.astroturf.baht.R
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelOfFortuneScreen(onBackClick: () -> Unit) {
    var items by remember { mutableStateOf(listOf<String>()) }
    var isSpinning by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<String?>(null) }
    var newItemName by remember { mutableStateOf("") }
    var isAddingItem by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableIntStateOf(-1) }
    var editingText by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Wheel of Fortune",
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
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(
                visible = isAddingItem,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
            ) {
                AddItemCard(
                    value = newItemName,
                    onValueChange = { newItemName = it },
                    onSave = {
                        if (newItemName.isNotBlank()) {
                            items = items + newItemName.trim()
                            newItemName = ""
                            isAddingItem = false
                            keyboardController?.hide()
                        }
                    },
                    onCancel = {
                        newItemName = ""
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
                            text = "No items added yet!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E40AF),
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add some options to spin the wheel",
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
                    WheelCanvas(
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
                    WinnerCard(winner = selectedItem ?: "")
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

                                val targetRotation = rotation.value + Random.nextFloat() * 360 + 1440
                                rotation.animateTo(
                                    targetValue = targetRotation,
                                    animationSpec =
                                        tween(
                                            durationMillis = 3000,
                                            easing = LinearEasing,
                                        ),
                                )

                                val normalizedRotation = (targetRotation % 360)
                                val segmentAngle = 360f / items.size
                                val selectedIndex = ((360 - normalizedRotation + segmentAngle / 2) / segmentAngle).toInt() % items.size
                                selectedItem = items[selectedIndex]
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

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn {
                            itemsIndexed(items) { index, item ->
                                ItemRow(
                                    item = item,
                                    isEditing = editingIndex == index,
                                    editingText = editingText,
                                    onEditingTextChange = { editingText = it },
                                    onEdit = {
                                        editingIndex = index
                                        editingText = item
                                    },
                                    onSaveEdit = {
                                        if (editingText.isNotBlank()) {
                                            items =
                                                items.toMutableList().apply {
                                                    set(index, editingText.trim())
                                                }
                                            editingIndex = -1
                                            editingText = ""
                                            keyboardController?.hide()
                                        }
                                    },
                                    onCancelEdit = {
                                        editingIndex = -1
                                        editingText = ""
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
                                            editingText = ""
                                        }
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddItemCard(
    value: String,
    onValueChange: (String) -> Unit,
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
                text = "Add New Item",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                modifier = Modifier.padding(bottom = 16.dp),
            )

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text("Item name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
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
fun ItemRow(
    item: String,
    isEditing: Boolean,
    editingText: String,
    onEditingTextChange: (String) -> Unit,
    onEdit: () -> Unit,
    onSaveEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onDelete: () -> Unit,
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
                    value = editingText,
                    onValueChange = onEditingTextChange,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onSaveEdit() }),
                    shape = RoundedCornerShape(8.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
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
                    Spacer(modifier = Modifier.width(8.dp))
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    color = Color(0xFF121417),
                    modifier = Modifier.weight(1f),
                )
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
fun WinnerCard(winner: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "🎉 WINNER! 🎉",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = winner,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
            )
        }
    }
}

@Composable
fun WheelCanvas(
    items: List<String>,
    rotation: Float,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier.rotate(rotation)) {
        if (items.isEmpty()) return@Canvas

        val center = size.center
        val radius = size.minDimension / 2f
        val segmentAngle = 360f / items.size

        items.forEachIndexed { index, item ->
            val startAngle = index * segmentAngle
            val color = getSegmentColor(index)

            // Draw segment
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = segmentAngle,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size =
                    androidx.compose.ui.geometry
                        .Size(radius * 2, radius * 2),
            )

            // Draw text
            val textAngle = startAngle + segmentAngle / 2
            val textRadius = radius * 0.7f
            val textX = center.x + textRadius * cos(Math.toRadians(textAngle.toDouble())).toFloat()
            val textY = center.y + textRadius * sin(Math.toRadians(textAngle.toDouble())).toFloat()

            rotate(degrees = textAngle + 90f, pivot = Offset(textX, textY)) {
                val textLayoutResult =
                    textMeasurer.measure(
                        text = item,
                        style =
                            TextStyle(
                                fontSize = 12.sp,
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

private fun getSegmentColor(index: Int): Color {
    val colors =
        listOf(
            Color(0xFFE53E3E),
            Color(0xFF3182CE),
            Color(0xFF38A169),
            Color(0xFFD69E2E),
            Color(0xFF805AD5),
            Color(0xFFDD6B20),
            Color(0xFFE53E3E),
            Color(0xFF319795),
        )
    return colors[index % colors.size]
} 
