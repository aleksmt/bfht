package commons.http

import commons.config.ConfigManager
import commons.models.WsTodo
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import java.util.concurrent.ConcurrentLinkedQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class TodoManagerWebSocketClient(
  private val ws: HttpClient = WsClientFactory.create(),
) {

  private val receivedMessages = ConcurrentLinkedQueue<String>()
  private var listenerJob: Job? = null

  /**
   * Performs initial listen to messages
   */
  fun listen() {
    listenerJob = CoroutineScope(Dispatchers.IO).launch {
      ws.webSocket(
        method = HttpMethod.Get,
        host = ConfigManager.wsHost,
        port = 8080,
        path = "/ws"
      ) {
        for (frame in incoming) {
          if (frame is Frame.Text) {
            val message = frame.readText()
            synchronized(receivedMessages) {
              receivedMessages.add(message)
            }
          }
        }
      }
    }
  }

  /**
   * Waits for particular count of messages
   */
  suspend fun awaitMessages(expected: Int): List<WsTodo> {
    withContext(Dispatchers.IO) {
      while (true) {
        synchronized(receivedMessages) {
          if (receivedMessages.size >= expected) {
            return@withContext
          }
        }
        delay(10)
      }
    }
    return synchronized(receivedMessages) {
      receivedMessages.map { Json.decodeFromString<WsTodo>(it) }
    }
  }

  /**
   * Closes listener and dispose opened resources
   */
  fun stopListening() {
    listenerJob?.cancel()
  }

}

