package cat.itb.m78.exercices.theme.State

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random


fun calculador(input: String,numeroRandom:Int): Pair<String, Int> {
    val number = Regex("\\d+").find(input)?.value?.toInt()

    // Contador para las veces que no se cumple la igualdad
    var contador = 0

    // Evaluamos el número extraído
    val resultado = if (number != null) {
        if (number > numeroRandom) {
            "El número es mayor"
        } else if (numeroRandom < number) {
            "El número es menor"
        } else {
            "Has acertat."
        }.also {
            // Incrementamos el contador si no es igual
            if (number != numeroRandom) contador++
        }
    } else {
        "No se encontró ningún número en el texto."
    }

    // Devolvemos la frase y el contador
    return Pair(resultado, contador)
}

@Composable
fun misterynumber() {
    val numeroRandom = Random.nextInt(0, 101)
    Column(modifier = Modifier.padding(20.dp)) {
        Text("Esdevina el número Secret")
        val textState = remember { mutableStateOf("") }
        OutlinedTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            label = { Text("") },
            placeholder = { Text("") },
            singleLine = true
        )
        var texto = remember { mutableStateOf("0") }
        var frase = remember { mutableStateOf("") }
        Button(onClick = {
            val resultados = calculador(textState.value,numeroRandom)
            texto.value= resultados.first
            frase.value=resultados.second.toString()

        }) {
            Text("Validar")
        }
        Text("intents:"+" "+ texto.value)
        Text(frase.value)
    }

    }