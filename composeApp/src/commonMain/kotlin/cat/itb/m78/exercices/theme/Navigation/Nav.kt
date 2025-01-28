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

@Composable
fun menuScreen(onNavigate: (Screen) -> Unit) {
    Column {
        Button(onClick = { onNavigate(Screen.Screen1) }) {
            Text("Screen1")
        }
        Button(onClick = { onNavigate(Screen.Screen2) }) {
            Text("Screen2")
        }

    }
}


@Composable
fun Screen1(navigateToScreen2: () -> Unit) {
    Column {
        Text(text = "Screen1")
        Button(onClick = navigateToScreen2) {
            Text("Go to Screen2")
        }
    }
}
@Composable
fun Screen2(navigateToScreen3: (String) -> Unit) {
    Column {
        Text(text = "Screen2")
        Button(onClick = { navigateToScreen3("Message from Screen2") }) {
            Text("Go to Screen3")
        }
    }
}
@Composable
fun Screen3(message: String) {
    Column {
        Text(text = "Screen3")
        Text(text = "Message: $message")
    }
}



@Composable
fun ManualNav() {
    val viewModel = viewModel { ManualNavAppViewModel() }
    val currentScreen = viewModel.currentScreen.value
    when (currentScreen) {
        Screen.Screen1 -> Screen1(
            navigateToScreen2 = { viewModel.navigateTo(Screen.Screen2) }
        )
        is Screen.Screen2 -> Screen2(
            navigateToScreen3 = { viewModel.navigateTo(Screen.Screen3(it)) }
        )
        is Screen.Screen3 -> Screen3(currentScreen.message)
    }
}

