package commons.http

import commons.Config
import commons.models.Todo
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
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

    private val todosEndpoint = Config.todoUrl
    private val username = Config.username
    private val password = Config.password

    /**
     * Fetch TODOs, url params: offset, limit
     * Json example:
     *    [{
     *         "id": 1,
     *         "text": "Wash dishes",
     *         "completed": true
     *     },
     *     {
     *         "id": 2,
     *         "text": "Workout",
     *         "completed": false
     *     }]
     */
    suspend fun getTodos(offset: Int? = null, limit: Int? = null): HttpResponse {
        return client.get(todosEndpoint) {
            url {
                if (offset != null) parameter("offset", offset.toString())
                if (limit != null) parameter("limit", limit.toString())
            }
        }
    }

    /**
     * Create TODOs
     * Json example:
     *  {
     *     "id": 3,
     *     "text": "Wash dishes",
     *     "completed": false
     * }
     */
    suspend fun postTodos(payload: Todo?): HttpResponse {
        return client.post(todosEndpoint) {
            contentType(ContentType.Application.Json)
            setBody(payload)
        }
    }

    /**
     * Update TODOs, url param: id
     * Json example:
     *  {
     *     "id": 3,
     *     "text": "Wash dishes",
     *     "completed": false
     * }
     */
    suspend fun putTodos(payload: Todo?, username: String? = null, password: String? = null): HttpResponse {
        val credentials: String = if (username != null && password != null) {
            "${username}:${password}"
        } else {
            "${this.username}:${this.password}"
        }

        return client.put("${todosEndpoint}/${payload?.id}") {
            header(HttpHeaders.Authorization, "Basic ${credentials.encodeBase64()}")
            contentType(ContentType.Application.Json)
            setBody(payload)
        }
    }

    /**
     * Remove TODOs, url param: id
     */
    suspend fun deleteTodos(id: Int?, username: String? = null, password: String? = null): HttpResponse {
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

