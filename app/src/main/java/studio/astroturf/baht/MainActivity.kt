package studio.astroturf.baht

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import studio.astroturf.baht.ui.coinFlip.CoinFlipScreen
import studio.astroturf.baht.ui.diceRoll.DiceRollScreen
import studio.astroturf.baht.ui.luckyDraw.LuckyDrawScreen
import studio.astroturf.baht.ui.numberGenerator.NumberGeneratorScreen
import studio.astroturf.baht.ui.randomizer.RandomizerItem
import studio.astroturf.baht.ui.theme.BahtTheme

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
                    label = { Text(it.label) },
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
    modifier: Modifier = Modifier,
) {
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
        )

    LazyColumn(
        modifier = modifier,
    ) {
        items(randomizerItems) { item ->
            RandomizerItem(
                imageRes = item.imageRes,
                title = item.title,
                description = item.description,
                onClick = item.onClick,
            )
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
