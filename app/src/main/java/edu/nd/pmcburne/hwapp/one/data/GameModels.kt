package edu.nd.pmcburne.hwapp.one.data

data class GamesResponse(
    val inputMD5Sum: String,
    val instanceId: String,
    val updated_at: String,
    val games: List<GameWrapper>
)

data class GameWrapper(
    val game: Game
)

data class Game(
    val gameID: String,
    val home: Team,
    val away: Team,
    val startTime: String,
    val startDate: String,
    val gameState: String,
    val finalMessage: String,
    val currentPeriod: String,
    val contestClock: String
)

data class Team(
    val score: String,
    val names: TeamNames,
    val winner: Boolean,
    val seed: String,
    val conferences: List<Conference>
)

data class TeamNames(
    val char6: String,
    val short: String,
    val seo: String,
    val full: String
)

data class Conference(
    val conferenceName: String,
    val conferenceSeo: String
)