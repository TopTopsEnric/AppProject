package cat.itb.m78.exercices.theme.ViewModel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun anotarequipo(){
    Column(){
        val viewModel = viewModel { Score() }
        var equipo1 = mutableStateOf("0")
        var equipo2 = mutableStateOf("0")
        Row(){
            Text(equipo1.value)

            Text(equipo2.value)
        }
        Row(){
            Button(onClick = {
                viewModel.Scoreequipo1()
               equipo1.value= viewModel.equipo1.value.toString()
            }) {
                Text("Score")
            }

            Button(onClick = {
                viewModel.Scoreequipo1()
                equipo2.value= viewModel.equipo2.value.toString()
            }) {
                Text("Score")
            }
        }
    }
}