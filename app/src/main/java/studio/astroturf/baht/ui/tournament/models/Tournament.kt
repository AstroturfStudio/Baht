package studio.astroturf.baht.ui.tournament.models

import java.util.UUID

data class Tournament(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val mode: TournamentMode,
    val status: TournamentStatus,
    val participants: List<Participant>,
    val rounds: List<Round>,
    val createdAt: Long = System.currentTimeMillis(),
)

data class Participant(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val seed: Int? = null,
)

data class Round(
    val roundNumber: Int,
    val matches: List<Match>,
)

data class Match(
    val id: String = UUID.randomUUID().toString(),
    val tournamentId: String,
    val round: Int,
    val participant1: Participant?,
    val participant2: Participant?,
    val winner: Participant? = null,
    val status: MatchStatus = MatchStatus.PENDING,
    val score1: Int? = null,
    val score2: Int? = null,
)

enum class TournamentMode {
    SINGLE_ELIMINATION,
    DOUBLE_ELIMINATION,
    ROUND_ROBIN,
}

enum class TournamentStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
}

enum class MatchStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
} 
