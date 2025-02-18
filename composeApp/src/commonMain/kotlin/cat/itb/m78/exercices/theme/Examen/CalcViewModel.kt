package cat.itb.m78.exercices.theme.Examen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cat.itb.m78.exercices.theme.Trivial.Question
import cat.itb.m78.exercices.theme.Trivial.TrivialSettings
import cat.itb.m78.exercices.theme.Trivial.TrivialSettingsManager

class CalculadoraViewModel : ViewModel() {
    val valor = mutableStateOf("")
    val amount = mutableStateOf(0)
    val tipo_operacion=mutableStateOf(0)
    var finalScore by mutableStateOf(ValorFinalManager.get().final_score)

    fun saveSettings() {
        val newvalorFinal = valorFinal(
            final_score= amount.value
        )
        ValorFinalManager.update(newvalorFinal)
    }
    fun cambiar_op(newnumber: Int){
        tipo_operacion.value=newnumber
    }

    fun Suma(newnumber: Int) {
        amount.value += newnumber
    }
    fun resta(newnumber: Int) {
        amount.value -= newnumber
    }
    fun multia(newnumber: Int) {
        amount.value *= newnumber
    }
    fun divi(newnumber: Int) {
        amount.value /= newnumber
    }
    fun changeAmount(newAmount: String) {
        valor.value = newAmount
    }

    fun calculadora(){
        val numero=valor.value.toInt()
        when (tipo_operacion.value) {
            1 -> Suma(numero)
            2 -> resta(numero)
            3 -> multia(numero)
            4 -> divi(numero)
            else -> { // Note the block
                print("Funcion erronea")
            }
        }
    }
}