package commons.http

import commons.config.ConfigManager
import commons.models.Todo
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.util.encodeBase64

class TodoManagerServiceClient(
  private val client: HttpClient = HttpClientFactory.create(),
) {

  private val todosEndpoint = ConfigManager.apiUrl
  private val wsEndpoint = ConfigManager.wsUrl
  private val username = ConfigManager.username
  private val password = ConfigManager.password

  suspend fun getTodos(offset: Int = -1, limit: Int = -1): HttpResponse {
    return client.get(todosEndpoint)
  }

  suspend fun postTodos(payload: Todo): HttpResponse {
    return client.post(todosEndpoint) {
      contentType(ContentType.Application.Json)
      setBody(payload)
    }
  }

  suspend fun putTodos(id: String): HttpResponse {
    return client.put("${todosEndpoint}/$id")
  }

  suspend fun retrieveWsEvents(id: String): HttpResponse {
    return client.put("${wsEndpoint}/$id")
  }

  suspend fun deleteTodos(id: Int, username: String? = null, password: String? = null): HttpResponse {
    val credentials: String = if (username != null && password != null) {
      "${username}:${password}"
    } else {
      "${this.username}:${this.password}"
    }

    return client.delete("${todosEndpoint}/$id") {
      header(HttpHeaders.Authorization, "Basic ${credentials.encodeBase64()}")
    }
  }

}

