package studio.astroturf.baht.ui.listShuffler

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.astroturf.baht.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListShufflerScreen(onBackClick: () -> Unit) {
    var items by remember { mutableStateOf(listOf<String>()) }
    var newItemName by remember { mutableStateOf("") }
    var shuffledItems by remember { mutableStateOf(listOf<String>()) }
    var isAddingItem by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableIntStateOf(-1) }
    var editingText by remember { mutableStateOf("") }
    var showShuffledList by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "List Shuffler",
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
                Icon(Icons.Filled.Add, contentDescription = "Add Item")
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
            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                    CardDefaults.cardColors(
                        containerColor = Color(0xFFF0F8FF),
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Filled.Shuffle,
                        contentDescription = "Shuffle",
                        tint = Color(0xFF3D99F5),
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Add items to your list and shuffle their positions randomly",
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        fontSize = 14.sp,
                        color = Color(0xFF2E5BBA),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add new item input
            AnimatedVisibility(
                visible = isAddingItem,
                enter = expandVertically(animationSpec = spring()) + fadeIn(),
                exit = shrinkVertically(animationSpec = spring()) + fadeOut(),
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = Color(0xFFF8F9FA),
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        OutlinedTextField(
                            value = newItemName,
                            onValueChange = { newItemName = it },
                            label = {
                                Text(
                                    "Item Name",
                                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                )
                            },
                            keyboardOptions =
                                KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                ),
                            keyboardActions =
                                KeyboardActions(
                                    onDone = {
                                        if (newItemName.isNotBlank()) {
                                            items = items + newItemName.trim()
                                            newItemName = ""
                                            isAddingItem = false
                                            showShuffledList = false
                                            keyboardController?.hide()
                                        }
                                    },
                                ),
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF3D99F5),
                                    focusedLabelColor = Color(0xFF3D99F5),
                                ),
                            singleLine = true,
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            Button(
                                onClick = {
                                    newItemName = ""
                                    isAddingItem = false
                                    keyboardController?.hide()
                                },
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFE3F2FD),
                                        contentColor = Color(0xFF1976D2),
                                    ),
                            ) {
                                Text(
                                    "Cancel",
                                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    if (newItemName.isNotBlank()) {
                                        items = items + newItemName.trim()
                                        newItemName = ""
                                        isAddingItem = false
                                        showShuffledList = false
                                        keyboardController?.hide()
                                    }
                                },
                                enabled = newItemName.isNotBlank(),
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF3D99F5),
                                    ),
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

            Spacer(modifier = Modifier.height(16.dp))

            // Original Items List
            if (items.isNotEmpty()) {
                Text(
                    text = "Original List (${items.size} items)",
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color(0xFF2C3E50),
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(items) { index, item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                CardDefaults.cardColors(
                                    containerColor = Color(0xFFF8F9FA),
                                ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        ) {
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(
                                    modifier =
                                        Modifier
                                            .size(24.dp)
                                            .background(
                                                Color(0xFF3D99F5),
                                                CircleShape,
                                            ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = (index + 1).toString(),
                                        color = Color.White,
                                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp,
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                if (editingIndex == index) {
                                    OutlinedTextField(
                                        value = editingText,
                                        onValueChange = { editingText = it },
                                        keyboardOptions =
                                            KeyboardOptions(
                                                imeAction = ImeAction.Done,
                                            ),
                                        keyboardActions =
                                            KeyboardActions(
                                                onDone = {
                                                    if (editingText.isNotBlank()) {
                                                        items =
                                                            items.toMutableList().apply {
                                                                set(index, editingText.trim())
                                                            }
                                                        editingIndex = -1
                                                        editingText = ""
                                                        showShuffledList = false
                                                        keyboardController?.hide()
                                                    }
                                                },
                                            ),
                                        modifier = Modifier.weight(1f),
                                        colors =
                                            OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = Color(0xFF3D99F5),
                                            ),
                                        singleLine = true,
                                    )
                                } else {
                                    Text(
                                        text = item,
                                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                        modifier = Modifier.weight(1f),
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        if (editingIndex == index) {
                                            if (editingText.isNotBlank()) {
                                                items =
                                                    items.toMutableList().apply {
                                                        set(index, editingText.trim())
                                                    }
                                                editingIndex = -1
                                                editingText = ""
                                                showShuffledList = false
                                                keyboardController?.hide()
                                            }
                                        } else {
                                            editingIndex = index
                                            editingText = item
                                        }
                                    },
                                ) {
                                    Icon(
                                        Icons.Filled.Edit,
                                        contentDescription = "Edit",
                                        tint = Color(0xFF3D99F5),
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        items = items.toMutableList().apply { removeAt(index) }
                                        if (editingIndex == index) {
                                            editingIndex = -1
                                            editingText = ""
                                        }
                                        showShuffledList = false
                                    },
                                ) {
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

                Spacer(modifier = Modifier.height(16.dp))

                // Shuffle Button
                Button(
                    onClick = {
                        shuffledItems = items.shuffled()
                        showShuffledList = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                        ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = items.size >= 2,
                ) {
                    Icon(
                        Icons.Filled.Shuffle,
                        contentDescription = "Shuffle",
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Shuffle List",
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                    )
                }

                if (items.size < 2) {
                    Text(
                        text = "Add at least 2 items to shuffle",
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        fontSize = 12.sp,
                        color = Color(0xFF666666),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Shuffled Results
                AnimatedVisibility(
                    visible = showShuffledList && shuffledItems.isNotEmpty(),
                    enter = expandVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
                    exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300)),
                ) {
                    Column {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                CardDefaults.cardColors(
                                    containerColor = Color(0xFFF1F8E9),
                                ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        Icons.Filled.Shuffle,
                                        contentDescription = "Shuffled",
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(24.dp),
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Shuffled List",
                                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color(0xFF2E7D32),
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                shuffledItems.forEachIndexed { index, item ->
                                    Row(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Box(
                                            modifier =
                                                Modifier
                                                    .size(28.dp)
                                                    .background(
                                                        Color(0xFF4CAF50),
                                                        CircleShape,
                                                    ),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Text(
                                                text = (index + 1).toString(),
                                                color = Color.White,
                                                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(16.dp))

                                        Text(
                                            text = item,
                                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 16.sp,
                                            color = Color(0xFF2E7D32),
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                shuffledItems = items.shuffled()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3D99F5),
                                ),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Icon(
                                Icons.Filled.Shuffle,
                                contentDescription = "Shuffle Again",
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Shuffle Again",
                                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                            )
                        }
                    }
                }
            } else {
                // Empty state
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                            CardDefaults.cardColors(
                                containerColor = Color(0xFFFAFAFA),
                            ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    ) {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                Icons.Filled.Shuffle,
                                contentDescription = "Empty",
                                tint = Color(0xFFBDBDBD),
                                modifier = Modifier.size(48.dp),
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No items yet",
                                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp,
                                color = Color(0xFF757575),
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Add items to your list to start shuffling",
                                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                fontSize = 14.sp,
                                color = Color(0xFF9E9E9E),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
} 
