package cat.itb.m78.exercices.theme.ViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun AnotarEquipo() {
    val viewModel: Score = viewModel()

    // Observamos los estados directamente del ViewModel
    val equipo1 = viewModel.equipo1.value
    val equipo2 = viewModel.equipo2.value

    Column {
        // Mostrar los puntajes
        Row {
            Text(text = "Equipo 1: $equipo1")
            Spacer(modifier = Modifier.width(16.dp)) // Espaciado entre textos
            Text(text = "Equipo 2: $equipo2")
        }

        Spacer(modifier = Modifier.height(16.dp)) // Espaciado entre filas

        // Botones para anotar puntos
        Row {
            Button(onClick = { viewModel.Scoreequipo1() }) {
                Text("Anotar Equipo 1")
            }
            Spacer(modifier = Modifier.width(16.dp)) // Espaciado entre botones
            Button(onClick = { viewModel.Scoreequipo2() }) {
                Text("Anotar Equipo 2")
            }
        }
    }
}