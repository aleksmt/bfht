package com.bfht.tests.todos

import com.bfht.tests.SetupTests
import commons.models.TodosList
import io.ktor.client.call.body
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

/**
 * This is Todos Manager GET test suite
 */
class GetTodoTests : SetupTests() {

    @Test
    fun `should fetch todo list`() = runTest {
        val response = service.getTodos()
        assertEquals(200, response.status.value)
    }

    @Test
    fun `created and received todos should be equal`() = runTest {
        todoNotes.forEach { (_, v) ->
            val response = service.postTodos(v)
            assertEquals(201, response.status.value)
        }

        val todosListResponse: TodosList = service.getTodos().body()
        assertEquals(todoNotes.values.toList(), todosListResponse)
    }

    @Test
    fun `should be able to apply 'offset' on received notes`() = runTest {
        todoNotes.forEach { (_, v) ->
            val response = service.postTodos(v)
            assertEquals(201, response.status.value)
        }

        val todosListResponse: TodosList = service.getTodos(offset = 4).body()
        assertEquals(
            listOf(
                todoNotes["sing"], todoNotes["feed"], todoNotes["cookies"]
            ), todosListResponse
        )
    }

    @Test
    fun `should be able to apply 'limit' on received notes`() = runTest {
        todoNotes.forEach { (_, v) ->
            val response = service.postTodos(v)
            assertEquals(201, response.status.value)
        }

        val todosListResponse: TodosList = service.getTodos(limit = 1).body()
        assertEquals(listOf(todoNotes["swim"]), todosListResponse)
    }

    @Test
    fun `should be able to apply 'limit & offset' on received notes`() = runTest {
        todoNotes.forEach { (_, v) ->
            val response = service.postTodos(v)
            assertEquals(201, response.status.value)
        }

        val todosListResponse: TodosList = service.getTodos(offset = 3, limit = 2).body()
        assertEquals(listOf(todoNotes["cleanup"], todoNotes["sing"]), todosListResponse)
    }

    @Test
    fun `get todo with wrong params = error`() = runTest {
        val response = service.getTodos(-222, -222)
        assertEquals(400, response.status.value)
    }
}
