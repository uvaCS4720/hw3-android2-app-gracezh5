package edu.nd.pmcburne.hwapp.one.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(

    @PrimaryKey
    val gameID: String,

    val homeTeam: String,
    val awayTeam: String,

    val homeScore: String,
    val awayScore: String,

    val startTime: String,
    val gameState: String,

    val currentPeriod: String,
    val contestClock: String,

    val winner: String
)
