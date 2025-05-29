package studio.astroturf.baht.ui.tournament.utils

import studio.astroturf.baht.ui.tournament.models.Match
import studio.astroturf.baht.ui.tournament.models.MatchStatus
import studio.astroturf.baht.ui.tournament.models.Participant
import studio.astroturf.baht.ui.tournament.models.Round
import studio.astroturf.baht.ui.tournament.models.Tournament
import studio.astroturf.baht.ui.tournament.models.TournamentMode
import studio.astroturf.baht.ui.tournament.models.TournamentStatus

object TournamentGenerator {
    fun createSampleSingleEliminationTournament(): Tournament {
        val participants =
            listOf(
                Participant(name = "Team Alpha", seed = 1),
                Participant(name = "Team Beta", seed = 2),
                Participant(name = "Team Gamma", seed = 3),
                Participant(name = "Team Delta", seed = 4),
            )

        val tournamentId = "sample-tournament"

        // Create semifinal matches
        val semifinal1 =
            Match(
                tournamentId = tournamentId,
                round = 1,
                participant1 = participants[0], // Team Alpha
                participant2 = participants[3], // Team Delta
                status = MatchStatus.COMPLETED,
                winner = participants[0], // Team Alpha wins
                score1 = 3,
                score2 = 1,
            )

        val semifinal2 =
            Match(
                tournamentId = tournamentId,
                round = 1,
                participant1 = participants[1], // Team Beta
                participant2 = participants[2], // Team Gamma
                status = MatchStatus.PENDING,
            )

        // Create final match
        val finalMatch =
            Match(
                tournamentId = tournamentId,
                round = 2,
                participant1 = participants[0], // Team Alpha (winner of semifinal1)
                participant2 = null, // TBD (waiting for semifinal2)
                status = MatchStatus.PENDING,
            )

        val rounds =
            listOf(
                Round(roundNumber = 1, matches = listOf(semifinal1, semifinal2)),
                Round(roundNumber = 2, matches = listOf(finalMatch)),
            )

        return Tournament(
            id = tournamentId,
            name = "Sample Tournament",
            mode = TournamentMode.SINGLE_ELIMINATION,
            status = TournamentStatus.IN_PROGRESS,
            participants = participants,
            rounds = rounds,
        )
    }

    fun createCompletedSampleTournament(): Tournament {
        val participants =
            listOf(
                Participant(name = "Warriors", seed = 1),
                Participant(name = "Knights", seed = 2),
                Participant(name = "Dragons", seed = 3),
                Participant(name = "Tigers", seed = 4),
            )

        val tournamentId = "completed-tournament"

        // Create completed semifinal matches
        val semifinal1 =
            Match(
                tournamentId = tournamentId,
                round = 1,
                participant1 = participants[0], // Warriors
                participant2 = participants[3], // Tigers
                status = MatchStatus.COMPLETED,
                winner = participants[0], // Warriors win
                score1 = 2,
                score2 = 0,
            )

        val semifinal2 =
            Match(
                tournamentId = tournamentId,
                round = 1,
                participant1 = participants[1], // Knights
                participant2 = participants[2], // Dragons
                status = MatchStatus.COMPLETED,
                winner = participants[1], // Knights win
                score1 = 1,
                score2 = 0,
            )

        // Create completed final match
        val finalMatch =
            Match(
                tournamentId = tournamentId,
                round = 2,
                participant1 = participants[0], // Warriors
                participant2 = participants[1], // Knights
                status = MatchStatus.COMPLETED,
                winner = participants[0], // Warriors win tournament
                score1 = 3,
                score2 = 2,
            )

        val rounds =
            listOf(
                Round(roundNumber = 1, matches = listOf(semifinal1, semifinal2)),
                Round(roundNumber = 2, matches = listOf(finalMatch)),
            )

        return Tournament(
            id = tournamentId,
            name = "Championship Finals",
            mode = TournamentMode.SINGLE_ELIMINATION,
            status = TournamentStatus.COMPLETED,
            participants = participants,
            rounds = rounds,
        )
    }
} 
