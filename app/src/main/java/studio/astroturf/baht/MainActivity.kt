package studio.astroturf.baht

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import studio.astroturf.baht.ui.coinFlip.CoinFlipScreen
import studio.astroturf.baht.ui.diceRoll.DiceRollScreen
import studio.astroturf.baht.ui.listSplitter.ListSplitterScreen
import studio.astroturf.baht.ui.luckyDraw.LuckyDrawScreen
import studio.astroturf.baht.ui.numberGenerator.NumberGeneratorScreen
import studio.astroturf.baht.ui.passwordGenerator.PasswordGeneratorScreen
import studio.astroturf.baht.ui.randomizer.RandomizerItem
import studio.astroturf.baht.ui.theme.BahtTheme
import studio.astroturf.baht.ui.weightedRandom.WeightedRandomScreen
import studio.astroturf.baht.ui.weightedWheelOfFortune.WeightedWheelOfFortuneScreen
import studio.astroturf.baht.ui.wheelOfFortune.WheelOfFortuneScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BahtTheme {
                BahtApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun BahtApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.RANDOM) }
    val randomNavController = rememberNavController()

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label,
                        )
                    },
                    label = {
                        Text(
                            text = it.label,
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                            fontWeight = FontWeight.Medium,
                        )
                    },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it },
                )
            }
        },
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            // Always render RandomNavigation but control its visibility
            RandomNavigation(
                navController = randomNavController,
                isVisible = currentDestination == AppDestinations.RANDOM,
                modifier = Modifier.padding(innerPadding),
            )

            if (currentDestination == AppDestinations.TOURNAMENTS) {
                TournamentsScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun RandomNavigation(
    navController: androidx.navigation.NavHostController,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    if (isVisible) {
        NavHost(
            navController = navController,
            startDestination = "random_home",
            modifier = modifier,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300),
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300),
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300),
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300),
                )
            },
        ) {
            composable("random_home") {
                RandomScreen(
                    onLuckyDrawClick = {
                        navController.navigate("lucky_draw")
                    },
                    onCoinFlipClick = {
                        navController.navigate("coin_flip")
                    },
                    onDiceRollClick = {
                        navController.navigate("dice_roll")
                    },
                    onNumberGeneratorClick = {
                        navController.navigate("number_generator")
                    },
                    onWeightedRandomClick = {
                        navController.navigate("weighted_random")
                    },
                    onWheelOfFortuneClick = {
                        navController.navigate("wheel_of_fortune")
                    },
                    onWeightedWheelOfFortuneClick = {
                        navController.navigate("weighted_wheel_of_fortune")
                    },
                    onPasswordGeneratorClick = {
                        navController.navigate("password_generator")
                    },
                    onListSplitterClick = {
                        navController.navigate("list_splitter")
                    },
                )
            }
            composable("lucky_draw") {
                LuckyDrawScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }
            composable("coin_flip") {
                CoinFlipScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }
            composable("dice_roll") {
                DiceRollScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }
            composable("number_generator") {
                NumberGeneratorScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }
            composable("weighted_random") {
                WeightedRandomScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }
            composable("wheel_of_fortune") {
                WheelOfFortuneScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }
            composable("weighted_wheel_of_fortune") {
                WeightedWheelOfFortuneScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }
            composable("password_generator") {
                PasswordGeneratorScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }
            composable("list_splitter") {
                ListSplitterScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    RANDOM("Random", Icons.Default.Casino),
    TOURNAMENTS("Tournaments", Icons.Default.EmojiEvents),
}

@Composable
fun RandomScreen(
    onLuckyDrawClick: () -> Unit,
    onCoinFlipClick: () -> Unit,
    onDiceRollClick: () -> Unit,
    onNumberGeneratorClick: () -> Unit,
    onWeightedRandomClick: () -> Unit,
    onWheelOfFortuneClick: () -> Unit,
    onWeightedWheelOfFortuneClick: () -> Unit,
    onPasswordGeneratorClick: () -> Unit,
    onListSplitterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchQuery by remember { mutableStateOf("") }

    val randomizerItems =
        listOf(
            RandomizerItemData(
                imageRes = R.drawable.random_pot,
                title = "Lucky Draw",
                description = "Draw random items from your custom list",
                onClick = onLuckyDrawClick,
            ),
            RandomizerItemData(
                imageRes = R.drawable.random_pot,
                title = "Coin Flip",
                description = "Classic heads or tails decision maker",
                onClick = onCoinFlipClick,
            ),
            RandomizerItemData(
                imageRes = R.drawable.random_pot,
                title = "Dice Roll",
                description = "Roll dice for games and decisions",
                onClick = onDiceRollClick,
            ),
            RandomizerItemData(
                imageRes = R.drawable.random_pot,
                title = "Number Generator",
                description = "Generate random numbers in any range",
                onClick = onNumberGeneratorClick,
            ),
            RandomizerItemData(
                imageRes = R.drawable.random_pot,
                title = "Weighted Random",
                description = "Select winners based on contribution amounts",
                onClick = onWeightedRandomClick,
            ),
            RandomizerItemData(
                imageRes = R.drawable.wheel_icon,
                title = "Wheel of Fortune",
                description = "Spin the wheel to select from custom options",
                onClick = onWheelOfFortuneClick,
            ),
            RandomizerItemData(
                imageRes = R.drawable.weighted_wheel_icon,
                title = "Weighted Wheel",
                description = "Spin a wheel where items have different probabilities",
                onClick = onWeightedWheelOfFortuneClick,
            ),
            RandomizerItemData(
                imageRes = R.drawable.random_pot,
                title = "Password Generator",
                description = "Generate secure passwords with custom settings",
                onClick = onPasswordGeneratorClick,
            ),
            RandomizerItemData(
                imageRes = R.drawable.random_pot,
                title = "List Splitter",
                description = "Split your list into equal groups randomly",
                onClick = onListSplitterClick,
            ),
        )

    val filteredItems =
        randomizerItems.filter { item ->
            if (searchQuery.isBlank()) {
                true
            } else {
                item.title.contains(searchQuery, ignoreCase = true) ||
                    item.description.contains(searchQuery, ignoreCase = true)
            }
        }

    val listState = rememberLazyListState()

    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        "Search games...",
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear search",
                            )
                        }
                    }
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
            )

            // Games List
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
            ) {
                items(filteredItems.size) { index ->
                    RandomizerItem(
                        imageRes = filteredItems[index].imageRes,
                        title = filteredItems[index].title,
                        description = filteredItems[index].description,
                        onClick = filteredItems[index].onClick,
                    )

                    if (index < filteredItems.size - 1) {
                        HorizontalDivider(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                            thickness = 1.dp,
                            color = Color(0xFFE8EBF0),
                        )
                    }
                }
            }
        }

        // Scroll indicator
        androidx.compose.foundation.layout.Column(
            modifier =
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
        ) {
            if (listState.canScrollBackward || listState.canScrollForward) {
                androidx.compose.foundation.Canvas(
                    modifier =
                        Modifier
                            .width(4.dp)
                            .height(60.dp)
                            .padding(vertical = 8.dp),
                ) {
                    val trackHeight = size.height
                    val thumbHeight = trackHeight * 0.3f
                    val scrollProgress =
                        if (listState.layoutInfo.totalItemsCount > 0) {
                            listState.firstVisibleItemIndex.toFloat() /
                                (listState.layoutInfo.totalItemsCount - listState.layoutInfo.visibleItemsInfo.size).coerceAtLeast(1)
                        } else {
                            0f
                        }
                    val thumbTop = scrollProgress * (trackHeight - thumbHeight)

                    drawRoundRect(
                        color =
                            androidx.compose.ui.graphics
                                .Color(0x1A3D99F5),
                        size =
                            androidx.compose.ui.geometry
                                .Size(size.width, trackHeight),
                        cornerRadius =
                            androidx.compose.ui.geometry
                                .CornerRadius(2.dp.toPx()),
                    )

                    drawRoundRect(
                        color =
                            androidx.compose.ui.graphics
                                .Color(0xFF3D99F5),
                        topLeft =
                            androidx.compose.ui.geometry
                                .Offset(0f, thumbTop),
                        size =
                            androidx.compose.ui.geometry
                                .Size(size.width, thumbHeight),
                        cornerRadius =
                            androidx.compose.ui.geometry
                                .CornerRadius(2.dp.toPx()),
                    )
                }
            }
        }
    }
}

data class RandomizerItemData(
    val imageRes: Int,
    val title: String,
    val description: String,
    val onClick: () -> Unit,
)

@Composable
fun TournamentsScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Tournaments Screen",
        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
        fontWeight = FontWeight.Medium,
        modifier = modifier,
    )
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BahtTheme {
        Greeting("Android")
    }
}
