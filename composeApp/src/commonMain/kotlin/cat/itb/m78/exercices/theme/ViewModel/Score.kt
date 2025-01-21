package cat.itb.m78.exercices.theme.ViewModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class Score : ViewModel() {
    // Estado directamente en el ViewModel
    var equipo1 = mutableStateOf(0)
        private set

    var equipo2 = mutableStateOf(0)
        private set

    fun Scoreequipo1() {
        equipo1.value++
    }

    fun Scoreequipo2() {
        equipo2.value++
    }
}