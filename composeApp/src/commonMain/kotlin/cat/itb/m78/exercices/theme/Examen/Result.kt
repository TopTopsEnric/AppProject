package cat.itb.m78.exercices.theme.Examen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cat.itb.m78.exercices.theme.Trivial.TrivialSettings
import cat.itb.m78.exercices.theme.Trivial.TrivialSettingsManager

data class valorFinal(
    var final_score: Int =0
)

data object ValorFinalManager{
    private var settings = valorFinal()
    fun update(newSettings: valorFinal){
        settings = newSettings
    }
    fun get() = settings
}

class ResultadoViewModel : ViewModel() {
    var finalScore by mutableStateOf(ValorFinalManager.get().final_score)
}