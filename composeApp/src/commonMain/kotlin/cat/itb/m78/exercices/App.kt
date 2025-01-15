package cat.itb.m78.exercices


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cat.itb.m78.exercices.theme.AppTheme
import cat.itb.m78.exercices.theme.State.Goodday
import cat.itb.m78.exercices.theme.State.misterynumber
import cat.itb.m78.exercices.theme.State.rollDice
import cat.itb.m78.exercices.theme.State.sayhi
import cat.itb.m78.exercices.theme.Stateless.HelloWorld
import cat.itb.m78.exercices.theme.Stateless.Welcome
import cat.itb.m78.exercices.theme.Stateless.contact
import cat.itb.m78.exercices.theme.Stateless.resources
import org.jetbrains.compose.reload.DevelopmentEntryPoint

@Composable
internal fun App() = AppTheme {
    rollDice()
}
