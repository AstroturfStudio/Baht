package studio.astroturf.baht

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import studio.astroturf.baht.ui.luckyDraw.LuckyDrawScreen
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
            when (currentDestination) {
                AppDestinations.RANDOM -> RandomNavigation(modifier = Modifier.padding(innerPadding))
                AppDestinations.TOURNAMENTS -> TournamentsScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun RandomNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "random_home",
        modifier = modifier,
    ) {
        composable("random_home") {
            RandomScreen(
                onLuckyDrawClick = {
                    navController.navigate("lucky_draw")
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
                onClick = { /* TODO: Implement coin flip */ },
            ),
            RandomizerItemData(
                imageRes = R.drawable.random_pot,
                title = "Dice Roll",
                description = "Roll dice for games and decisions",
                onClick = { /* TODO: Implement dice roll */ },
            ),
            RandomizerItemData(
                imageRes = R.drawable.random_pot,
                title = "Number Generator",
                description = "Generate random numbers in any range",
                onClick = { /* TODO: Implement number generator */ },
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
