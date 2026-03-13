package edu.nd.pmcburne.hwapp.one.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.nd.pmcburne.hwapp.one.viewmodel.GameViewModel
import java.time.LocalDate

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {

    val gamesResponse by viewModel.games.collectAsState()
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var gender by remember { mutableStateOf("men") }

    val today = LocalDate.now()
    val year = today.year
    val month = today.monthValue
    val day = today.dayOfMonth

    LaunchedEffect(gender) {
        viewModel.fetchGames(gender, year, month, day)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "NCAA Basketball Scores",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Button(
                onClick = { gender = "men" },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Men")
            }

            Button(
                onClick = { gender = "women" }
            ) {
                Text("Women")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.fetchGames(gender, year, month, day) }
        ) {
            Text("Refresh")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        }

        error?.let {
            Text(text = "Error: $it")
        }

        gamesResponse?.let { response ->

            LazyColumn {

                items(response.games) { wrapper ->

                    val game = wrapper.game

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {

                            Text(
                                text = "${game.away.names.short} vs ${game.home.names.short}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Score: ${game.away.score} - ${game.home.score}"
                            )

                            Text(
                                text = "Status: ${game.gameState}"
                            )

                            if (game.gameState == "final") {
                                val winner =
                                    if (game.home.winner) game.home.names.short
                                    else game.away.names.short

                                Text("Winner: $winner")
                            }
                        }
                    }
                }
            }
        }
    }
}
