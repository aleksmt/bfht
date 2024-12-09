package com.bfht.tests.performance

import com.bfht.tests.SetupTests
import commons.models.Todo
import commons.models.TodosList
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

/**
 * This is Todos Manager Load test suite
 */
class PerformanceTests : SetupTests() {
  @Test
  fun `should be able to serve 10 rps`() = runTest {

  }

  @Test
  fun `should be able to serve 100 rps`() = runTest {

  }

}
