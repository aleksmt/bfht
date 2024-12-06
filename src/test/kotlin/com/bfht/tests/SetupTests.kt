package com.bfht.tests

import commons.http.TodoManagerServiceClient
import commons.models.TodosList
import io.ktor.client.call.body
import kotlin.test.BeforeTest
import kotlinx.coroutines.runBlocking

/**
 * Global setup/teardown
 */
open class SetupTests {

  protected val service = TodoManagerServiceClient()

  @BeforeTest
  fun setUp(): Unit = runBlocking {
    val todoList: TodosList = service.getTodos().body()
    todoList.forEach { todo ->
      service.deleteTodos(todo.id)
    }
  }

}
