package studio.astroturf.baht.ui.tournament.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import studio.astroturf.baht.ui.tournament.models.Match
import studio.astroturf.baht.ui.tournament.models.Tournament

@Composable
fun TournamentBracket(
    tournament: Tournament,
    onMatchClick: (Match) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var matchPositions by remember { mutableStateOf<Map<String, Offset>>(emptyMap()) }

    val transformableState =
        rememberTransformableState { zoomChange, offsetChange, _ ->
            scale = (scale * zoomChange).coerceIn(0.3f, 4f)
            offset += offsetChange
        }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .clipToBounds()
                .transformable(
                    state = transformableState,
                    lockRotationOnZoomPan = true,
                ),
    ) {
        Box(
            modifier =
                Modifier
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y,
                    ),
        ) {
            // Tournament bracket layout
            Row(
                horizontalArrangement = Arrangement.spacedBy(120.dp),
                modifier = Modifier.padding(32.dp),
            ) {
                tournament.rounds.forEachIndexed { roundIndex, round ->
                    Column(
                        verticalArrangement =
                            Arrangement.spacedBy(
                                when (round.roundNumber) {
                                    1 -> 20.dp // Semi-finals spacing
                                    2 -> 0.dp // Final
                                    else -> 40.dp
                                },
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(140.dp),
                    ) {
                        // Add top spacer for final to center it
                        if (round.roundNumber == 2) {
                            Spacer(modifier = Modifier.height(45.dp))
                        }

                        round.matches.forEachIndexed { matchIndex, match ->
                            MatchCard(
                                match = match,
                                onMatchClick = onMatchClick,
                                onPositionChanged = { position ->
                                    // Store the position within the column and round info
                                    matchPositions = matchPositions + (
                                        match.id to
                                            Offset(
                                                roundIndex.toFloat(), // Store round index in x for now
                                                position.y,
                                            )
                                    )
                                },
                            )
                        }
                    }
                }
            }

            // Canvas overlay for connecting lines - positioned to match the content
            Canvas(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp),
            ) {
                drawBracketConnections(tournament, matchPositions)
            }
        }
    }
}

private fun DrawScope.drawBracketConnections(
    tournament: Tournament,
    matchPositions: Map<String, Offset>,
) {
    val lineColor = Color(0xFF666666)
    val strokeWidth = 2.dp.toPx()
    val matchWidth = 140.dp.toPx()
    val matchHeight = 70.dp.toPx()
    val roundSpacing = 120.dp.toPx()

    // Connect semifinals to final
    if (tournament.rounds.size >= 2) {
        val semifinals = tournament.rounds.find { it.roundNumber == 1 }?.matches ?: emptyList()
        val final =
            tournament.rounds
                .find { it.roundNumber == 2 }
                ?.matches
                ?.firstOrNull()

        if (semifinals.size == 2 && final != null) {
            val semi1Position = matchPositions[semifinals[0].id]
            val semi2Position = matchPositions[semifinals[1].id]
            val finalPosition = matchPositions[final.id]

            if (semi1Position != null && semi2Position != null && finalPosition != null) {
                // Calculate actual x positions based on round index (stored in x coordinate)
                val semi1RoundIndex = semi1Position.x.toInt()
                val semi2RoundIndex = semi2Position.x.toInt()
                val finalRoundIndex = finalPosition.x.toInt()

                val semi1X = semi1RoundIndex * (matchWidth + roundSpacing)
                val semi2X = semi2RoundIndex * (matchWidth + roundSpacing)
                val finalX = finalRoundIndex * (matchWidth + roundSpacing)

                // Use the positions with calculated x coordinates
                val semi1Right =
                    Offset(
                        semi1X + matchWidth,
                        semi1Position.y + matchHeight / 2,
                    )
                val semi2Right =
                    Offset(
                        semi2X + matchWidth,
                        semi2Position.y + matchHeight / 2,
                    )
                val finalLeft =
                    Offset(
                        finalX,
                        finalPosition.y + matchHeight / 2,
                    )

                // Calculate horizontal midpoint based on actual positions
                val horizontalMidpoint = semi1Right.x + (finalLeft.x - semi1Right.x) / 2

                // Calculate the vertical midpoint between the two semifinals
                val verticalMidpoint = (semi1Right.y + semi2Right.y) / 2

                // Draw lines for first semifinal
                drawLine(
                    color = lineColor,
                    start = semi1Right,
                    end = Offset(horizontalMidpoint, semi1Right.y),
                    strokeWidth = strokeWidth,
                )

                drawLine(
                    color = lineColor,
                    start = Offset(horizontalMidpoint, semi1Right.y),
                    end = Offset(horizontalMidpoint, verticalMidpoint),
                    strokeWidth = strokeWidth,
                )

                // Draw lines for second semifinal
                drawLine(
                    color = lineColor,
                    start = semi2Right,
                    end = Offset(horizontalMidpoint, semi2Right.y),
                    strokeWidth = strokeWidth,
                )

                drawLine(
                    color = lineColor,
                    start = Offset(horizontalMidpoint, semi2Right.y),
                    end = Offset(horizontalMidpoint, verticalMidpoint),
                    strokeWidth = strokeWidth,
                )

                // Final horizontal line from vertical midpoint to final match
                drawLine(
                    color = lineColor,
                    start = Offset(horizontalMidpoint, verticalMidpoint),
                    end = finalLeft,
                    strokeWidth = strokeWidth,
                )
            }
        }
    }
} 
