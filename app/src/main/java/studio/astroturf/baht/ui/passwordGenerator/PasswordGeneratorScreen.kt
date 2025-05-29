package studio.astroturf.baht.ui.passwordGenerator

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
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import studio.astroturf.baht.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordGeneratorScreen(onBackClick: () -> Unit) {
    var passwordLength by remember { mutableFloatStateOf(12f) }
    var includeUppercase by remember { mutableStateOf(true) }
    var includeLowercase by remember { mutableStateOf(true) }
    var includeNumbers by remember { mutableStateOf(true) }
    var includeSymbols by remember { mutableStateOf(false) }
    var excludeSimilar by remember { mutableStateOf(false) }
    var excludeAmbiguous by remember { mutableStateOf(false) }
    var generatedPassword by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }
    var animatingPasswords by remember { mutableStateOf(listOf<String>()) }

    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Password Generator",
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
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Configuration Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    Text(
                        text = "Password Settings",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        color = Color(0xFF121417),
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password Length
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Length",
                                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                fontWeight = FontWeight.Medium,
                            )
                            Text(
                                text = passwordLength.toInt().toString(),
                                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3D99F5),
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Slider(
                            value = passwordLength,
                            onValueChange = { passwordLength = it },
                            valueRange = 4f..128f,
                            steps = 124,
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Character Options
                    PasswordOption(
                        title = "Uppercase Letters (A-Z)",
                        checked = includeUppercase,
                        onCheckedChange = { includeUppercase = it },
                    )

                    PasswordOption(
                        title = "Lowercase Letters (a-z)",
                        checked = includeLowercase,
                        onCheckedChange = { includeLowercase = it },
                    )

                    PasswordOption(
                        title = "Numbers (0-9)",
                        checked = includeNumbers,
                        onCheckedChange = { includeNumbers = it },
                    )

                    PasswordOption(
                        title = "Symbols (!@#$%^&*)",
                        checked = includeSymbols,
                        onCheckedChange = { includeSymbols = it },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Advanced Options
                    Text(
                        text = "Advanced Options",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        color = Color(0xFF121417),
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PasswordOption(
                        title = "Exclude Similar (i, l, 1, L, o, 0, O)",
                        checked = excludeSimilar,
                        onCheckedChange = { excludeSimilar = it },
                    )

                    PasswordOption(
                        title = "Exclude Ambiguous ({ } [ ] ( ) / \\ ' \" ~ , ; . < >)",
                        checked = excludeAmbiguous,
                        onCheckedChange = { excludeAmbiguous = it },
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Password Display
            AnimatedPasswordDisplay(
                isGenerating = isGenerating,
                generatedPassword = generatedPassword,
                showResult = showResult,
                animatingPasswords = animatingPasswords,
                modifier = Modifier.size(200.dp),
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Result Card
            AnimatedVisibility(
                visible = showResult,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                ResultCard(
                    generatedPassword = generatedPassword,
                    onCopyClick = {
                        clipboardManager.setText(AnnotatedString(generatedPassword))
                    },
                    onRegenerateClick = {
                        generatePassword(
                            length = passwordLength.toInt(),
                            includeUppercase = includeUppercase,
                            includeLowercase = includeLowercase,
                            includeNumbers = includeNumbers,
                            includeSymbols = includeSymbols,
                            excludeSimilar = excludeSimilar,
                            excludeAmbiguous = excludeAmbiguous,
                        ) { password ->
                            generatedPassword = password
                        }
                    },
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Generate Button
            Button(
                onClick = {
                    if (hasValidOptions(includeUppercase, includeLowercase, includeNumbers, includeSymbols)) {
                        isGenerating = true
                        showResult = false
                        // Generate sample passwords for animation
                        animatingPasswords =
                            (1..5).map {
                                generateSamplePassword(
                                    passwordLength.toInt(),
                                    includeUppercase,
                                    includeLowercase,
                                    includeNumbers,
                                    includeSymbols,
                                    excludeSimilar,
                                    excludeAmbiguous,
                                )
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isGenerating && hasValidOptions(includeUppercase, includeLowercase, includeNumbers, includeSymbols),
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
            delay(2000) // Animation duration matching NumberGenerator
            generatePassword(
                length = passwordLength.toInt(),
                includeUppercase = includeUppercase,
                includeLowercase = includeLowercase,
                includeNumbers = includeNumbers,
                includeSymbols = includeSymbols,
                excludeSimilar = excludeSimilar,
                excludeAmbiguous = excludeAmbiguous,
            ) { password ->
                generatedPassword = password
                showResult = true
                isGenerating = false
            }
        }
    }
}

@Composable
fun AnimatedPasswordDisplay(
    isGenerating: Boolean,
    generatedPassword: String,
    showResult: Boolean,
    animatingPasswords: List<String>,
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Password",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ready!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                isGenerating && animatingPasswords.isNotEmpty() -> {
                    val infiniteTransition = rememberInfiniteTransition(label = "passwordCycle")
                    val cycleIndex by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = animatingPasswords.size.toFloat(),
                        animationSpec =
                            infiniteRepeatable(
                                animation = tween(200, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart,
                            ),
                        label = "cycleIndex",
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Generating",
                            tint = Color.White,
                            modifier =
                                Modifier
                                    .size(32.dp)
                                    .rotate(rotation),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "***",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                else -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Password Generator",
                            tint = Color(0xFF61758A),
                            modifier = Modifier.size(32.dp),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "🔐",
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultCard(
    generatedPassword: String,
    onCopyClick: () -> Unit,
    onRegenerateClick: () -> Unit,
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
                text = "🔐 Your Password! 🔐",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            SelectionContainer {
                Text(
                    text = generatedPassword,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    onClick = onCopyClick,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF4CAF50),
                        ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Icon(
                        Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Copy",
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        fontWeight = FontWeight.Medium,
                    )
                }

                Button(
                    onClick = onRegenerateClick,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White,
                        ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Regenerate",
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "New",
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
private fun GenerateButton(isGenerating: Boolean) {
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
            text = if (isGenerating) "Generating..." else "Generate Password",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
        )
    }
}

@Composable
private fun PasswordOption(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onCheckedChange(!checked) },
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (checked) {
                        Color(0xFF3D99F5).copy(alpha = 0.1f)
                    } else {
                        Color.Transparent
                    },
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                fontWeight = if (checked) FontWeight.SemiBold else FontWeight.Medium,
                color = if (checked) Color(0xFF3D99F5) else Color(0xFF121417),
                modifier = Modifier.weight(1f),
            )

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors =
                    SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF3D99F5),
                        checkedBorderColor = Color(0xFF3D99F5),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFE0E0E0),
                        uncheckedBorderColor = Color(0xFFE0E0E0),
                    ),
            )
        }
    }
}

private fun generatePassword(
    length: Int,
    includeUppercase: Boolean,
    includeLowercase: Boolean,
    includeNumbers: Boolean,
    includeSymbols: Boolean,
    excludeSimilar: Boolean,
    excludeAmbiguous: Boolean,
    onPasswordGenerated: (String) -> Unit,
) {
    val uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val lowercase = "abcdefghijklmnopqrstuvwxyz"
    val numbers = "0123456789"
    val symbols = "!@#$%^&*()_+-=[]{}|;:,.<>?"

    val similarChars = "il1Lo0O"
    val ambiguousChars = "{}[]()/'\"~,;.<>"

    var charset = ""

    if (includeUppercase) {
        charset +=
            if (excludeSimilar) {
                uppercase.filter { !similarChars.contains(it) }
            } else {
                uppercase
            }
    }

    if (includeLowercase) {
        charset +=
            if (excludeSimilar) {
                lowercase.filter { !similarChars.contains(it) }
            } else {
                lowercase
            }
    }

    if (includeNumbers) {
        charset +=
            if (excludeSimilar) {
                numbers.filter { !similarChars.contains(it) }
            } else {
                numbers
            }
    }

    if (includeSymbols) {
        charset +=
            if (excludeAmbiguous) {
                symbols.filter { !ambiguousChars.contains(it) }
            } else {
                symbols
            }
    }

    if (charset.isEmpty()) {
        onPasswordGenerated("Error: No character set selected")
        return
    }

    val password =
        (1..length)
            .map { charset.random() }
            .joinToString("")

    onPasswordGenerated(password)
}

private fun hasValidOptions(
    includeUppercase: Boolean,
    includeLowercase: Boolean,
    includeNumbers: Boolean,
    includeSymbols: Boolean,
): Boolean = includeUppercase || includeLowercase || includeNumbers || includeSymbols

private fun generateSamplePassword(
    length: Int,
    includeUppercase: Boolean,
    includeLowercase: Boolean,
    includeNumbers: Boolean,
    includeSymbols: Boolean,
    excludeSimilar: Boolean,
    excludeAmbiguous: Boolean,
): String {
    val uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val lowercase = "abcdefghijklmnopqrstuvwxyz"
    val numbers = "0123456789"
    val symbols = "!@#$%^&*()_+-=[]{}|;:,.<>?"

    val similarChars = "il1Lo0O"
    val ambiguousChars = "{}[]()/'\"~,;.<>"

    var charset = ""

    if (includeUppercase) {
        charset +=
            if (excludeSimilar) {
                uppercase.filter { !similarChars.contains(it) }
            } else {
                uppercase
            }
    }

    if (includeLowercase) {
        charset +=
            if (excludeSimilar) {
                lowercase.filter { !similarChars.contains(it) }
            } else {
                lowercase
            }
    }

    if (includeNumbers) {
        charset +=
            if (excludeSimilar) {
                numbers.filter { !similarChars.contains(it) }
            } else {
                numbers
            }
    }

    if (includeSymbols) {
        charset +=
            if (excludeAmbiguous) {
                symbols.filter { !ambiguousChars.contains(it) }
            } else {
                symbols
            }
    }

    if (charset.isEmpty()) {
        return "***"
    }

    return (1..length)
        .map { charset.random() }
        .joinToString("")
}
