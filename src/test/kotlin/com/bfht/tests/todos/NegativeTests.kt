package com.bfht.tests.todos

import com.bfht.tests.SetupTests
import commons.models.Todo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

/**
 * This is Todos Manager NEGATIVE test suite
 */
class NegativeTests : SetupTests() {

    @Test
    fun `add todo without payload = error`() = runTest {
        val response = service.postTodos(Todo(id = null))
        assertEquals(400, response.status.value)
    }

    @Test
    fun `modify todo without payload = error`() = runTest {
        val response = service.putTodos(Todo())
        assertEquals(400, response.status.value)
    }

    @Test
    fun `wrong basic auth = error`() = runTest {
        val todo = todo()
        service.postTodos(todo)

        val response = service.deleteTodos(todo.id, "wrong", "wrong")
        assertEquals(401, response.status.value)
    }

}
