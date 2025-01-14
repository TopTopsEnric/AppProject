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

enum class ValueResult{
    GREATER, LOWER, SAME, NOT_VALID;

    fun toUiString(): String {
        return when(this){
            GREATER ->  "El número es mayor"
            LOWER -> "El número es menor"
            SAME -> "Has acertat."
            NOT_VALID -> "No se encontró ningún número en el texto."
        }
    }
}



fun checkValue(input: String,numeroRandom:Int): ValueResult {
    val number = Regex("\\d+").find(input)?.value?.toInt()
    return if (number != null) {
        if (number > numeroRandom) {
            ValueResult.GREATER
        } else if (numeroRandom < number) {
            ValueResult.LOWER
        } else {
            ValueResult.SAME
        }
    } else {
        ValueResult.NOT_VALID
    }
}



@Composable
fun misterynumber() {
    var contador=0;
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
            val result = checkValue(textState.value,numeroRandom)
            if(result!=ValueResult.SAME){
                 contador++;
            }
            //val resultados = calculador(textState.value,numeroRandom)
            texto.value= contador.toString()
            frase.value= result.toUiString()

        }) {
            Text("Validar")
        }
        Text("intents:"+" "+ texto.value)
        Text(frase.value)
    }

    }