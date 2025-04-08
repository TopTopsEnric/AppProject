package cat.itb.m78.exercices.theme.ApiDatabase


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cat.itb.m78.exercices.database
import cat.itb.m78.exercices.db.SelectAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest





class SearchViewModel : ViewModel() {
    // Cambia el valor inicial a false para mostrar la lista general primero
    private val _showFavorites = MutableStateFlow(false)
    val showFavorites = _showFavorites.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // Datos originales
    private val _allNasa = MutableStateFlow<List<SelectAll>>(emptyList())
    val allNasa = _allNasa.asStateFlow()

    // Lista filtrada para mostrar
    private val _filteredList = MutableStateFlow<List<SelectAll>>(emptyList())
    val filteredList = _filteredList.asStateFlow()

    init {
        // Cargar datos de la API inmediatamente
        loadApiData()

        // Configurar la observación de cambios
        setupDataObservers()
    }

    private fun loadApiData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiData = NasaApi.listNasa()
                _allNasa.value = apiData

                // Actualizar la lista filtrada si estamos mostrando todos los elementos
                if (!_showFavorites.value) {
                    _filteredList.value = apiData.filter { item ->
                        if (_searchQuery.value.isBlank()) true
                        else item.full_name.contains(_searchQuery.value, ignoreCase = true)
                    }
                }
            } catch (e: Exception) {
                // Manejo silencioso del error
            }
        }
    }

    private fun setupDataObservers() {
        viewModelScope.launch {
            // Observar cambios en favoritos
            database.myDatabaseQueries.selectAll().asFlow().mapToList(Dispatchers.IO)
                .collect { favoritesList ->
                    if (_showFavorites.value) {
                        _filteredList.value = favoritesList.filter { item ->
                            if (_searchQuery.value.isBlank()) true
                            else item.full_name.contains(_searchQuery.value, ignoreCase = true)
                        }
                    }
                }
        }

        // Observar cambios en la consulta de búsqueda
        viewModelScope.launch {
            _searchQuery.collect { query ->
                updateFilteredList()
            }
        }

        // Observar cambios en el tipo de lista
        viewModelScope.launch {
            _showFavorites.collect { showFavs ->
                updateFilteredList()
            }
        }
    }

    private fun updateFilteredList() {
        viewModelScope.launch {
            val query = _searchQuery.value
            val showFavs = _showFavorites.value

            val sourceList = if (showFavs) {
                database.myDatabaseQueries.selectAll().executeAsList()
            } else {
                _allNasa.value
            }

            _filteredList.value = if (query.isBlank()) {
                sourceList
            } else {
                sourceList.filter {
                    it.full_name.contains(query, ignoreCase = true)
                }
            }
        }
    }

    fun toggleListType() {
        _showFavorites.value = !_showFavorites.value
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
