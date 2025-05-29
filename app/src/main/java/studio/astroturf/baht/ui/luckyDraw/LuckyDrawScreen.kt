package studio.astroturf.baht.ui.luckyDraw

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import studio.astroturf.baht.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuckyDrawScreen(onBackClick: () -> Unit) {
    var entrants by remember { mutableStateOf(listOf<String>()) }
    var newEntrantName by remember { mutableStateOf("") }
    var isAddingEntrant by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableIntStateOf(-1) }
    var editingText by remember { mutableStateOf("") }
    var isDrawing by remember { mutableStateOf(false) }
    var winner by remember { mutableStateOf<String?>(null) }
    var showWinner by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Lucky Draw",
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
                onClick = { isAddingEntrant = true },
                containerColor = Color(0xFF3D99F5),
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Entrant")
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
        ) {
            AnimatedVisibility(
                visible = isAddingEntrant,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
            ) {
                AddEntrantCard(
                    value = newEntrantName,
                    onValueChange = { newEntrantName = it },
                    onSave = {
                        if (newEntrantName.isNotBlank()) {
                            entrants = entrants + newEntrantName
                            newEntrantName = ""
                            isAddingEntrant = false
                            keyboardController?.hide()
                        }
                    },
                    onCancel = {
                        newEntrantName = ""
                        isAddingEntrant = false
                        keyboardController?.hide()
                    },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (entrants.isNotEmpty()) {
                Button(
                    onClick = {
                        if (!isDrawing && entrants.isNotEmpty()) {
                            isDrawing = true
                            winner = null
                            showWinner = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isDrawing && entrants.isNotEmpty(),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3D99F5),
                        ),
                ) {
                    DrawButton(isDrawing = isDrawing)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            AnimatedVisibility(
                visible = showWinner && winner != null,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                WinnerCard(winner = winner ?: "")
            }

            if (showWinner) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (entrants.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    entrants.forEachIndexed { index, entrant ->
                        EntrantItem(
                            entrant = entrant,
                            isEditing = editingIndex == index,
                            editingText = editingText,
                            onEditingTextChange = { editingText = it },
                            onEdit = {
                                editingIndex = index
                                editingText = entrants[index]
                            },
                            onSaveEdit = {
                                entrants =
                                    entrants.toMutableList().apply {
                                        this[index] = editingText
                                    }
                                editingIndex = -1
                                editingText = ""
                                keyboardController?.hide()
                            },
                            onCancelEdit = {
                                editingIndex = -1
                                editingText = ""
                                keyboardController?.hide()
                            },
                            onDelete = {
                                entrants =
                                    entrants.toMutableList().apply {
                                        removeAt(index)
                                    }
                            },
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "No entrants yet!\nTap + to add some",
                        textAlign = TextAlign.Center,
                        color = Color(0xFF61758A),
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    LaunchedEffect(isDrawing) {
        if (isDrawing && entrants.isNotEmpty()) {
            delay(2000) // Drawing animation duration
            winner = entrants.random()
            isDrawing = false
            showWinner = true
        }
    }
}

@Composable
fun DrawButton(isDrawing: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "drawAnimation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isDrawing) 360f else 0f,
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
    ) {
        Box(
            modifier =
                Modifier
                    .size(24.dp)
                    .rotate(rotation)
                    .background(
                        if (isDrawing) Color.White.copy(alpha = 0.3f) else Color.Transparent,
                        CircleShape,
                    ).border(
                        2.dp,
                        if (isDrawing) Color.White else Color.Transparent,
                        CircleShape,
                    ),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = if (isDrawing) "Drawing..." else "Draw Winner!",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
        )
    }
}

@Composable
fun WinnerCard(winner: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "winnerGlow")
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.7f,
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
                    scaleX = 1f + (glow - 0.7f) * 0.1f
                    scaleY = 1f + (glow - 0.7f) * 0.1f
                },
        colors =
            CardDefaults.cardColors(
                containerColor = Color(0xFF4CAF50),
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
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
fun AddEntrantCard(
    value: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text("Entrant Name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onSave() }),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Button(
                    onClick = onCancel,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFF61758A),
                        ),
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onSave,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3D99F5),
                        ),
                ) {
                    Text("Add")
                }
            }
        }
    }
}

@Composable
fun EntrantItem(
    entrant: String,
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
                .scale(scale),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = if (isEditing) 4.dp else 1.dp,
            ),
    ) {
        if (isEditing) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = editingText,
                    onValueChange = onEditingTextChange,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onSaveEdit() }),
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
                                containerColor = Color.Transparent,
                                contentColor = Color(0xFF61758A),
                            ),
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onSaveEdit,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF3D99F5),
                            ),
                    ) {
                        Text("Save")
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
                    text = entrant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF121417),
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    modifier = Modifier.weight(1f),
                )
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = Color(0xFF3D99F5),
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFE57373),
                        )
                    }
                }
            }
        }
    }
} 
