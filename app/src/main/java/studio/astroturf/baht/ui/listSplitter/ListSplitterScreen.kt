package studio.astroturf.baht.ui.listSplitter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.astroturf.baht.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSplitterScreen(onBackClick: () -> Unit) {
    var items by remember { mutableStateOf(listOf<String>()) }
    var newItemName by remember { mutableStateOf("") }
    var numberOfGroups by remember { mutableStateOf("2") }
    var isAddingItem by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableIntStateOf(-1) }
    var editingText by remember { mutableStateOf("") }
    var groupedItems by remember { mutableStateOf<List<List<String>>>(emptyList()) }
    var showGroups by remember { mutableStateOf(false) }
    var remainingItemsOption by remember { mutableStateOf(RemainingItemsOption.DISTRIBUTE) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "List Splitter",
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
            // Number of groups input
            OutlinedTextField(
                value = numberOfGroups,
                onValueChange = {
                    if (it.isEmpty() || (it.toIntOrNull() != null && it.toInt() > 0)) {
                        numberOfGroups = it
                        showGroups = false
                    }
                },
                label = {
                    Text(
                        "Number of Groups",
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    )
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                modifier = Modifier.fillMaxWidth(),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3D99F5),
                        focusedLabelColor = Color(0xFF3D99F5),
                    ),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Remaining items option
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                    CardDefaults.cardColors(
                        containerColor = Color(0xFFF8F9FA),
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = "When items don't divide evenly:",
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )

                    RemainingItemsOption.entries.forEach { option ->
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (remainingItemsOption == option),
                                        onClick = {
                                            remainingItemsOption = option
                                            showGroups = false
                                        },
                                    ).padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = (remainingItemsOption == option),
                                onClick = {
                                    remainingItemsOption = option
                                    showGroups = false
                                },
                                colors =
                                    RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFF3D99F5),
                                    ),
                            )
                            Text(
                                text = option.displayName,
                                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add item card
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
                            items = items + newItemName
                            newItemName = ""
                            isAddingItem = false
                            showGroups = false
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

            // Split button
            if (items.isNotEmpty() && numberOfGroups.isNotBlank()) {
                Button(
                    onClick = {
                        val numGroups = numberOfGroups.toIntOrNull() ?: 2
                        if (numGroups > 0 && items.isNotEmpty()) {
                            groupedItems = splitItemsIntoGroups(items, numGroups, remainingItemsOption)
                            showGroups = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3D99F5),
                        ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            Icons.Filled.Group,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Split into Groups",
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Groups result
            AnimatedVisibility(
                visible = showGroups && groupedItems.isNotEmpty(),
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                GroupsResultCard(groupedItems = groupedItems)
            }

            if (showGroups && groupedItems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Items list
            if (items.isNotEmpty()) {
                Text(
                    text = "Items (${items.size})",
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items.forEachIndexed { index, item ->
                        ItemCard(
                            item = item,
                            isEditing = editingIndex == index,
                            editingText = editingText,
                            onEditingTextChange = { editingText = it },
                            onEdit = {
                                editingIndex = index
                                editingText = items[index]
                            },
                            onSaveEdit = {
                                if (editingText.isNotBlank()) {
                                    items =
                                        items.toMutableList().apply {
                                            this[index] = editingText
                                        }
                                    editingIndex = -1
                                    editingText = ""
                                    showGroups = false
                                    keyboardController?.hide()
                                }
                            },
                            onCancelEdit = {
                                editingIndex = -1
                                editingText = ""
                                keyboardController?.hide()
                            },
                            onDelete = {
                                items =
                                    items.toMutableList().apply {
                                        removeAt(index)
                                    }
                                showGroups = false
                            },
                        )
                    }
                }
            } else {
                // Empty state
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            Icons.Filled.Group,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFF61758A),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Add items to split into groups",
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                            color = Color(0xFF61758A),
                            textAlign = TextAlign.Center,
                        )
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = {
                    Text(
                        "Item Name",
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3D99F5),
                        focusedLabelColor = Color(0xFF3D99F5),
                    ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onSave() }),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(12.dp))

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
                    Text(
                        "Cancel",
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onSave,
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

@Composable
fun ItemCard(
    item: String,
    isEditing: Boolean,
    editingText: String,
    onEditingTextChange: (String) -> Unit,
    onEdit: () -> Unit,
    onSaveEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.White,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        if (isEditing) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                OutlinedTextField(
                    value = editingText,
                    onValueChange = onEditingTextChange,
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3D99F5),
                        ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onSaveEdit() }),
                    singleLine = true,
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
                        Text(
                            "Cancel",
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onSaveEdit,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF3D99F5),
                            ),
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
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    fontSize = 16.sp,
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

@Composable
fun GroupsResultCard(groupedItems: List<List<String>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = Color(0xFFF0F8FF),
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Groups Result",
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF3D99F5),
                modifier = Modifier.padding(bottom = 16.dp),
            )

            groupedItems.forEachIndexed { groupIndex, group ->
                val isRemainingGroup =
                    groupIndex >= groupedItems.size - 1 &&
                        groupedItems.size > groupedItems.size &&
                        group.size < (groupedItems.getOrNull(0)?.size ?: 0)

                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = if (isRemainingGroup) Color(0xFFFFF3E0) else Color.White,
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                    ) {
                        Text(
                            text = "Group ${groupIndex + 1}",
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color(0xFF3D99F5),
                            modifier = Modifier.padding(bottom = 8.dp),
                        )

                        group.forEach { item ->
                            Text(
                                text = "• $item",
                                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 2.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

fun splitItemsIntoGroups(
    items: List<String>,
    numberOfGroups: Int,
    remainingItemsOption: RemainingItemsOption,
): List<List<String>> {
    if (items.isEmpty() || numberOfGroups <= 0) return emptyList()

    val shuffledItems = items.shuffled()
    val groups = mutableListOf<MutableList<String>>()

    when (remainingItemsOption) {
        RemainingItemsOption.DISTRIBUTE -> {
            // Initialize empty groups
            repeat(numberOfGroups) {
                groups.add(mutableListOf())
            }

            // Distribute items round-robin style
            shuffledItems.forEachIndexed { index, item ->
                groups[index % numberOfGroups].add(item)
            }
        }

        RemainingItemsOption.SEPARATE_GROUP -> {
            val itemsPerGroup = shuffledItems.size / numberOfGroups
            val remainingItems = shuffledItems.size % numberOfGroups

            // Create main groups with equal items
            repeat(numberOfGroups) { groupIndex ->
                val startIndex = groupIndex * itemsPerGroup
                val endIndex = startIndex + itemsPerGroup
                groups.add(shuffledItems.subList(startIndex, endIndex).toMutableList())
            }

            // Add remaining items as a separate group if any
            if (remainingItems > 0) {
                val remainingStartIndex = numberOfGroups * itemsPerGroup
                groups.add(shuffledItems.subList(remainingStartIndex, shuffledItems.size).toMutableList())
            }
        }

        RemainingItemsOption.EXCLUDE -> {
            val itemsPerGroup = shuffledItems.size / numberOfGroups

            // Create groups with equal items only, exclude remaining
            repeat(numberOfGroups) { groupIndex ->
                val startIndex = groupIndex * itemsPerGroup
                val endIndex = startIndex + itemsPerGroup
                groups.add(shuffledItems.subList(startIndex, endIndex).toMutableList())
            }
        }
    }

    return groups.filter { it.isNotEmpty() }
}

enum class RemainingItemsOption(
    val displayName: String,
) {
    DISTRIBUTE("Distribute remaining items among groups"),
    SEPARATE_GROUP("Create separate group for remaining items"),
    EXCLUDE("Exclude remaining items"),
}
