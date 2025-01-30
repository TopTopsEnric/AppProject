package cat.itb.m78.exercices.theme.Jueguito

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import cat.itb.m78.exercices.theme.Navigation.Destination
import kotlinx.serialization.Serializable

fun List<List<Boolean?>>.toMutableMatrix(): List<MutableList<Boolean?>> {
    return map { it.toMutableList() }
}
fun tres(){
    val matriz = listOf(
        listOf(null, null, null,null,null,null,null,null,null)
    ).toMutableMatrix()




}

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

) {
    Column {
        Button(onClick = navigateToScreen2) {
            Text("Start")
        }
    }
}

@Composable
fun Screen2(navigateToScreen3: () -> Unit) {
    Column {
        Row{
            Button(onClick = navigateToScreen3) {
                Text("")
            }
            Button(onClick = navigateToScreen3) {
                Text("")
            }
            Button(onClick = navigateToScreen3) {
                Text("")
            }
        }
        Row{
            Button(onClick = navigateToScreen3) {
                Text("")
            }
            Button(onClick = navigateToScreen3) {
                Text("")
            }
            Button(onClick = navigateToScreen3) {
                Text("")
            }
        }
        Row{
            Button(onClick = navigateToScreen3) {
                Text("")
            }
            Button(onClick = navigateToScreen3) {
                Text("")
            }
            Button(onClick = navigateToScreen3) {
                Text("")
            }
        }


    }
}

@Composable
fun Screen3(navigateToScreen2: () -> Unit) {
    Column {
        Text(text = "Screen 3")
        Button(onClick = navigateToScreen2) {
            Text("Main Menu")
        }
    }
}



@Composable
fun jueguito() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Destination.Screen1.toString()) {
        composable(Destination.Screen1.toString()) {
            Screen1(
                navigateToScreen2 = { navController.navigate(Destination.Screen2.toString()) },
            )
            Screen2(
                navigateToScreen3 = { navController.navigate(Destination.Screen3.toString()) },
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
            //Screen3(message) { navController.popBackStack() }
        }
    }
}