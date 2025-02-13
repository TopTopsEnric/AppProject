package cat.itb.m78.exercices.theme.Trivial

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class TrivialSettings(
    val dificulty : String = "easy",
    val questionsPerGame: Int = 5,
    val tiempoRonda: Int=10
)

data object TrivialSettingsManager{
    private var settings = TrivialSettings()
    fun update(newSettings: TrivialSettings){
        settings = newSettings
    }
    fun get() = settings
}

class SettingsViewModel: ViewModel(){
    // ----- AJUSTES (por defecto) -----
    var totalRounds by mutableStateOf(TrivialSettingsManager.get().questionsPerGame)
    var gameTimeSeconds by mutableStateOf(TrivialSettingsManager.get().tiempoRonda)
    var difficulty by mutableStateOf(TrivialSettingsManager.get().dificulty)


    fun saveSettings(){
        val settingsFromInputs = TrivialSettings()
        TrivialSettingsManager.update(settingsFromInputs)
    }
    // ----- FUNCIONES PARA MODIFICAR LOS AJUSTES -----
    fun updateTotalRounds(rounds: Int) {
        totalRounds = rounds
    }

    fun updateGameTime(seconds: Int) {
        gameTimeSeconds = seconds
    }

    fun updateDifficulty(hard: String) {
        difficulty = hard
    }
}