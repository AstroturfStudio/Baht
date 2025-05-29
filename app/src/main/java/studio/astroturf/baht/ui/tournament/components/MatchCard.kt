package studio.astroturf.baht.ui.tournament.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.astroturf.baht.ui.tournament.models.Match
import studio.astroturf.baht.ui.tournament.models.MatchStatus

@Composable
fun MatchCard(
    match: Match,
    onMatchClick: (Match) -> Unit = {},
    onPositionChanged: (androidx.compose.ui.geometry.Offset) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .width(140.dp)
                .height(70.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    when (match.status) {
                        MatchStatus.COMPLETED -> Color(0xFFE8F5E8)
                        MatchStatus.IN_PROGRESS -> Color(0xFFFFF3E0)
                        MatchStatus.PENDING -> Color(0xFFF5F5F5)
                    },
                ).border(
                    width = 1.dp,
                    color =
                        when (match.status) {
                            MatchStatus.COMPLETED -> Color(0xFF4CAF50)
                            MatchStatus.IN_PROGRESS -> Color(0xFFFF9800)
                            MatchStatus.PENDING -> Color(0xFFBDBDBD)
                        },
                    shape = RoundedCornerShape(8.dp),
                ).clickable { onMatchClick(match) }
                .onGloballyPositioned { coordinates ->
                    onPositionChanged(Offset(coordinates.positionInParent().x, coordinates.positionInParent().y))
                },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
        ) {
            // Participant 1
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = match.participant1?.name ?: "TBD",
                    fontSize = 11.sp,
                    fontWeight = if (match.winner?.id == match.participant1?.id) FontWeight.Bold else FontWeight.Normal,
                    color = if (match.participant1 == null) Color.Gray else Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )

                if (match.status == MatchStatus.COMPLETED && match.score1 != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = match.score1.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Participant 2
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = match.participant2?.name ?: "TBD",
                    fontSize = 11.sp,
                    fontWeight = if (match.winner?.id == match.participant2?.id) FontWeight.Bold else FontWeight.Normal,
                    color = if (match.participant2 == null) Color.Gray else Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )

                if (match.status == MatchStatus.COMPLETED && match.score2 != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = match.score2.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }
            }
        }
    }
} 
