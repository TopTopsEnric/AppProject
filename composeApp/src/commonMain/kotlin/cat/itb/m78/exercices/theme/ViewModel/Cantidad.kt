package cat.itb.m78.exercices.theme.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class Cantidad {
    // Lista dinámica para almacenar los datos
    val cantidades = mutableStateListOf<Int>()

    // Estados para los campos de entrada

    val itemQuantity = mutableStateOf(0)


    // Agregar una nueva columna a la lista
    fun agregarColumna() {
        if (itemQuantity.value==0) {
            cantidades.add(itemQuantity.value)
            // Limpiar los campos después de agregar

            itemQuantity.value = 0
        }
    }
}