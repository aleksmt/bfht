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
 * This is Todos Manager SMOKE test suite
 */
class SmokeTests : SetupTests() {

    @Test
    fun `should fetch todo list`() = runTest {
        val response = service.getTodos()
        assertEquals(200, response.status.value)
    }

    @Test
    fun `should create a new todo note`() = runTest {
        val response = service.postTodos(todo())
        assertEquals(201, response.status.value)
    }

    @Test
    fun `should delete a todo note`() = runTest {
        val note: Todo = todo()
        service.postTodos(note)

        val responseDelete = service.deleteTodos(note.id)
        assertEquals(204, responseDelete.status.value)

        val todosListResponse: TodosList = service.getTodos().body()
        assertEquals(listOf(), todosListResponse)
    }

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

}
