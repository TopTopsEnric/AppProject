package cat.itb.m78.exercices.theme.Trivial

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random


// ─────────────────────────────────────────────────────────────────────────────
// DATA CLASS PARA LAS PREGUNTAS
data class Question(
    val questionText: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)
data class ShareDates(
    var final_score: Int =0
)
data object sharedatesManager{
    private var dates = TrivialSettings()
    fun update(newSettings: TrivialSettings){
        dates = newSettings
    }
    fun get() = dates
}

// ─────────────────────────────────────────────────────────────────────────────
// GAME VIEWMODEL: CONTIENE TANTO LA LÓGICA DEL JUEGO COMO LOS AJUSTES

class GameViewModel : ViewModel() {
    // Estados del juego
    var selectedDifficulty by mutableStateOf(TrivialSettingsManager.get().difficulty)
    var currentRound by mutableStateOf(1)
    var score by mutableStateOf(0)
    var currentQuestion: Question? by mutableStateOf(null)
    var timerValue by mutableStateOf(0)
    var gameFinished by mutableStateOf(false)


    private var timerJob: Job? = null
    private val usedQuestions = mutableListOf<Int>()

    // Score final separado
    var finalScore by mutableStateOf(TrivialSettingsManager.get().final_score)


    private val questionsEasy = List(20) { index ->
        Question(
            questionText = when (index) {
                0 -> "Pregunta fácil 1: ¿Cuál es la capital de Francia?"
                1 -> "Pregunta fácil 2: ¿Cuánto es 2 + 2?"
                2 -> "Pregunta fácil 3: ¿Cuál es el color del cielo en un día despejado?"
                3 -> "Pregunta fácil 4: ¿Qué animal dice 'miau'?"
                4 -> "Pregunta fácil 5: ¿Cuál es el planeta más cercano al Sol?"
                5 -> "Pregunta fácil 6: ¿Cuántos lados tiene un triángulo?"
                6 -> "Pregunta fácil 7: ¿De qué color son las hojas en primavera?"
                7 -> "Pregunta fácil 8: ¿Cuál es el océano más grande del mundo?"
                8 -> "Pregunta fácil 9: ¿Qué gas respiramos para vivir?"
                9 -> "Pregunta fácil 10: ¿Qué objeto usamos para escribir en un cuaderno?"
                10 -> "Pregunta fácil 11: ¿Cómo se llama el satélite natural de la Tierra?"
                11 -> "Pregunta fácil 12: ¿Cuántas patas tiene una araña?"
                12 -> "Pregunta fácil 13: ¿Cuál es el metal principal de las monedas de 1 euro?"
                13 -> "Pregunta fácil 14: ¿Qué instrumento tiene teclas blancas y negras?"
                14 -> "Pregunta fácil 15: ¿Qué gas hace que los globos floten?"
                15 -> "Pregunta fácil 16: ¿Cuál es el animal más rápido del mundo?"
                16 -> "Pregunta fácil 17: ¿Qué sentido nos permite oler?"
                17 -> "Pregunta fácil 18: ¿Qué planeta es conocido como el 'planeta rojo'?"
                18 -> "Pregunta fácil 19: ¿Cuál es el número romano para el 10?"
                19 -> "Pregunta fácil 20: ¿Qué animal es famoso por su trompa larga?"
                else -> ""
            },
            answers = when (index) {
                0 -> listOf("París", "Roma", "Madrid", "Berlín") // Correcta: París
                1 -> listOf("3", "4", "5", "6") // Correcta: 4
                2 -> listOf("Rojo", "Azul", "Verde", "Amarillo") // Correcta: Azul
                3 -> listOf("Perro", "Gato", "Pez", "Conejo") // Correcta: Gato
                4 -> listOf("Venus", "Tierra", "Marte", "Mercurio") // Correcta: Mercurio
                5 -> listOf("Cuadrado", "Triángulo", "Círculo", "Pentágono") // Correcta: Triángulo
                6 -> listOf("Rojas", "Amarillas", "Verdes", "Azules") // Correcta: Verdes
                7 -> listOf("Atlántico", "Pacífico", "Índico", "Ártico") // Correcta: Pacífico
                8 -> listOf("Oxígeno", "Hidrógeno", "Dióxido de carbono", "Helio") // Correcta: Oxígeno
                9 -> listOf("Lápiz", "Regla", "Goma", "Pegamento") // Correcta: Lápiz
                10 -> listOf("Luna", "Sol", "Marte", "Saturno") // Correcta: Luna
                11 -> listOf("6", "8", "10", "12") // Correcta: 8
                12 -> listOf("Cobre", "Oro", "Níquel", "Plata") // Correcta: Níquel
                13 -> listOf("Guitarra", "Piano", "Flauta", "Violín") // Correcta: Piano
                14 -> listOf("Nitrógeno", "Oxígeno", "Helio", "Dióxido de carbono") // Correcta: Helio
                15 -> listOf("León", "Guepardo", "Caballo", "Tigre") // Correcta: Guepardo
                16 -> listOf("Vista", "Olfato", "Gusto", "Tacto") // Correcta: Olfato
                17 -> listOf("Júpiter", "Marte", "Saturno", "Venus") // Correcta: Marte
                18 -> listOf("V", "X", "IX", "XI") // Correcta: X
                19 -> listOf("León", "Caballo", "Elefante", "Jirafa") // Correcta: Elefante
                else -> listOf()
            },
            correctAnswerIndex = when (index) {
                0, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 -> 0
                1, 2, 3, 4, 5, 6, 9 -> 1
                else -> 0
            }
        )
    }

    private val questionsModerate = List(20) { index ->
        Question(
            questionText = when (index) {
                0 -> "Pregunta moderada 1: ¿Cuál es el metal principal en la sangre humana?"
                1 -> "Pregunta moderada 2: ¿Qué famoso científico desarrolló la teoría de la relatividad?"
                2 -> "Pregunta moderada 3: ¿En qué año llegó el hombre a la Luna por primera vez?"
                3 -> "Pregunta moderada 4: ¿Cuál es el elemento químico con el símbolo 'Na'?"
                4 -> "Pregunta moderada 5: ¿Qué tipo de animal es un ornitorrinco?"
                5 -> "Pregunta moderada 6: ¿Quién escribió 'Don Quijote de la Mancha'?"
                6 -> "Pregunta moderada 7: ¿Cuál es el idioma más hablado en el mundo?"
                7 -> "Pregunta moderada 8: ¿En qué continente se encuentra el río Amazonas?"
                8 -> "Pregunta moderada 9: ¿Cuál es el número atómico del oxígeno?"
                9 -> "Pregunta moderada 10: ¿Cuál es la capital de Canadá?"
                10 -> "Pregunta moderada 11: ¿Qué invento revolucionó la impresión en el siglo XV?"
                11 -> "Pregunta moderada 12: ¿Quién pintó la Capilla Sixtina?"
                12 -> "Pregunta moderada 13: ¿Qué gas es el principal responsable del efecto invernadero?"
                13 -> "Pregunta moderada 14: ¿Qué es más grande, un virus o una bacteria?"
                14 -> "Pregunta moderada 15: ¿En qué año comenzó la Segunda Guerra Mundial?"
                15 -> "Pregunta moderada 16: ¿Cuál es el desierto más grande del mundo?"
                16 -> "Pregunta moderada 17: ¿Cuál es la unidad básica de la vida?"
                17 -> "Pregunta moderada 18: ¿Quién fue el primer emperador de Roma?"
                18 -> "Pregunta moderada 19: ¿Cuál es el órgano más grande del cuerpo humano?"
                19 -> "Pregunta moderada 20: ¿Qué famoso físico formuló las leyes del movimiento?"
                else -> ""
            },
            answers = when (index) {
                0 -> listOf("Hierro", "Cobre", "Calcio", "Potasio") // Correcta: Hierro
                1 -> listOf("Newton", "Einstein", "Galileo", "Tesla") // Correcta: Einstein
                2 -> listOf("1965", "1969", "1972", "1958") // Correcta: 1969
                3 -> listOf("Níquel", "Sodio", "Neón", "Nitrógeno") // Correcta: Sodio
                4 -> listOf("Mamífero", "Reptil", "Ave", "Anfibio") // Correcta: Mamífero
                5 -> listOf("Miguel de Cervantes", "Shakespeare", "Pablo Neruda", "Gabriel García Márquez") // Correcta: Miguel de Cervantes
                6 -> listOf("Inglés", "Chino", "Español", "Hindi") // Correcta: Chino
                7 -> listOf("África", "Asia", "América del Sur", "Europa") // Correcta: América del Sur
                8 -> listOf("6", "8", "10", "12") // Correcta: 8
                9 -> listOf("Toronto", "Ottawa", "Vancouver", "Montreal") // Correcta: Ottawa
                10 -> listOf("Imprenta", "Máquina de escribir", "Teléfono", "Televisión") // Correcta: Imprenta
                11 -> listOf("Da Vinci", "Miguel Ángel", "Van Gogh", "Rembrandt") // Correcta: Miguel Ángel
                12 -> listOf("Dióxido de carbono", "Metano", "Oxígeno", "Argón") // Correcta: Dióxido de carbono
                13 -> listOf("Virus", "Bacteria", "Son del mismo tamaño", "Depende del tipo") // Correcta: Bacteria
                14 -> listOf("1914", "1939", "1945", "1929") // Correcta: 1939
                15 -> listOf("Sahara", "Gobi", "Antártico", "Kalahari") // Correcta: Antártico
                16 -> listOf("Átomo", "Célula", "Molécula", "Orgánulo") // Correcta: Célula
                17 -> listOf("Julio César", "Augusto", "Nerón", "Trajano") // Correcta: Augusto
                18 -> listOf("Hígado", "Piel", "Cerebro", "Corazón") // Correcta: Piel
                19 -> listOf("Newton", "Einstein", "Kepler", "Galileo") // Correcta: Newton
                else -> listOf()
            },
            correctAnswerIndex = when (index) {
                0, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 -> 0
                1, 2, 3, 4 -> 1
                else -> 0
            }
        )
    }

    private val questionsHard = List(20) { index ->
        Question(
            questionText = when (index) {
                0 -> "Pregunta difícil 1: ¿Cuál es el número atómico del uranio?"
                1 -> "Pregunta difícil 2: ¿En qué año cayó Constantinopla en manos del Imperio Otomano?"
                2 -> "Pregunta difícil 3: ¿Cuál es la ecuación de Schrödinger utilizada para describir?"
                3 -> "Pregunta difícil 4: ¿Quién escribió 'Crítica de la razón pura'?"
                4 -> "Pregunta difícil 5: ¿Cuál es la única sustancia que puede encontrarse en los tres estados de la materia de forma natural en la Tierra?"
                5 -> "Pregunta difícil 6: ¿Qué teorema establece que 'ningún sistema lógico consistente puede demostrarse completamente a sí mismo'?"
                6 -> "Pregunta difícil 7: ¿Quién descubrió la penicilina?"
                7 -> "Pregunta difícil 8: ¿Cuál es el segundo elemento más abundante en la corteza terrestre?"
                8 -> "Pregunta difícil 9: ¿Qué emperador romano legalizó el cristianismo?"
                9 -> "Pregunta difícil 10: ¿Cuál es la galaxia más cercana a la Vía Láctea?"
                10 -> "Pregunta difícil 11: ¿Qué famoso matemático formuló la hipótesis de Riemann?"
                11 -> "Pregunta difícil 12: ¿Cómo se llama el agujero más profundo excavado por el ser humano?"
                12 -> "Pregunta difícil 13: ¿Cuál es la capital de Bután?"
                13 -> "Pregunta difícil 14: ¿Qué es un 'bosón de Higgs' y por qué es importante?"
                14 -> "Pregunta difícil 15: ¿Cuál es el radio aproximado de la Tierra en kilómetros?"
                15 -> "Pregunta difícil 16: ¿Qué físico introdujo el concepto de dualidad onda-partícula?"
                16 -> "Pregunta difícil 17: ¿Cuál es el único mamífero capaz de volar?"
                17 -> "Pregunta difícil 18: ¿Cuál es el idioma oficial de Etiopía?"
                18 -> "Pregunta difícil 19: ¿Qué océano es el más profundo del planeta?"
                19 -> "Pregunta difícil 20: ¿Cuál fue la primera civilización conocida en desarrollar la escritura?"
                else -> ""
            },
            answers = when (index) {
                0 -> listOf("92", "86", "104", "78") // Correcta: 92
                1 -> listOf("1453", "1492", "1517", "1321") // Correcta: 1453
                2 -> listOf("E = mc²", "ψ = Hψ", "F = ma", "ΔE = hν") // Correcta: ψ = Hψ
                3 -> listOf("Immanuel Kant", "Descartes", "Hegel", "Nietzsche") // Correcta: Immanuel Kant
                4 -> listOf("Oxígeno", "Agua", "Dióxido de carbono", "Nitrógeno") // Correcta: Agua
                5 -> listOf("Teorema de Gödel", "Principio de incertidumbre", "Ley de Murphy", "Paradoja de Fermi") // Correcta: Teorema de Gödel
                6 -> listOf("Alexander Fleming", "Louis Pasteur", "Robert Koch", "Marie Curie") // Correcta: Alexander Fleming
                7 -> listOf("Hierro", "Silicio", "Magnesio", "Aluminio") // Correcta: Silicio
                8 -> listOf("Julio César", "Constantino", "Nerón", "Augusto") // Correcta: Constantino
                9 -> listOf("Andrómeda", "Sagitario A*", "Messier 87", "Triángulo") // Correcta: Andrómeda
                10 -> listOf("Euler", "Gauss", "Riemann", "Fermat") // Correcta: Riemann
                11 -> listOf("Pozo Kola", "Gran Agujero Azul", "Chicxulub", "Trinchera de las Marianas") // Correcta: Pozo Kola
                12 -> listOf("Thimphu", "Katmandú", "Ulan Bator", "Vientiane") // Correcta: Thimphu
                13 -> listOf("Una partícula subatómica", "Un tipo de estrella", "Una forma de energía oscura", "Un mineral raro") // Correcta: Una partícula subatómica
                14 -> listOf("6,371 km", "4,500 km", "10,200 km", "8,350 km") // Correcta: 6,371 km
                15 -> listOf("Albert Einstein", "Max Planck", "Louis de Broglie", "Richard Feynman") // Correcta: Louis de Broglie
                16 -> listOf("Murciélago", "Ardilla voladora", "Colugo", "Pteropus") // Correcta: Murciélago
                17 -> listOf("Árabe", "Amhárico", "Suajili", "Zulú") // Correcta: Amhárico
                18 -> listOf("Índico", "Atlántico", "Pacífico", "Ártico") // Correcta: Pacífico
                19 -> listOf("Egipcios", "Sumerios", "Mayas", "Fenicios") // Correcta: Sumerios
                else -> listOf()
            },
            correctAnswerIndex = when (index) {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 -> 0
                else -> 0
            }
        )
    }

    // Selección de preguntas según dificultad
    private val currentQuestions: List<Question>
        get() = when (selectedDifficulty.trim().lowercase()) {
            "moderado" -> questionsModerate
            "dificil" -> questionsHard
            else -> questionsEasy
        }

    init {
        initializeGame() // Cambiamos resetGame() por initializeGame()
    }
    // Nuevo método para la inicialización inicial
    private fun initializeGame() {
        currentRound = 1
        score = 0
        gameFinished = false
        usedQuestions.clear()
        loadQuestion()
    }


    private fun resetGame() {
        finalScore = 0 // Solo reseteamos el score final aquí
        initializeGame()
    }

    fun generarIndiceUnico(): Int {
        if (usedQuestions.size >= currentQuestions.size) {
            usedQuestions.clear()
        }

        var indice: Int
        do {
            indice = Random.nextInt(currentQuestions.size)
        } while (usedQuestions.contains(indice))

        usedQuestions.add(indice)
        return indice
    }

    fun loadQuestion() {
        timerJob?.cancel()

        val randomIndex = generarIndiceUnico()
        currentQuestion = currentQuestions[randomIndex]
        startTimer()
    }

    fun answerQuestion(answerIndex: Int) {
        currentQuestion?.let { question ->
            if (answerIndex == question.correctAnswerIndex) {
                score++
            }
        }
        moveToNextRound()
    }

    private fun moveToNextRound() {
        if (currentRound < TrivialSettingsManager.get().questionsPerGame) {
            currentRound++
            loadQuestion()
        } else {
            timerJob?.cancel()
            finalScore = score // Guardamos el score final

            gameFinished = true
        }
    }

    fun onTimerFinished() {
        moveToNextRound()
    }

    private fun startTimer() {
        timerValue = TrivialSettingsManager.get().tiempoRonda

        timerJob = viewModelScope.launch {
            while (timerValue > 0) {
                delay(1000L)
                if (!isActive) break
                timerValue--
            }
            if (isActive) onTimerFinished()
        }
    }

    fun resetForNewGame() {
        resetGame()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }


}
