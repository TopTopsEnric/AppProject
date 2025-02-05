package cat.itb.m78.exercices.theme.Trivial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.serialization.Serializable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import m78exercices.composeapp.generated.resources.Res
import m78exercices.composeapp.generated.resources.tapestry
import org.jetbrains.compose.resources.painterResource

class preguntas_sistem(){
    var pregunta = remember { mutableStateOf("") }
    var resuesta1 = remember { mutableStateOf("") }
    var resuesta2 = remember { mutableStateOf("") }
    var resuesta3 = remember { mutableStateOf("") }
    var resuesta4 = remember { mutableStateOf("") }
    val listaPre =listOf("Cual es la fruta alargada?")
    val listaRes = listOf("Manzana", "Banana", "Cereza","fresa")
}

class respostes(){
    val respostes=mutableListOf<Int>()
    fun rellenar1(){
        respostes.add(1)
    }
    fun rellenar2(){
        respostes.add(2)
    }
    fun rellenar3(){
        respostes.add(3)
    }
    fun rellenar4(){
        respostes.add(4)
    }
}
object Destination {
    @Serializable
    data object Screen1

    @Serializable
    data object Screen2

    @Serializable
    data object Screen3

    @Serializable
    data object Screen4
}

@Composable
fun Screen1(
    navigateToScreen2: () -> Unit,
    navigateToScreen4: () -> Unit
) {
    Column {
        Image(
            painter = painterResource(Res.drawable.tapestry), // Reemplaza con tu recurso
            contentDescription = "Imagen de fondo",
            contentScale = ContentScale.FillBounds, // Ajusta la escala de la imagen
            modifier = Modifier.fillMaxSize().background(Color.Red) // Asegura que la imagen ocupe todo el tamaño
        )

        Row{
            Button(onClick = navigateToScreen2) {
                Text("Screen1")
            }
            Button(onClick = navigateToScreen4) {
                Text("Screen2")
            }
        }

    }
}

@Composable
fun Screen2(navigateBack: () -> Unit) {
    Column {
        var info_ronda = remember { mutableStateOf("") }

        var pregunta = remember { mutableStateOf(preguntas_sistem().pregunta.value) }
        Row{
            Button(onClick = {}) {
                Text(preguntas_sistem().pregunta.value)
            }
            Button(onClick = {}) {
                Text("")
            }
        }
        Row{
            Button(onClick = {} ) {
                Text("")
            }
            Button(onClick = {} ) {
                Text("")
            }
        }
    }
}

@Composable
fun Screen3(navigateBack: () -> Unit) {
    Column {
        Text(text = "Your Score")
        var resultat = remember { mutableStateOf("") }
        Button(onClick = navigateBack) {
            Text("Main Menu")
        }
    }
}

@Composable
fun Screen4(navigateBack: () -> Unit) {
    Column(modifier = Modifier
        .background(color = Color(0xFF0000)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,) {
        Text(text = "Screen 2")
        Button(onClick = navigateBack) {
            Text("Main Menu")
        }
    }
}

@Composable
fun LibNavScreenSample() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Destination.Screen1.toString()) {
        composable(Destination.Screen1.toString()) {
            Screen1(
                navigateToScreen2 = { navController.navigate(Destination.Screen2.toString()) },
                navigateToScreen4 = { navController.navigate(Destination.Screen4.toString()) }
            )
        }
        composable(Destination.Screen2.toString()) {
            Screen2 { navController.popBackStack() }
        }
        composable(Destination.Screen3.toString()) {
            Screen3() { navController.popBackStack() }
        }
        composable(Destination.Screen4.toString()) {
            Screen4 { navController.popBackStack() }
        }
    }
}