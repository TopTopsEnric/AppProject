package cat.itb.m78.exercices.theme.Trivial

// IMPORTS NECESARIOS
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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

// ─────────────────────────────────────────────────────────────────────────────
// DATA CLASS PARA LAS PREGUNTAS
data class Question(
    val questionText: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)

// ─────────────────────────────────────────────────────────────────────────────
// GAME VIEWMODEL: CONTIENE TANTO LA LÓGICA DEL JUEGO COMO LOS AJUSTES

class GameViewModel : ViewModel() {
    // ----- AJUSTES (por defecto) -----
    var totalRounds by mutableStateOf(5)
    var gameTimeSeconds by mutableStateOf(10)
    var difficulty by mutableStateOf("Easy")

    // ----- ESTADO DEL JUEGO -----
    var currentRound by mutableStateOf(1)
    var score by mutableStateOf(0)
    var currentQuestion: Question? by mutableStateOf(null)
    var timerValue by mutableStateOf(0)
    var gameFinished by mutableStateOf(false)

    // Lista de preguntas “faciles”
    private val questions = listOf(
        Question(
            questionText = "¿Cuál es la capital de Francia?",
            answers = listOf("París", "Roma", "Madrid", "Berlín"),
            correctAnswerIndex = 0
        ),
        Question(
            questionText = "¿Cuánto es 2 + 2?",
            answers = listOf("3", "4", "5", "22"),
            correctAnswerIndex = 1
        ),
        Question(
            questionText = "¿De qué color es el cielo?",
            answers = listOf("Azul", "Verde", "Rojo", "Amarillo"),
            correctAnswerIndex = 0
        ),
        Question(
            questionText = "¿Qué animal ladra?",
            answers = listOf("Gato", "Perro", "Vaca", "Oveja"),
            correctAnswerIndex = 1
        )
    )
    private var questionIndex = 0

    // Inicia o reinicia el juego
    fun startGame() {
        currentRound = 1
        score = 0
        gameFinished = false
        questionIndex = 0
        loadQuestion()
    }

    // Carga la siguiente pregunta (se recorre la lista cíclicamente)
    private fun loadQuestion() {
        currentQuestion = questions[questionIndex % questions.size]
        questionIndex++
        startTimer()
    }

    // Se invoca cuando se pulsa una respuesta
    fun answerQuestion(answerIndex: Int) {
        currentQuestion?.let { question ->
            if (answerIndex == question.correctAnswerIndex) {
                score++
            }
        }
        moveToNextRound()
    }

    // Avanza a la siguiente ronda o finaliza el juego
    private fun moveToNextRound() {
        if (currentRound < totalRounds) {
            currentRound++
            loadQuestion()
        } else {
            gameFinished = true
        }
    }

    // Si se acaba el temporizador sin contestar, se pasa a la siguiente ronda
    fun onTimerFinished() {
        moveToNextRound()
    }

    // Inicia el temporizador para cada pregunta
    private fun startTimer() {
        timerValue = gameTimeSeconds
        viewModelScope.launch {
            while (timerValue > 0) {
                delay(1000L)
                timerValue--
            }
            onTimerFinished()
        }
    }

    // ----- FUNCIONES PARA MODIFICAR LOS AJUSTES -----
    fun updateTotalRounds(rounds: Int) {
        totalRounds = rounds
    }

    fun updateGameTime(seconds: Int) {
        gameTimeSeconds = seconds
    }

    fun updateDifficulty(diff: String) {
        difficulty = diff
    }

    // Para reiniciar el juego desde la pantalla de resultados
    fun resetGame() {
        startGame()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PANTALLA 1: MENÚ PRINCIPAL
@Composable
fun Screen1(
    navigateToScreen2: () -> Unit,
    navigateToScreen4: () -> Unit,
    gameViewModel: GameViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen de fondo (reemplaza el id del recurso por el tuyo)
        Image(
            painter = painterResource(Res.drawable.),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = {
                // Inicia el juego (resetear estado, iniciar temporizador, etc.)
                gameViewModel.startGame()
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
fun Screen2(
    navigateToScore: () -> Unit,
    gameViewModel: GameViewModel
) {
    // Se observan los estados necesarios
    val currentRound = gameViewModel.currentRound
    val totalRounds = gameViewModel.totalRounds
    val currentQuestion = gameViewModel.currentQuestion
    val timerValue = gameViewModel.timerValue
    val gameFinished = gameViewModel.gameFinished

    // Si se terminó el juego, se navega automáticamente a la pantalla de resultados
    if (gameFinished) {
        LaunchedEffect(Unit) {
            navigateToScore()
        }
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
        // Se muestran los 4 botones de respuesta (o la cantidad que tenga la pregunta)
        currentQuestion?.answers?.forEachIndexed { index, answer ->
            Button(
                onClick = { gameViewModel.answerQuestion(index) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(answer)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PANTALLA 3: RESULTADO
@Composable
fun Screen3(
    navigateToMainMenu: () -> Unit,
    gameViewModel: GameViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Tu Puntuación: ${gameViewModel.score}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Reinicia el juego y vuelve al menú principal
            gameViewModel.resetGame()
            navigateToMainMenu()
        }) {
            Text("Volver al Menú")
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PANTALLA 4: AJUSTES
@Composable
fun Screen4(
    navigateBack: () -> Unit,
    gameViewModel: GameViewModel
) {

    var roundsText by remember { mutableStateOf(gameViewModel.totalRounds.toString()) }
    var timeText by remember { mutableStateOf(gameViewModel.gameTimeSeconds.toString()) }
    var difficultyText by remember { mutableStateOf(gameViewModel.difficulty) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0000FF)) // Fondo azul (ajusta el color si lo deseas)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Ajustes", color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = roundsText,
            onValueChange = { roundsText = it },
            label = { Text("Número de rondas") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = timeText,
            onValueChange = { timeText = it },
            label = { Text("Tiempo por pregunta (segundos)") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = difficultyText,
            onValueChange = { difficultyText = it },
            label = { Text("Dificultad") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Actualiza los ajustes en el viewmodel y vuelve atrás
            gameViewModel.updateTotalRounds(roundsText.toIntOrNull() ?: gameViewModel.totalRounds)
            gameViewModel.updateGameTime(timeText.toIntOrNull() ?: gameViewModel.gameTimeSeconds)
            gameViewModel.updateDifficulty(difficultyText)
            navigateBack()
        }) {
            Text("Guardar y Volver")
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// NAVEGACIÓN: Se configura el NavHost y se comparte la misma instancia del GameViewModel
@Composable
fun LibNavScreenSample() {
    val navController = rememberNavController()
    // Se obtiene o crea la instancia compartida del viewmodel
    val gameViewModel: GameViewModel = viewModel()

    NavHost(navController = navController, startDestination = Destination.Screen1.toString()) {
        composable(Destination.Screen1.toString()) {
            Screen1(
                navigateToScreen2 = { navController.navigate(Destination.Screen2.toString()) },
                navigateToScreen4 = { navController.navigate(Destination.Screen4.toString()) },
                gameViewModel = gameViewModel
            )
        }
        composable(Destination.Screen2.toString()) {
            Screen2(
                navigateToScore = { navController.navigate(Destination.Screen3.toString()) },
                gameViewModel = gameViewModel
            )
        }
        composable(Destination.Screen3.toString()) {
            Screen3(
                navigateToMainMenu = {
                    // Vuelve al menú principal
                    navController.popBackStack(Destination.Screen1.toString(), false)
                },
                gameViewModel = gameViewModel
            )
        }
        composable(Destination.Screen4.toString()) {
            Screen4(
                navigateBack = { navController.popBackStack() },
                gameViewModel = gameViewModel
            )
        }
    }
}
