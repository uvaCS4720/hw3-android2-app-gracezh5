package edu.nd.pmcburne.hwapp.one

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import edu.nd.pmcburne.hwapp.one.data.DatabaseProvider
import edu.nd.pmcburne.hwapp.one.data.GameRepository
import edu.nd.pmcburne.hwapp.one.ui.GameScreen
import edu.nd.pmcburne.hwapp.one.ui.theme.HWStarterRepoTheme
import edu.nd.pmcburne.hwapp.one.viewmodel.GameViewModel
import edu.nd.pmcburne.hwapp.one.viewmodel.GameViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = DatabaseProvider.getDatabase(applicationContext)
        val repository = GameRepository(db.gameDao())
        val factory = GameViewModelFactory(repository)
        val gameViewModel = ViewModelProvider(this, factory).get(GameViewModel::class.java)

        setContent {
            HWStarterRepoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(
                        viewModel = gameViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
