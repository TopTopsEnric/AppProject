package cat.itb.m78.exercices.theme.Stateless

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import m78exercices.composeapp.generated.resources.Res
import m78exercices.composeapp.generated.resources.frase_imagen
import m78exercices.composeapp.generated.resources.karenCara
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun resources(){
    Column{
        val stringValue = stringResource(Res.string.frase_imagen)
        Text(stringValue)
        Image(
            painter = painterResource(Res.drawable.karenCara),
            contentDescription = null
        )
    }
}