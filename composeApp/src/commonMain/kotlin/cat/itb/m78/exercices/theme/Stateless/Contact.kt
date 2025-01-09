package cat.itb.m78.exercices.theme.Stateless

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import m78exercices.composeapp.generated.resources.Res
import m78exercices.composeapp.generated.resources.karenCara
import org.jetbrains.compose.resources.painterResource

data class Contact(val fullName: String, val email: String, val phone: String)
val contact = Contact("Marta Casserres", "marta@example.com", "934578484")

@Composable
fun RoundedIconWithImage() {
    Box(
        modifier = Modifier
            .size(80.dp) // Tamaño del ícono
            .clip(CircleShape) // Forma redonda
            .background(Color.LightGray), // Fondo del ícono
        contentAlignment = Alignment.Center // Centrar contenido
    ) {
        Image(
            painter = painterResource(Res.drawable.karenCara), // Reemplaza con tu recurso de imagen
            contentDescription = "Rounded Icon Image",
            modifier = Modifier
                .fillMaxSize() // Llenar el espacio del ícono
                .clip(CircleShape), // Asegurar que también esté redondeado
            contentScale = ContentScale.Crop // Ajustar la imagen para llenar el círculo
        )
    }
}
@Composable
fun contact(){
    Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RoundedIconWithImage()
            Text(contact.fullName)
        }
        Column(modifier = Modifier.padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card {
                Text(text = contact.email)
                Text(text = contact.phone)
            }
        }
    }
}