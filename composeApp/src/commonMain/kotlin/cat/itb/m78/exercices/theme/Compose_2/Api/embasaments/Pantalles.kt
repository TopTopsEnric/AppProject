package cat.itb.m78.exercices.theme.Compose_2.Api.embasaments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cat.itb.m78.exercices.theme.Trivial.*

@Composable
fun EmbaScreen3(navigateToMainMenu: () -> Unit) {
    val viewModel = viewModel { GameViewModel() }
    Screen3(
        navigateToMainMenu = {
            viewModel.resetForNewGame()
            navigateToMainMenu()
        },
        score = viewModel.finalScore
    )
}

@Composable
fun EmScreen3(
    navigateToMainMenu: () -> Unit,
    score: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tu Puntuación Final: $score",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateToMainMenu) {
            Text("Volver al Menú")
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PANTALLA 4: AJUSTES

@Composable
fun Embacreen4(navigateBack: () -> Unit) {
    val viewModel = viewModel { SettingsViewModel() } // Usa viewModel() para obtener la instancia


}

@Composable
fun EmScreen4(

) {

}



// ─────────────────────────────────────────────────────────────────────────────
// NAVEGACIÓN: Se configura el NavHost y se comparte la misma instancia del GameViewModel
@Composable
fun blaLibNavScreenSample() {
    val navController = rememberNavController()
    // Se obtiene o crea la instancia compartida del viewmodel
    val gameViewModel: GameViewModel = viewModel()

    NavHost(navController = navController, startDestination = Destination.Screen3.toString()) {

        composable(Destination.Screen3.toString()) {
            EmbaScreen3(
                navigateToMainMenu = {
                    navController.popBackStack(Destination.Screen4.toString(), false)
                },
                )
        }
        composable(Destination.Screen4.toString()) {
            Embacreen4(
                navigateBack = { navController.navigate(Destination.Screen3.toString()) },
            )
        }
    }
}