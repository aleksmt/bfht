package com.bfht.tests.todos

import com.bfht.tests.SetupTests
import commons.models.Todo
import commons.models.TodosList
import io.ktor.client.call.body
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

/**
 * This is Todos Manager SMOKE test suite
 */
class ComplexTests : SetupTests() {
  @Test
  fun `should be able to delete a todo note`() = runTest {
    service.postTodos(Todo(id = 2, text = "make cookies", completed = false))
    val responseDelete = service.deleteTodos(2)
    assertEquals(204, responseDelete.status.value)

    val responseGet: TodosList = service.getTodos().body()

  }

  @Test
  fun `should be able to modify a todo note`() = runTest {
    val response = service.postTodos(Todo(id = 1, text = "wash dishes", completed = false))
    assertEquals(201, response.status.value)
  }
}
