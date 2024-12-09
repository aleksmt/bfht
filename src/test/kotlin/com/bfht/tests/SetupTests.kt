package com.bfht.tests

import commons.http.TodoManagerServiceClient
import commons.http.TodoManagerWebSocketClient
import commons.models.Todo
import commons.models.TodosList
import io.ktor.client.call.body
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlinx.coroutines.runBlocking

/**
 * Global setup/teardown and shareable methods
 */
open class SetupTests {

  protected val service = TodoManagerServiceClient()
  protected val ws = TodoManagerWebSocketClient()

  protected val todoNotes = mapOf(
    "swim" to Todo(id = 1, text = "go swim", completed = false),
    "workout" to Todo(id = 2, text = "go workout", completed = true),
    "store" to Todo(id = 3, text = "go to store", completed = true),
    "cleanup" to Todo(id = 4, text = "cleanup a room", completed = false),
    "sing" to Todo(id = 5, text = "sing in a bathroom", completed = true),
    "feed" to Todo(id = 6, text = "feed a hamster", completed = false),
    "cookies" to Todo(id = 7, text = "make cookies", completed = true)
  )

  @BeforeTest
  fun setUp(): Unit = runBlocking {
    val todoList: TodosList = service.getTodos().body()
    todoList.forEach { todo -> service.deleteTodos(todo.id) }
  }

  protected fun todo(): Todo {
    val rand = Random.nextInt(todoNotes.size)
    return todoNotes.entries.elementAt(rand).value
  }

}
