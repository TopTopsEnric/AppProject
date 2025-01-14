package cat.itb.m78.exercices.theme.State

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import cat.itb.m78.exercices.theme.Stateless.contact

@Composable
fun Goodday() {
    Column (modifier = Modifier.padding(20.dp)){
        var texto = remember { mutableStateOf("Good?!") }
        Text(texto.value)
        Button(onClick = {
            texto.value = "Good Morning!"
        }) {
            Text("Morning")
        }
        Button(onClick = {
            texto.value = "Good Night"
        }) {
            Text("Night")
        }
    }

}