package cat.itb.m78.exercices.theme.State

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun sayhi() {
    Column (modifier = Modifier.padding(20.dp),horizontalAlignment = Alignment.CenterHorizontally) {

        val textState = remember { mutableStateOf("") }
        OutlinedTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            label = { Text("Name") },
            placeholder = { Text("") },
            singleLine = true
        )
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)

            },
        ){ padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize() // Ocupa todo el espacio disponible
                    .padding(padding), // Respeta el padding del Scaffold
                horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente
            ) {
                Button(modifier = Modifier.padding(padding), onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Hello!" +" "+textState.value)
                    }
                }) {
                    Text("Say hello")
                }
            }
        }
      }
    }