package commons.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
  fun create(): HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
      json(Json {
        ignoreUnknownKeys = true
        prettyPrint = true
      })
    }
  }
}
