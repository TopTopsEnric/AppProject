package cat.itb.m78.exercices.theme.ViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun listaCompra(){
    val viewModel: Shoplist = viewModel()
    val cantidades: Cantidad = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Entrada de datos
        Column(
            modifier = Modifier
                .background(color = Color(0xFFe2e2e2))
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Campo para el nombre del producto
            OutlinedTextField(
                value = viewModel.itemName.value,
                onValueChange = { viewModel.itemName.value = it },
                label = { Text("Nombre") },
                placeholder = { Text("") },
                singleLine = true
            )

            // Campo para la cantidad
           /* OutlinedTextField(
                value = cantidades.itemQuantity.value,
                onValueChange = { cantidades.itemQuantity.value = it },
                label = { Text("Cantidad") },
                placeholder = { Text("") },
                singleLine = true,
            )*/

            // Botón para añadir el producto
            Button(onClick = { viewModel.agregarColumna() }) {
                Text("Add")
            }
        }

        // Lista de productos
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            viewModel.columnas.forEach { columna ->
                // Separar las palabras de la columna por espacios en blanco
                val palabras = columna.split(" ")

                // Asegurarse de que siempre haya exactamente dos palabras

                    Row(
                        modifier = Modifier
                            .background(color = Color(0xFFe2e2e2))
                            .padding(top = 5.dp, start = 5.dp, end = 5.dp, bottom = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = palabras[0], // Primera palabra
                            modifier = Modifier.padding(25.dp)
                        )
                        Text(
                            text = palabras[2], // Segunda palabra
                            modifier = Modifier.padding(25.dp)
                        )
                    }

            }
        }
    }
}