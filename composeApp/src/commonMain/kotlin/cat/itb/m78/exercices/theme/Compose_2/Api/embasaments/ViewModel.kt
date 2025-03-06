package cat.itb.m78.exercices.theme.Compose_2.Api.embasaments

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.serialization.Serializable

@Serializable
data class embassament(
    val dia : String,
    val estaci: String,
    val nivell_absolut : String,
    val percentatge_volum_embassat: String,
    val volum_embassat: String
)

class EmbaViewModel : ViewModel() {
    var datos by mutableStateOf<List<embassament>>(emptyList())



}