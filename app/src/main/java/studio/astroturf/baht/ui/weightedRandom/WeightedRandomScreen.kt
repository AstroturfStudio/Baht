package studio.astroturf.baht.ui.weightedRandom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import studio.astroturf.baht.R
import kotlin.random.Random

data class WeightedCandidate(
    val name: String,
    val contribution: Double,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightedRandomScreen(onBackClick: () -> Unit) {
    var candidates by remember { mutableStateOf(listOf<WeightedCandidate>()) }
    var newCandidateName by remember { mutableStateOf("") }
    var newCandidateContribution by remember { mutableStateOf("10.0") }
    var isAddingCandidate by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableIntStateOf(-1) }
    var editingName by remember { mutableStateOf("") }
    var editingContribution by remember { mutableStateOf("") }
    var isDrawing by remember { mutableStateOf(false) }
    var winner by remember { mutableStateOf<WeightedCandidate?>(null) }
    var showWinner by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Weighted Random",
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
                onClick = { isAddingCandidate = true },
                containerColor = Color(0xFF3D99F5),
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Candidate")
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
                visible = isAddingCandidate,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
            ) {
                AddCandidateCard(
                    name = newCandidateName,
                    weight = newCandidateContribution,
                    onNameChange = { newCandidateName = it },
                    onWeightChange = { newCandidateContribution = it },
                    onSave = {
                        val weight = newCandidateContribution.toDoubleOrNull()
                        if (newCandidateName.isNotBlank() && weight != null && weight > 0) {
                            candidates = candidates + WeightedCandidate(newCandidateName, weight)
                            newCandidateName = ""
                            newCandidateContribution = "10.0"
                            isAddingCandidate = false
                            keyboardController?.hide()
                        }
                    },
                    onCancel = {
                        newCandidateName = ""
                        newCandidateContribution = "10.0"
                        isAddingCandidate = false
                        keyboardController?.hide()
                    },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (candidates.isNotEmpty()) {
                Button(
                    onClick = {
                        if (!isDrawing && candidates.isNotEmpty()) {
                            isDrawing = true
                            winner = null
                            showWinner = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isDrawing && candidates.isNotEmpty(),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3D99F5),
                        ),
                    shape = RoundedCornerShape(12.dp),
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
                WinnerCard(winner = winner!!, totalContribution = candidates.sumOf { it.contribution })
            }

            if (showWinner) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (candidates.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    candidates.forEachIndexed { index, candidate ->
                        CandidateItem(
                            candidate = candidate,
                            isEditing = editingIndex == index,
                            editingName = editingName,
                            editingContribution = editingContribution,
                            onEditingNameChange = { editingName = it },
                            onEditingContributionChange = { editingContribution = it },
                            onEdit = {
                                editingIndex = index
                                editingName = candidate.name
                                editingContribution = candidate.contribution.toString()
                            },
                            onSaveEdit = {
                                val newContribution = editingContribution.toDoubleOrNull()
                                if (editingName.isNotBlank() && newContribution != null && newContribution > 0) {
                                    candidates =
                                        candidates.toMutableList().apply {
                                            this[index] = WeightedCandidate(editingName, newContribution)
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
                                candidates =
                                    candidates.toMutableList().apply {
                                        removeAt(index)
                                    }
                            },
                            totalContribution = candidates.sumOf { it.contribution },
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "No candidates yet!\nTap + to add some",
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
        if (isDrawing) {
            delay(2000) // Animation duration
            winner = selectWeightedRandomCandidate(candidates)
            isDrawing = false
            showWinner = true
        }
    }
}

@Composable
fun AddCandidateCard(
    name: String,
    weight: String,
    onNameChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
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
                text = "Add New Candidate",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                modifier = Modifier.padding(bottom = 16.dp),
            )

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Candidate Name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape = RoundedCornerShape(8.dp),
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = weight,
                onValueChange = onWeightChange,
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
fun CandidateItem(
    candidate: WeightedCandidate,
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = if (isEditing) Color(0xFFF0F8FF) else Color.White,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        if (isEditing) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
            ) {
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
                        text = candidate.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        color = Color(0xFF121417),
                    )

                    val probability =
                        if (totalContribution > 0) {
                            (candidate.contribution / totalContribution * 100)
                        } else {
                            0.0
                        }

                    Text(
                        text = "Contribution: ${candidate.contribution}",
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
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = Color(0xFF3D99F5),
                        )
                    }

                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFE74C3C),
                        )
                    }
                }
            }
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
        modifier = Modifier.padding(16.dp),
    ) {
        if (isDrawing) {
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
            text = if (isDrawing) "Drawing..." else "Draw Winner",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
        )
    }
}

@Composable
fun WinnerCard(
    winner: WeightedCandidate,
    totalContribution: Double = 0.0,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "winnerGlow")
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

    val probability =
        if (totalContribution > 0) {
            (winner.contribution / totalContribution * 100)
        } else {
            0.0
        }

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

private fun selectWeightedRandomCandidate(candidates: List<WeightedCandidate>): WeightedCandidate? {
    if (candidates.isEmpty()) return null

    val totalWeight = candidates.sumOf { it.contribution }
    val randomValue = Random.nextDouble(0.0, totalWeight)

    var currentWeight = 0.0
    for (candidate in candidates) {
        currentWeight += candidate.contribution
        if (randomValue <= currentWeight) {
            return candidate
        }
    }

    return candidates.last() // Fallback, should never happen
}
