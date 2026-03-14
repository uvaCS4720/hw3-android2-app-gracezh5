package edu.nd.pmcburne.hwapp.one.data

import edu.nd.pmcburne.hwapp.one.network.RetrofitInstance

class GameRepository(private val gameDao: GameDao) {

    private val api = RetrofitInstance.api

    fun getStoredGames() = gameDao.getGames()

    suspend fun refreshGames(
        gender: String,
        year: Int,
        month: Int,
        day: Int
    ) {

        val response = api.getGames(
            gender = gender,
            year = year.toString(),
            month = month.toString(),
            day = day.toString()
        )

        val entities = response.games.map {

            val game = it.game

            GameEntity(
                gameID = game.gameID,
                homeTeam = game.home.names.short,
                awayTeam = game.away.names.short,
                homeScore = game.home.score,
                awayScore = game.away.score,
                startTime = game.startTime,
                gameState = game.gameState,
                currentPeriod = game.currentPeriod,
                contestClock = game.contestClock,
                winner =
                    if (game.home.winner) game.home.names.short
                    else game.away.names.short
            )
        }

        gameDao.insertGames(entities)
    }
}
