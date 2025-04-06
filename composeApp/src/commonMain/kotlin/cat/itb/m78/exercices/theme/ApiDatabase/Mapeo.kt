package cat.itb.m78.exercices.theme.ApiDatabase

import cat.itb.m78.exercices.db.Camera
import cat.itb.m78.exercices.db.Nasa_photo
import cat.itb.m78.exercices.db.Rover


fun Nasa_photo.toNasa(camera: Camera, rover: Rover): Nasa {
    return Nasa(
        id = id.toInt(),
        sol = sol.toInt(),
        camera = camera.toSerializableCamera(),
        img_src = img_src,
        earth_date = earth_date,
        rover = rover.toSerializableRover()
    )
}

fun Camera.toSerializableCamera(): Camera_Api {
    return Camera_Api(
        id = id.toInt(),
        name = name,
        rover_id = rover_id.toInt(),
        full_name = full_name
    )
}

fun Rover.toSerializableRover(): Rover_Api {
    return Rover_Api(
        id = id.toInt(),
        name = name,
        landing_date = landing_date,
        launch_date = launch_date,
        status = status
    )
}