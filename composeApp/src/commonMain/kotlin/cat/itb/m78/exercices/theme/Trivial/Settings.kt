package cat.itb.m78.exercices.theme.Trivial

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider



data class TrivialSettings(
    var difficulty : String = "facil",
    var questionsPerGame: Int = 5,
    var tiempoRonda: Int=10,
    var final_score: Int =0
)

data object TrivialSettingsManager{
    private var settings = TrivialSettings()
    fun update(newSettings: TrivialSettings){
        settings = newSettings
    }
    fun get() = settings
}

class SettingsViewModel : ViewModel() {
    var totalRounds by mutableStateOf(TrivialSettingsManager.get().questionsPerGame)
    var gameTimeSeconds by mutableStateOf(TrivialSettingsManager.get().tiempoRonda)
    var difficulty by mutableStateOf(TrivialSettingsManager.get().difficulty)
    var finalScore by mutableStateOf(TrivialSettingsManager.get().final_score)
    fun saveSettings() {
        val newSettings = TrivialSettings(
            difficulty = difficulty.trim().lowercase(), // Normalizar el string
            questionsPerGame = totalRounds,
            tiempoRonda = gameTimeSeconds,
            final_score= finalScore
        )
        TrivialSettingsManager.update(newSettings)
    }

    fun updateTotalRounds(rounds: Int) {
        totalRounds = rounds
    }


    fun updateGameTime(seconds: Int) {
        gameTimeSeconds = seconds
    }

    fun updateDifficulty(newDifficulty: String) {
        difficulty = newDifficulty.trim().lowercase()
    }
}

