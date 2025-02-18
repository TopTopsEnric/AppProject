package cat.itb.m78.exercices.theme.Trivial

// IMPORTS NECESARIOS
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import m78exercices.composeapp.generated.resources.Res
import m78exercices.composeapp.generated.resources.dice_1
import m78exercices.composeapp.generated.resources.karenCara
import m78exercices.composeapp.generated.resources.trivial
import org.jetbrains.compose.resources.painterResource

// ─────────────────────────────────────────────────────────────────────────────
// OBJETO DESTINO CON LAS PANTALLAS (utilizando Serializable para ilustrar la idea)
object Destination {
    @Serializable
    data object Screen1

    @Serializable
    data object Screen2

    @Serializable
    data object Screen3

    @Serializable
    data object Screen4
}



// PANTALLA 1: MENÚ PRINCIPAL
@Composable
fun Screen1(
    navigateToScreen2: () -> Unit,
    navigateToScreen4: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFf80000)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(Res.drawable.trivial),
            contentDescription = "Imagen de fondo",
            contentScale = ContentScale.Crop, // Ajusta la escala de la imagen
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = {
                navigateToScreen2()
            }) {
                Text("Jugar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = navigateToScreen4) {
                Text("Ajustes")
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PANTALLA 2: PANTALLA DEL JUEGO (PREGUNTA, RESPUESTAS Y TEMPORIZADOR)

@Composable
fun FScreen2(navigateToScore: () -> Unit) {
    val viewModel = viewModel { GameViewModel() }
    Screen2(
        navigateToScore,
        viewModel.currentRound,
        TrivialSettingsManager.get().questionsPerGame,
        viewModel.currentQuestion,
        viewModel.timerValue,
        viewModel.gameFinished,
        answerQuestion = viewModel::answerQuestion,
        loadQuestion = viewModel::loadQuestion
    )
}

@Composable
fun Screen2(
    navigateToScore: () -> Unit,
    currentRound: Int,
    totalRounds: Int,
    currentQuestion: Question?,
    timerValue: Int,
    gameFinished: Boolean,
    answerQuestion: (Int) -> Unit,
    loadQuestion: () -> Unit
) {
    if (gameFinished) {
        LaunchedEffect(Unit) {
            navigateToScore()
        }
    }
    LaunchedEffect(currentRound) {
        loadQuestion()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Ronda $currentRound de $totalRounds")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = currentQuestion?.questionText ?: "Cargando pregunta...")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Tiempo: $timerValue seg")
        Spacer(modifier = Modifier.height(16.dp))
        // opciones
        currentQuestion?.answers?.forEachIndexed { index, answer ->
            Button(
                onClick = { answerQuestion(index) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(answer)
            }
        }
    }
}

@Composable
fun FScreen3(navigateToMainMenu: () -> Unit) {
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
fun Screen3(
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
fun FScreen4(navigateBack: () -> Unit) {
    val viewModel = viewModel { SettingsViewModel() } // Usa viewModel() para obtener la instancia

    Screen4(
        navigateBack = {
            viewModel.saveSettings() // Guarda los ajustes antes de navegar
            navigateBack()
        },
        currentDifficulty = viewModel.difficulty,
        currentRounds = viewModel.totalRounds,
        currentTime = viewModel.gameTimeSeconds,
        onDifficultyChanged = { viewModel.updateDifficulty(it) },
        onRoundsChanged = { viewModel.updateTotalRounds(it) },
        onTimeChanged = { viewModel.updateGameTime(it) }
    )
}

@Composable
fun Screen4(
    navigateBack: () -> Unit,
    currentDifficulty: String,
    currentRounds: Int,
    currentTime: Int,
    onDifficultyChanged: (String) -> Unit,
    onRoundsChanged: (Int) -> Unit,
    onTimeChanged: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFf80000))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ajustes", color = Color.White)
        Spacer(modifier = Modifier.height(24.dp))

        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
            Button(onClick = { expanded = true }) {
                Text(
                    text = currentDifficulty.replaceFirstChar { it.uppercase() },
                    color = Color.White
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("facil", "moderado", "dificil").forEach { difficulty ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = difficulty.replaceFirstChar { it.uppercase() },
                                color = Color.Green
                            )
                        },
                        onClick = {
                            onDifficultyChanged(difficulty)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Selector de Rondas
        Text("Rondas", color = Color.White)
        Row(modifier = Modifier.padding(8.dp)) {
            listOf(5, 10, 15).forEach { rounds ->
                Button(
                    onClick = { onRoundsChanged(rounds) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (rounds == currentRounds) Color.Green else Color.Gray // Usa containerColor
                    ),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(rounds.toString())
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Selector de Tiempo
        // Selector de Tiempo - Versión corregida
        Row(verticalAlignment = Alignment.CenterVertically) {
            Slider(
                value = currentTime.toFloat(),
                onValueChange = { onTimeChanged(it.toInt()) },
                valueRange = 5f..60f,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$currentTime s",
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = navigateBack,
            modifier = Modifier.width(200.dp)
        ) {
            Text("Guardar y Volver")
        }
    }
}



// ─────────────────────────────────────────────────────────────────────────────
// NAVEGACIÓN: Se configura el NavHost y se comparte la misma instancia del GameViewModel
@Composable
fun blaLibNavScreenSample() {
    val navController = rememberNavController()
    // Se obtiene o crea la instancia compartida del viewmodel
    val gameViewModel: GameViewModel = viewModel()

    NavHost(navController = navController, startDestination = Destination.Screen1.toString()) {
        composable(Destination.Screen1.toString()) {
            Screen1(
                navigateToScreen2 = { navController.navigate(Destination.Screen2.toString()) },
                navigateToScreen4 = { navController.navigate(Destination.Screen4.toString()) },

                )
        }
        composable(Destination.Screen2.toString()) {
            FScreen2(
                navigateToScore = { navController.navigate(Destination.Screen3.toString()) },

                )
        }
        composable(Destination.Screen3.toString()) {
            FScreen3(
                navigateToMainMenu = {
                    // Vuelve al menú principal
                    navController.popBackStack(Destination.Screen1.toString(), false)
                },

                )
        }
        composable(Destination.Screen4.toString()) {
            FScreen4(
                navigateBack = { navController.navigate(Destination.Screen1.toString()) },
            )
        }
    }
}