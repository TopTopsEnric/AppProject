package cat.itb.m78.exercices.theme.Examen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cat.itb.m78.exercices.theme.Trivial.*
import cat.itb.m78.exercices.theme.Trivial.Destination
import kotlinx.serialization.Serializable
import m78exercices.composeapp.generated.resources.Calculator_icon
import m78exercices.composeapp.generated.resources.Res
import m78exercices.composeapp.generated.resources.trivial
import org.jetbrains.compose.resources.painterResource

object Destination {
    @Serializable
    data object Screen1

    @Serializable
    data object Screen2
}


@Composable
fun ExaScreen2(navigateToScore: () -> Unit) {
    val viewModel = viewModel { CalculadoraViewModel() }
    Scream2(
        amount = viewModel.valor.value,
        valor= viewModel.amount.value,
        navigateToScore,
        onOperationChanged={viewModel.cambiar_op(it)},
        changeAmount={viewModel.changeAmount(it)},
        Calculate=viewModel:: calculadora,
        setearValor=viewModel::saveSettings
    )
}

@Composable
fun Scream2(
    amount: String,
    valor:Int,
    navigateToScore: () -> Unit,
    onOperationChanged: (Int) -> Unit,
    changeAmount: (String) -> Unit,
    Calculate: () -> Unit,
    setearValor:()-> Unit,

) {
        Box(modifier = Modifier.wrapContentSize(Alignment.Center).padding(16.dp),) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFf80000))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ){
                Text(text = "$valor")
                Row(modifier = Modifier.padding(8.dp)){
                    Button(
                        onClick = { onOperationChanged(1) },

                        // Usa containerColor

                    ) {
                        Text("+")
                    }
                    Button(
                        onClick = { onOperationChanged(2) },
                    ) {
                        Text("-")
                    }
                    Button(
                        onClick = { onOperationChanged(3) },
                    ) {
                        Text("*")
                    }
                    Button(
                        onClick = {onOperationChanged(4) },
                    ) {
                        Text("/")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Card(modifier = Modifier.padding(8.dp)){
                    Column() {
                        OutlinedTextField(amount, changeAmount,
                            label = { Text("") })
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(modifier = Modifier.padding(8.dp)){
                            Button(
                                onClick = {
                                    setearValor()
                                    navigateToScore()
                                },

                                ) {
                                Text("Final Result")
                            }
                            Button(
                                onClick = { Calculate() },

                                ) {
                                Text("Calculate")
                            }

                        }
                    }
                }
            }


        }

}

@Composable
fun ExaScreen3(navigateToMainMenu: () -> Unit) {
    val viewModel = viewModel { ResultadoViewModel() }
    Screan3(
        navigateToMainMenu = {
            navigateToMainMenu()
        },
        score = viewModel.finalScore
    )
}

@Composable
fun Screan3(
    navigateToMainMenu: () -> Unit,
    score: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Te final result is",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "$score",
            style = MaterialTheme.typography.headlineMedium
        )
        Image(
            painter = painterResource(Res.drawable.Calculator_icon),
            contentDescription = "Imagen de fondo",
            contentScale = ContentScale.Crop, // Ajusta la escala de la imagen
            modifier = Modifier.size(200.dp)
        )
    }
}


@Composable
fun controladorNavExamen() {
    val navController = rememberNavController()
    // Se obtiene o crea la instancia compartida del viewmodel

    NavHost(navController = navController, startDestination = Destination.Screen2.toString()) {

        composable(Destination.Screen2.toString()) {
            cat.itb.m78.exercices.theme.Examen.ExaScreen2(
                navigateToScore = { navController.navigate(Destination.Screen3.toString()) },

                )
        }
        composable(Destination.Screen3.toString()) {
            cat.itb.m78.exercices.theme.Examen.ExaScreen3(
                navigateToMainMenu = {
                    // Vuelve al menú principal
                    navController.popBackStack(Destination.Screen2.toString(), false)
                },

                )
        }

    }
}