package studio.astroturf.baht.ui.tournament

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import studio.astroturf.baht.ui.tournament.components.TournamentBracket
import studio.astroturf.baht.ui.tournament.models.Match
import studio.astroturf.baht.ui.tournament.utils.TournamentGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var tournamentIndex by remember { mutableStateOf(0) }
    var selectedMatch by remember { mutableStateOf<Match?>(null) }

    val sampleTournaments =
        listOf(
            TournamentGenerator.createSampleSingleEliminationTournament(),
            TournamentGenerator.createCompletedSampleTournament(),
        )

    val selectedTournament = sampleTournaments[tournamentIndex]

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selectedTournament.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    tournamentIndex = (tournamentIndex + 1) % sampleTournaments.size
                },
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Switch Tournament",
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            TournamentBracket(
                tournament = selectedTournament,
                onMatchClick = { match ->
                    selectedMatch = match
                    // TODO: Handle match click (open match details dialog)
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
} 
