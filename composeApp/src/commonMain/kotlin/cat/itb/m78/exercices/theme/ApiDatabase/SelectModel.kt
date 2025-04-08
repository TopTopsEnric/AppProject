package cat.itb.m78.exercices.theme.ApiDatabase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.itb.m78.exercices.database
import cat.itb.m78.exercices.db.SelectAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class ShowViewModel : ViewModel() {
    // Estados para la UI
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    // Estado para el elemento NASA seleccionado
    private val _selectedItem = MutableStateFlow<SelectAll?>(null)
    val selectedItem = _selectedItem.asStateFlow()

    // Estado para mensajes de operaciones
    private val _operationMessage = MutableStateFlow<String?>(null)
    val operationMessage = _operationMessage.asStateFlow()

    // Estado para indicar si el elemento está en favoritos
    private val _isInFavorites = MutableStateFlow(false)
    val isInFavorites = _isInFavorites.asStateFlow()

    // Función para cargar un elemento por ID
    fun loadItemById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiState.Loading

            try {
                // Obtener todos los elementos de la API
                val allItems = NasaApi.listNasa()

                // Buscar el elemento con el ID correspondiente
                val item = allItems.find { it.id == id }

                if (item != null) {
                    _selectedItem.value = item
                    _uiState.value = UiState.Success

                    // Verificar si el elemento está en favoritos
                    checkIfInFavorites(id)
                } else {
                    _uiState.value = UiState.Error("No se encontró el elemento con ID: $id")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al cargar datos: ${e.message ?: "Error desconocido"}")
            }
        }
    }

    // Verificar si el elemento está en favoritos
    private fun checkIfInFavorites(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val count = database.myDatabaseQueries
                    .countPhotoById(id)
                    .executeAsOne()

                _isInFavorites.value = count > 0
            } catch (e: Exception) {
                // Si hay error al verificar, asumimos que no está en favoritos
                _isInFavorites.value = false
            }
        }
    }

    // Función para guardar el elemento en la base de datos
    fun saveToDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val item = _selectedItem.value

            if (item == null) {
                _operationMessage.value = "No hay elemento para guardar"
                return@launch
            }

            try {
                // Primero verificamos si el rover ya existe
                val roverExists = database.myDatabaseQueries
                    .countRoverById(item.rover_id)
                    .executeAsOne() > 0

                // Si no existe, lo insertamos
                if (!roverExists) {
                    database.myDatabaseQueries.insertRover(
                        id = item.rover_id,
                        name = item.rover_name,
                        landing_date = item.landing_date,
                        launch_date = item.launch_date,
                        status = item.status
                    )
                }

                // Verificamos si la cámara ya existe
                val cameraExists = database.myDatabaseQueries
                    .countCameraById(item.camera_id)
                    .executeAsOne() > 0

                // Si no existe, la insertamos
                if (!cameraExists) {
                    database.myDatabaseQueries.insertCamera(
                        id = item.camera_id,
                        name = item.camera_name,
                        rover_id = item.rover_id,
                        full_name = item.full_name
                    )
                }

                // Verificamos si la foto ya existe
                val photoExists = database.myDatabaseQueries
                    .countPhotoById(item.id)
                    .executeAsOne() > 0

                if (!photoExists) {
                    // Insertamos la foto
                    database.myDatabaseQueries.insertNasaPhoto(
                        id = item.id,
                        sol = item.sol,
                        camera_id = item.camera_id,
                        img_src = item.img_src,
                        earth_date = item.earth_date,
                        rover_id = item.rover_id
                    )
                    _operationMessage.value = "Elemento guardado correctamente"
                    _isInFavorites.value = true
                } else {
                    _operationMessage.value = "Este elemento ya existe en favoritos"
                }
            } catch (e: Exception) {
                _operationMessage.value = "Error al guardar: ${e.message ?: "Error desconocido"}"
            }

            // Limpiar el mensaje después de 3 segundos
            delay(3000)
            _operationMessage.value = null
        }
    }

    // Función para eliminar el elemento de la base de datos
    fun deleteFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val item = _selectedItem.value

            if (item == null) {
                _operationMessage.value = "No hay elemento para eliminar"
                return@launch
            }

            try {
                // Verificamos si la foto existe antes de eliminar
                val photoExists = database.myDatabaseQueries
                    .countPhotoById(item.id)
                    .executeAsOne() > 0

                if (photoExists) {
                    // Eliminamos la foto
                    database.myDatabaseQueries.deleteNasaPhoto(item.id)

                    // Verificamos si hay otras fotos que usen esta cámara
                    val otherPhotosWithCamera = database.myDatabaseQueries
                        .countPhotosByCameraId(item.camera_id)
                        .executeAsOne()

                    // Si no hay otras fotos con esta cámara, la eliminamos
                    if (otherPhotosWithCamera == 0L) {
                        database.myDatabaseQueries.deleteCamera(item.camera_id)

                        // Verificamos si hay otras cámaras que usen este rover
                        val otherCamerasWithRover = database.myDatabaseQueries
                            .countCamerasByRoverId(item.rover_id)
                            .executeAsOne()

                        // Si no hay otras cámaras con este rover, lo eliminamos
                        if (otherCamerasWithRover == 0L) {
                            database.myDatabaseQueries.deleteRover(item.rover_id)
                        }
                    }

                    _operationMessage.value = "Elemento eliminado correctamente"
                    _isInFavorites.value = false
                } else {
                    _operationMessage.value = "Este elemento no está en favoritos"
                }
            } catch (e: Exception) {
                _operationMessage.value = "Error al eliminar: ${e.message ?: "Error desconocido"}"
            }

            // Limpiar el mensaje después de 3 segundos
            delay(3000)
            _operationMessage.value = null
        }
    }

    // Función para alternar entre guardar y eliminar
    fun toggleFavorite() {
        if (_isInFavorites.value) {
            deleteFromDatabase()
        } else {
            saveToDatabase()
        }
    }

    // Función para limpiar el mensaje de operación
    fun clearOperationMessage() {
        _operationMessage.value = null
    }

    // Estados de la UI
    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }
}
