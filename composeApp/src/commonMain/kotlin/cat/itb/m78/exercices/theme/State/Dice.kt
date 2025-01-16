package cat.itb.m78.exercices.theme.State

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import m78exercices.composeapp.generated.resources.*
import m78exercices.composeapp.generated.resources.Res
import m78exercices.composeapp.generated.resources.dice_1
import m78exercices.composeapp.generated.resources.tapestry
import m78exercices.composeapp.generated.resources.title
import org.jetbrains.compose.resources.imageResource

import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

fun random(): Int {
    return Random.nextInt(0, 6) // Genera un número aleatorio entre 0 y 5 (incluyendo 0 y excluyendo 6)
}


@Composable
fun rollDice() {
    val drawableResources = arrayOf(
        Res.drawable.dice_1,
        Res.drawable.dice_2,
        Res.drawable.dice_3,
        Res.drawable.dice_4,
        Res.drawable.dice_5,
        Res.drawable.dice_6
    )

    var numeroRandom1 = 0
    var numeroRandom2 = 0
    var imageResource1 by remember { mutableStateOf(Res.drawable.dice_1) }
    var imageResource2 by remember { mutableStateOf(Res.drawable.dice_1) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Imagen de fondo

        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { padding ->
            Image(
                painter = painterResource(Res.drawable.tapestry), // Reemplaza con tu recurso
                contentDescription = "Imagen de fondo",
                contentScale = ContentScale.FillBounds, // Ajusta la escala de la imagen
                modifier = Modifier.fillMaxSize().background(Color.Red) // Asegura que la imagen ocupe todo el tamaño
            )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.title),
                contentDescription = null
            )




                Button(
                    modifier = Modifier.padding(padding),
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Hello!")
                        }
                    }
                ) {
                    Text("Jackpot!!")
                }

                Button(onClick = {
                    numeroRandom1 = (0..5).random()
                    numeroRandom2 = (0..5).random()
                    imageResource1 = drawableResources[numeroRandom1]
                    imageResource2 = drawableResources[numeroRandom2]

                    if (numeroRandom1 == 5 && imageResource1 == imageResource2) {
                        // Acciones cuando ambos dados muestran 5 y son iguales
                        // Por ejemplo, lanzar un mensaje o mostrar un snackbar
                        scope.launch {
                            snackbarHostState.showSnackbar("Jackpot!!")
                        }
                    }
                }) {
                    Text("Roll Dice")
                }

                Row(horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically ) {
                    Image(
                        painter = painterResource(imageResource1), // Reemplaza con tu recurso
                        contentDescription = "Imagen de fondo",
                        contentScale = ContentScale.Crop, // Ajusta la escala de la imagen
                        modifier = Modifier.size(100.dp)
                    )

                    Image(
                        painter = painterResource(imageResource2), // Reemplaza con tu recurso
                        contentDescription = "Imagen de fondo",
                        contentScale = ContentScale.Crop, // Ajusta la escala de la imagen
                        modifier = Modifier.size(100.dp) // Asegura que la imagen ocupe todo el tamaño
                    )
                }
            }
        }
    }
}