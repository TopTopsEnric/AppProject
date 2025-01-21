package cat.itb.m78.exercices.theme.ViewModel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class Shoplist: ViewModel() {
    // Lista dinámica para almacenar los datos
    val columnas = mutableStateListOf<String>()

    // Estados para los campos de entrada
    val itemName = mutableStateOf("")



    // Agregar una nueva columna a la lista
    fun agregarColumna() {
        if (itemName.value.isNotBlank()) {
            columnas.add("${itemName.value}")
            // Limpiar los campos después de agregar
            itemName.value = ""
        }
    }

}