package cat.itb.m78.exercices.theme.ApiDatabase




import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cat.itb.m78.exercices.db.SelectAll
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import kotlinx.serialization.Serializable



object Destination {
    @Serializable
    data object Screen1

    @Serializable
    data object Screen2

}

@Composable
fun NasaItemButton(
    item: SelectAll,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = item.img_src,
                contentDescription = item.full_name,
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop)




            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = item.full_name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Rover: ${item.rover_name}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
//Pantalla 1
@Composable
fun FSearchScreen(onItemClick: (Long) -> Unit) {
    val viewModel = viewModel { SearchViewModel() }

    SearchScreen(
        searchQuery = viewModel.searchQuery.collectAsState().value,
        filteredList = viewModel.filteredList.collectAsState().value,
        showFavorites = viewModel.showFavorites.collectAsState().value,
        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
        onToggleListType = { viewModel.toggleListType() },
        onItemClick = onItemClick
    )
}

@Composable
fun SearchScreen(
    searchQuery: String,
    filteredList: List<SelectAll>,
    showFavorites: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onToggleListType: () -> Unit,
    onItemClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de elementos
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(filteredList) { item ->
                NasaItemButton(
                    item = item,
                    onClick = { onItemClick(item.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones para cambiar entre listas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { if (showFavorites) onToggleListType() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!showFavorites) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Lista General")
            }

            Button(
                onClick = { if (!showFavorites) onToggleListType() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (showFavorites) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Favoritos")
            }
        }
    }
}

//Pantalla 2

@Composable
fun FDetailScreen(itemId: Long, navigateBack: () -> Unit) {
    val viewModel = viewModel { ShowViewModel() }


    // Cargar el elemento cuando se inicia la pantalla
    LaunchedEffect(itemId) {
        viewModel.loadItemById(itemId)
    }

    DetailScreen(
        uiState = viewModel.uiState.collectAsState().value,
        selectedItem = viewModel.selectedItem.collectAsState().value,
        isInFavorites = viewModel.isInFavorites.collectAsState().value,
        operationMessage = viewModel.operationMessage.collectAsState().value,
        onToggleFavorite = { viewModel.toggleFavorite() },
        onBackClick = navigateBack
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: ShowViewModel.UiState,
    selectedItem: SelectAll?,
    isInFavorites: Boolean,
    operationMessage: String?,
    onToggleFavorite: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isInFavorites) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isInFavorites) "Quitar de favoritos" else "Añadir a favoritos",
                            tint = if (isInFavorites) Color.Red else Color.Gray
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is ShowViewModel.UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is ShowViewModel.UiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = Color.Red,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                is ShowViewModel.UiState.Success -> {
                    selectedItem?.let { item ->
                        NasaItemDetail(item = item)
                    }
                }
            }

            // Mostrar mensaje de operación
            operationMessage?.let { message ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(text = message)
                }
            }
        }
    }
}

@Composable
fun NasaItemDetail(item: SelectAll) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Imagen principal
        AsyncImage(
            model = item.img_src,
            contentDescription = item.full_name,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Información del elemento
        Text(
            text = item.full_name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        DetailRow(title = "ID", value = "${item.id}")
        DetailRow(title = "Fecha Terrestre", value = item.earth_date)
        DetailRow(title = "Sol", value = "${item.sol}")

        Spacer(modifier = Modifier.height(16.dp))

        // Información del Rover
        Text(
            text = "Información del Rover",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        DetailRow(title = "Nombre", value = item.rover_name)
        DetailRow(title = "Estado", value = item.status)
        DetailRow(title = "Fecha de Lanzamiento", value = item.launch_date)
        DetailRow(title = "Fecha de Aterrizaje", value = item.landing_date)

        Spacer(modifier = Modifier.height(16.dp))

        // Información de la Cámara
        Text(
            text = "Información de la Cámara",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        DetailRow(title = "Nombre", value = item.camera_name)
        DetailRow(title = "Nombre Completo", value = item.full_name)
    }
}

@Composable
fun DetailRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$title: ",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}



@Composable
fun NasaAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Destination.Screen1.toString()) {
        composable(Destination.Screen1.toString()) {
            FSearchScreen(
                onItemClick = { itemId ->
                    navController.navigate("${Destination.Screen2}/$itemId")
                }
            )
        }

        composable(
            route = "${Destination.Screen2}/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.LongType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getLong("itemId") ?: 0L

            FDetailScreen(
                itemId = itemId,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun NasaApp() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NasaAppNavigation()
        }
    }
}



