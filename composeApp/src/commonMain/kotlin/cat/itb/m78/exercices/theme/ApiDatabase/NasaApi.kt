package cat.itb.m78.exercices.theme.ApiDatabase
import cat.itb.m78.exercices.database
import cat.itb.m78.exercices.db.Camera
import cat.itb.m78.exercices.db.Rover
import cat.itb.m78.exercices.db.SelectAll
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json



@Serializable
data class Nasa(
    val id: Int,
    val sol: Int,
    val camera: Camera_Api,
    val img_src: String,
    val earth_date: String,
    val rover: Rover_Api,
)

@Serializable
data class Camera_Api(
    val id: Int,
    val name: String,
    val rover_id: Int,
    val full_name: String,
)

@Serializable
data class Rover_Api(
    val id: Int,
    val name: String,
    val landing_date: String,
    val launch_date: String,
    val status: String,
)



object NasaApi {
    private val url = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=Zlp0XblvQ2XPJlBLYOtwmCHfW0XrmvdJJRnA9CPS"
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    @Serializable
    data class NasaApiResponse(
        val photos: List<Nasa>
    )

    suspend fun listNasa(): List<SelectAll> {
        val response = client.get(url).body<NasaApiResponse>()


            val nasaList = response.photos.map { nasa ->
                SelectAll(
                    id = nasa.id.toLong(),
                    sol = nasa.sol.toLong(),
                    camera_id = nasa.camera.id.toLong(),
                    img_src = nasa.img_src,
                    earth_date = nasa.earth_date,
                    rover_id = nasa.rover.id.toLong(),
                    camera_name = nasa.camera.name,
                    full_name = nasa.camera.full_name,
                    rover_name = nasa.rover.name,
                    landing_date = nasa.rover.landing_date,
                    launch_date = nasa.rover.launch_date,
                    status = nasa.rover.status
                )
            }
            return nasaList

    }
}

