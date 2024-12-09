package com.bfht.tests.todos

import com.bfht.tests.SetupTests
import commons.models.Todo
import commons.models.TodosList
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

/**
 * This is Todos Manager PUT test suite
 */
class PutTodoTests : SetupTests() {

    @Test
    fun `should modify a todo note`() = runTest {
        var response: HttpResponse?
        val todoNotes = mapOf(
            "initial" to Todo(id = 88, text = "go swim", completed = false),
            "modified" to Todo(id = 88, text = "go workout", completed = true),
        )
        response = service.postTodos(todoNotes["initial"])
        assertEquals(201, response.status.value)

        response = service.putTodos(todoNotes["modified"])
        assertEquals(200, response.status.value)

        val todosListResponse: TodosList = service.getTodos().body()
        assertEquals(listOf(todoNotes["modified"]), todosListResponse)
    }

    @Test
    fun `should be able to mark note completed`() = runTest {
        var response: HttpResponse?
        val todoNotes = mapOf(
            "not_completed" to Todo(id = 5, text = "cleanup room", completed = false),
            "completed" to Todo(id = 5, text = "cleanup room", completed = true),
        )
        response = service.postTodos(todoNotes["not_completed"])
        assertEquals(201, response.status.value)

        response = service.putTodos(todoNotes["completed"])
        assertEquals(200, response.status.value)

        val todosListResponse: TodosList = service.getTodos().body()
        assertEquals(listOf(todoNotes["completed"]), todosListResponse)
    }

    @Test
    fun `update todo with identical data`() = runTest {
        var response: HttpResponse?
        val todoNote = Todo(id = 12, text = "read a book", completed = false)

        response = service.postTodos(todoNote)
        assertEquals(201, response.status.value)

        response = service.putTodos(todoNote)
        assertEquals(200, response.status.value)

        val todosListResponse: TodosList = service.getTodos().body()
        assertEquals(listOf(todoNote), todosListResponse)
    }

    @Test
    fun `modify todo without payload = error`() = runTest {
        val response = service.putTodos(Todo())
        assertEquals(400, response.status.value)
    }

    @Test
    fun `modify missing todo = error`() = runTest {
        val todoNote = Todo(id = 999, text = "no such task", completed = true)
        val response = service.putTodos(todoNote)

        assertEquals(404, response.status.value)
    }
}
