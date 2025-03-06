package cat.itb.m78.exercices.theme.Compose_2.Api

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Country(
    val name: String,
    val capital: String,
    val flag: String
)

suspend fun getCountries(): List<Country> {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    return client.get("https://api.sampleapis.com/countries/countries").body()
}

@Composable
fun CountryScreen() {
    val countryState = produceState<List<Country>?>(initialValue = null) {
        value = getCountries()
    }

    if (countryState.value == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Screen3(paises = countryState.value!!)
    }
}

@Composable
fun Screen3(paises: List<Country>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(paises) { pais ->
            Row(modifier = Modifier.padding(8.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = pais.name)
                    Text(text = pais.capital)
                }
                Image(
                    painter = rememberAsyncImagePainter(pais.flag),
                    contentDescription = "Imagen de bandera",
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}