package cat.itb.m78.exercices


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.cash.sqldelight.db.SqlDriver
import cat.itb.m78.exercices.db.MyDatabase
import cat.itb.m78.exercices.theme.ApiDatabase.Destination
import cat.itb.m78.exercices.theme.ApiDatabase.FSearchScreen
import cat.itb.m78.exercices.theme.ApiDatabase.NasaApp
import cat.itb.m78.exercices.theme.ApiDatabase.NasaAppNavigation
import cat.itb.m78.exercices.theme.AppTheme
import cat.itb.m78.exercices.theme.Compose_2.Api.JokeScreen
import cat.itb.m78.exercices.theme.Examen.controladorNavExamen
import cat.itb.m78.exercices.theme.Jueguito.jueguito
import cat.itb.m78.exercices.theme.Navigation.LibNavScreenSample
import cat.itb.m78.exercices.theme.State.Goodday
import cat.itb.m78.exercices.theme.State.misterynumber
import cat.itb.m78.exercices.theme.State.rollDice
import cat.itb.m78.exercices.theme.State.sayhi
import cat.itb.m78.exercices.theme.Stateless.HelloWorld
import cat.itb.m78.exercices.theme.Stateless.Welcome
import cat.itb.m78.exercices.theme.Stateless.contact
import cat.itb.m78.exercices.theme.Stateless.resources
import cat.itb.m78.exercices.theme.Trivial.blaLibNavScreenSample
import cat.itb.m78.exercices.theme.ViewModel.AnotarEquipo

import org.jetbrains.compose.reload.DevelopmentEntryPoint
expect fun createDriver(): SqlDriver
fun createDatabase(): MyDatabase {
    val driver = createDriver()
    return MyDatabase(driver)
}
val database by lazy { createDatabase() }


@Composable
internal fun App() = AppTheme {
    NasaApp()
}
