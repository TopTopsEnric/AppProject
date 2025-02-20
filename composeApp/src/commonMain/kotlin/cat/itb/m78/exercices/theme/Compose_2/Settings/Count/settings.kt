package cat.itb.m78.exercices.theme.Compose_2.Settings.Count

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.itb.m78.exercices.theme.Trivial.TrivialSettings
import cat.itb.m78.exercices.theme.Trivial.TrivialSettingsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class MyData(
    var contador:Int =0
)

data object MyApi{
    private var settings = MyData()
    fun update(newSettings: MyData){
        settings = newSettings
    }
    fun get() = settings
}
class MyContadorModel() : ViewModel(){
    val data = mutableStateOf<MyData?>(null)
    var contadore = data.value?.contador ?: 0
    init{
        viewModelScope.launch(Dispatchers.Default){
            data.value = MyApi.get()
            contadore++
        }
        fun saveSettings() {
            val newSettings = MyData(
                contador = contadore
            )
            MyApi.update(newSettings)
        }
    }

}