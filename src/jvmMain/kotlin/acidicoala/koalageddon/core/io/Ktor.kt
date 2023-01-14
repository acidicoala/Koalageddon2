package acidicoala.koalageddon.core.io

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

val httpClient = HttpClient {
    expectSuccess = true
    install(HttpTimeout)
    install(ContentNegotiation) {
        json(appJson)
    }
}