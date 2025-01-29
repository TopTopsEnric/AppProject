package cat.itb.m78.exercices.theme.Navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.bundle.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel



sealed class Screen {
    object Screen1 : Screen()
    object Screen2 : Screen()
    data class Screen3(val message: String) : Screen()
}

class ManualNavAppViewModel : ViewModel() {
    val currentScreen = mutableStateOf<Screen>(Screen.Screen1)

    fun navigateTo(screen: Screen) {
        currentScreen.value = screen
    }
}



