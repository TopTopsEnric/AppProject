package cat.itb.m78.exercices.theme.Navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import kotlinx.serialization.Serializable

// Objeto Destination con data objects y data class
object Destination {
    @Serializable
    data object Screen1

    @Serializable
    data object Screen2

    @Serializable
    data class Screen3(val message: String)

    @Serializable
    data object Screen4
}

@Composable
fun Screen1(
    navigateToScreen2: () -> Unit,
    navigateToScreen3: (String) -> Unit,
    navigateToScreen4: () -> Unit
) {
    Column {
        Button(onClick = navigateToScreen2) {
            Text("Screen1")
        }
        Button(onClick = navigateToScreen4) {
            Text("Screen2")
        }
        Button(onClick = { navigateToScreen3("¡Hola!") }) {
            Text("Screen3")
        }
    }
}

@Composable
fun Screen2(navigateBack: () -> Unit) {
    Column {
        Text(text = "Screen 1")
        Button(onClick = navigateBack) {
            Text("Main Menu")
        }
    }
}

@Composable
fun Screen3(message: String, navigateBack: () -> Unit) {
    Column {
        Text(text = "Screen 3")
        Text(text = "$message")
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
                navigateToScreen3 = { message -> navController.navigate(Destination.Screen3(message).toString()) },
                navigateToScreen4 = { navController.navigate(Destination.Screen4.toString()) }
            )
        }
        composable(Destination.Screen2.toString()) {
            Screen2 { navController.popBackStack() }
        }
        composable(
            route = Destination.Screen3("{message}").toString(),
            arguments = listOf(navArgument("message") { type = NavType.StringType })
        ) { backStackEntry ->
            val message = backStackEntry.arguments?.getString("message") ?: "Mensaje vacío"
            Screen3(message) { navController.popBackStack() }
        }
        composable(Destination.Screen4.toString()) {
            Screen4 { navController.popBackStack() }
        }
    }
}