package cat.itb.m78.exercices.theme.ApiDatabase

import app.cash.sqldelight.db.SqlDriver
import cat.itb.m78.exercices.db.MyDatabase

expect fun createDriver(): SqlDriver
fun createDatabase(): MyDatabase {
    val driver = createDriver()
    return MyDatabase(driver)
}
val database by lazy { createDatabase() }