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
class SmokeTests : SetupTests() {

  @Test
  fun `should fetch todo list`() = runTest {
    val response = service.getTodos()
    assertEquals(200, response.status.value)
  }

  @Test
  fun `should create a new todo note`() = runTest {
    val response = service.postTodos(Todo(id = 1, text = "wash dishes", completed = false))
    assertEquals(201, response.status.value)
  }

}
