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
 * This is Todos Manager COMPLEX test suite
 */
class ComplexTests : SetupTests() {

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
  fun `should be able to delete a todo note`() = runTest {
    val note: Todo = todo()
    service.postTodos(note)

    val responseDelete = service.deleteTodos(note.id)
    assertEquals(204, responseDelete.status.value)

    val todosListResponse: TodosList = service.getTodos().body()
    assertEquals(listOf(), todosListResponse)
  }

  @Test
  fun `should be able to delete one todo note and save another`() = runTest {
    val todoNotes = listOf(todo(), todo())
    for (note in todoNotes) {
      assertEquals(201, service.postTodos(note).status.value)
    }

    val response = service.deleteTodos(todoNotes[1].id)
    assertEquals(204, response.status.value)

    val todosListResponse: TodosList = service.getTodos().body()
    assertEquals(listOf(todoNotes[0]), todosListResponse)
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
  fun `should be able to modify a todo note text`() = runTest {
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
  fun `should be able to apply offset on received notes`() = runTest {
    todoNotes.forEach { (_, v) ->
      val response = service.postTodos(v)
      assertEquals(201, response.status.value)
    }

    val todosListResponse: TodosList = service.getTodos(offset = 4).body()
    assertEquals(listOf(
      todoNotes["sing"],
      todoNotes["feed"],
      todoNotes["cookies"]
    ), todosListResponse)
  }

  @Test
  fun `should be able to apply limit on received notes`() = runTest {
    todoNotes.forEach { (_, v) ->
      val response = service.postTodos(v)
      assertEquals(201, response.status.value)
    }

    val todosListResponse: TodosList = service.getTodos(limit = 1).body()
    assertEquals(listOf(todoNotes["swim"]), todosListResponse)
  }


  @Test
  fun `should be able to apply limit and offset on received notes`() = runTest {
    todoNotes.forEach { (_, v) ->
      val response = service.postTodos(v)
      assertEquals(201, response.status.value)
    }

    val todosListResponse: TodosList = service.getTodos(offset = 3, limit = 2).body()
    assertEquals(listOf(todoNotes["cleanup"], todoNotes["sing"]), todosListResponse)
  }

}
