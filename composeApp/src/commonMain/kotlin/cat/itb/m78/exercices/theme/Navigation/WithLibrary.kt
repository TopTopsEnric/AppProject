package cat.itb.m78.exercices.theme.Navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

object Destination {
    @kotlinx.serialization.Serializable
    data object Screenlib0
    @kotlinx.serialization.Serializable
    data object Screenlib1
    @kotlinx.serialization.Serializable
    data object Screenlib2
    @Serializable
    data class Screenlib3(val message: String)
}
@Composable
fun Screenlib0(navigateToScreen1: () -> Unit) {
    Column {
        Text(text = "Screen1")
        Button(onClick = navigateToScreen1) {
            Text("Go to Screen2")
        }
    }
}

@Composable
fun Screenlib1(navigateToScreen1: () -> Unit) {
    Column {
        Text(text = "Screen1")
        Button(onClick = navigateToScreen1) {
            Text("Go to Screen2")
        }
    }
}
@Composable
fun Screenlib2(navigateToScreen2: () -> Unit) {
    Column {
        Text(text = "Screen1")
        Button(onClick = navigateToScreen2) {
            Text("Go to Screen2")
        }
    }
}
@Composable
fun Screenlib3(navigateToScreen2: () -> Unit) {
    Column {
        Text(text = "Screen1")
        Button(onClick = navigateToScreen2) {
            Text("Go to Screen2")
        }
    }
}
@Composable
fun Screenlib4(navigateToScreen2: () -> Unit) {
    Column {
        Text(text = "Screen1")
        Button(onClick = navigateToScreen2) {
            Text("Go to Screen2")
        }
    }
}

@Composable
fun LibNavScreenSample() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Destination.Screenlib0) {
        composable<Destination.Screenlib1> {
            Screen1(
                navigateToScreen2 = { navController.navigate(Destination.Screenlib0) },
            )
        }
        composable<Destination.Screenlib2> {
            Screen2 { navController.navigate(Destination.Screenlib0) }
        }
        composable<Destination.Screenlib3> { backStack ->
            val message = backStack.toRoute<Destination.Screenlib3>().message
            Screen3(message)
        }
    }
}