package cat.itb.m78.exercices.theme.State

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import m78exercices.composeapp.generated.resources.Res
import m78exercices.composeapp.generated.resources.tapestry
import m78exercices.composeapp.generated.resources.title

import org.jetbrains.compose.resources.painterResource

@Composable
fun rollDice(){
    Box(
        modifier = Modifier
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(Res.drawable.tapestry), // Reemplaza con tu recurso
            contentDescription = "Imagen de fondo",
            contentScale = ContentScale.Crop, // Ajusta la escala de la imagen
            modifier = Modifier.fillMaxSize() // Asegura que la imagen ocupe todo el tamaño
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            Image(
                painter = painterResource(Res.drawable.title),
             contentDescription = null
         )
         Button(onClick = {

            }) {
            Text("Roll Dices")
            }
        }
}   }