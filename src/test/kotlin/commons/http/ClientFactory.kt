package commons.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


val formatter = Json {
  ignoreUnknownKeys = true
  prettyPrint = true
  explicitNulls = false
}

object HttpClientFactory {
  fun create(): HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
      json(formatter)
    }
  }
}

object WsClientFactory {
  fun create(): HttpClient = HttpClient {
    install(WebSockets)
  }
}
