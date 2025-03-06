package cat.itb.m78.exercices.theme.Compose_2.Api


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlin.random.Random

@Serializable
data class jokes(
    val setup : String,
    val punchline: String
)

suspend fun getjoke(): jokes {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    val chistes: List<jokes> = client.get("https://api.sampleapis.com/jokes/goodJokes").body()
    val defiJoke = chistes[Random.nextInt(0, chistes.size)]

    return jokes(defiJoke.setup, defiJoke.punchline)
}

@Composable
fun JokeScreen() {
    val jokeState = produceState<jokes?>(initialValue = null) {
        value = getjoke()
    }

    if (jokeState.value == null) {
        // Muestra el indicador de carga mientras se obtiene el chiste
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Screen2(jokeState.value!!)
    }
}

@Composable
fun Screen2(
    Joke: jokes,
) {
    Text(text = Joke.setup)
    Text(text = Joke.punchline)
}


