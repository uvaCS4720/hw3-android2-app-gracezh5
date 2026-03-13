package edu.nd.pmcburne.hwapp.one.data

import edu.nd.pmcburne.hwapp.one.network.RetrofitInstance

class GameRepository {

    private val api = RetrofitInstance.api

    suspend fun getGames(
        gender: String,
        year: Int,
        month: Int,
        day: Int
    ): GamesResponse {
        return api.getGames(
            gender = gender,
            year = year.toString(),
            month = month.toString(),
            day = day.toString()
        )
    }
}
