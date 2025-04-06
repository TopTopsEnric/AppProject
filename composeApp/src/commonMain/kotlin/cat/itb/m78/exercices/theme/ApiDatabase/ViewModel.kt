package cat.itb.m78.exercices.theme.ApiDatabase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cat.itb.m78.exercices.database
import cat.itb.m78.exercices.db.SelectAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch


class ShowViewModel: ViewModel() {
    var numero: Int = 0
    var objeto: Nasa? = null

    suspend fun setearObjetoa() {
        val fotos = NasaApi.listNasa()
        if (numero in fotos.indices) {
            //objeto = fotos[numero]
        }
    }

    fun guardarFavoritos(){

    }
}


class SearchViewModel : ViewModel() {

    var wichlist =true
    var search by mutableStateOf("")
    val favoritos = database.myDatabaseQueries.selectAll().asFlow().mapToList(Dispatchers.IO)

    var mostrador =mutableStateOf<Flow<List<SelectAll>>?>(null)
    val allNasa= mutableStateOf<List<SelectAll>?>(null)
    init{
        viewModelScope.launch(Dispatchers.Default) {
            allNasa.value = NasaApi.listNasa().asFlow().toList()
        }
    }

    fun changeList() {
        if(wichlist){
            mostrador.value=favoritos
        }else{

        }
    }

    /*LaunchedEffect(mostrador.value) {
        mostrador.value?.collect { list ->
            val sortedList = list.sortedBy {
                Math.abs(it.full_name.length - search.length)
            }
            // Crea un nuevo Flow con la lista ordenada y reasígnalo a 'mostrador'
            mostrador.value = flowOf(sortedList)
        }
    }*/


}