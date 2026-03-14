package edu.nd.pmcburne.hwapp.one.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.nd.pmcburne.hwapp.one.data.DatabaseProvider
import edu.nd.pmcburne.hwapp.one.data.GameRepository
import edu.nd.pmcburne.hwapp.one.viewmodel.GameViewModel
import edu.nd.pmcburne.hwapp.one.viewmodel.GameViewModelFactory
import java.time.LocalDate

@Composable
fun GameScreen(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    // Create database → DAO → repository
    val db = remember {
        DatabaseProvider.getDatabase(context)
    }

    val repository = remember {
        GameRepository(db.gameDao())
    }

    val viewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(repository)
    )

    val games by viewModel.games.collectAsState()
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var gender by remember { mutableStateOf("men") }

    val today = LocalDate.now()
    var selectedDate by remember { mutableStateOf(today) }

    LaunchedEffect(gender) {
        viewModel.fetchGames(
            gender,
            selectedDate.year,
            selectedDate.monthValue,
            selectedDate.dayOfMonth
        )
    }

    Column(
        modifier = modifier
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
            onClick = {
                val datePicker = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        selectedDate = LocalDate.of(year, month + 1, dayOfMonth)

                        viewModel.fetchGames(
                            gender,
                            year,
                            month + 1,
                            dayOfMonth
                        )
                    },
                    selectedDate.year,
                    selectedDate.monthValue - 1,
                    selectedDate.dayOfMonth
                )
                datePicker.show()
            }
        ) {
            Text(text = "Select Date: $selectedDate")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.fetchGames(
                    gender,
                    selectedDate.year,
                    selectedDate.monthValue,
                    selectedDate.dayOfMonth
                )
            }
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

        LazyColumn {

            items(games) { game ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        Text(
                            text = "${game.awayTeam} (Away) vs ${game.homeTeam} (Home)",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        if (game.gameState == "pre") {
                            Text("Start Time: ${game.startTime}")
                        } else {
                            Text("Score: ${game.awayScore} - ${game.homeScore}")
                        }

                        val statusText = when (game.gameState.lowercase()) {
                            "pre" -> "Upcoming"
                            "live" -> "In Progress"
                            "final" -> "Final"
                            else -> game.gameState
                        }

                        Text("Status: $statusText")

                        if (game.gameState.lowercase() == "live") {
                            Text(
                                "Period: ${game.currentPeriod}, Time Remaining: ${game.contestClock}"
                            )
                        }

                        if (game.gameState.lowercase() == "final") {
                            Text("Winner: ${game.winner}")
                        }
                    }
                }
            }
        }
    }
}
