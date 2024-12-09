package com.bfht.tests.todos

import com.bfht.tests.SetupTests
import commons.models.Todo
import commons.models.TodosList
import io.ktor.client.call.body
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue

/**
 * This is Todos Manager POST test suite
 */
class PostTodoTests : SetupTests() {

    @Test
    fun `should create a new todo note`() = runTest {
        val response = service.postTodos(todo())
        assertEquals(201, response.status.value)
    }

    @Test
    fun `should be able to create a new todo note and it should be consent`() = runTest {
        val todo: Todo = todo()
        service.postTodos(todo)

        val response = service.getTodos()
        assertEquals(200, response.status.value)

        val todosList = response.body<TodosList>()
        assertEquals(todo, todosList[0])
    }

    @Test
    fun `create todo with minimal data`() = runTest {
        val minimalTodo = Todo(id = 1, text = "", completed = false)

        val response = service.postTodos(minimalTodo)
        assertEquals(201, response.status.value)

        val todosList = service.getTodos().body<TodosList>()
        assertTrue(todosList.any {
            it.id == minimalTodo.id &&
            it.text == minimalTodo.text &&
            it.completed == minimalTodo.completed
        })
    }

    @Test
    fun `add todo without payload = error`() = runTest {
        val response = service.postTodos(Todo(id = null, text = null, completed = null))
        assertEquals(400, response.status.value)
    }

    @Test
    fun `create todo with duplicate ID = error`() = runTest {
        val todo = Todo(id = 10, text = "existing task", completed = false)

        val firstResponse = service.postTodos(todo)
        assertEquals(201, firstResponse.status.value)

        val duplicateResponse = service.postTodos(todo)
        assertEquals(400, duplicateResponse.status.value)
    }

    @Test
    fun `create todo with negative id = error`() = runTest {
        val minimalTodo = Todo(id = -1, text = "", completed = false)

        val response = service.postTodos(minimalTodo)
        assertEquals(400, response.status.value)
    }

}
