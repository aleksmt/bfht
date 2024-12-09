package com.bfht.tests.todos

import com.bfht.tests.SetupTests
import commons.models.Todo
import commons.models.TodosList
import io.ktor.client.call.body
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

/**
 * This is Todos Manager DELETE test suite
 */
class DeleteTodoTests : SetupTests() {

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
    fun `should be able to delete one todo note and save another`() = runTest {
        val todoNotes = todos(2)
        for (note in todoNotes) {
            assertEquals(201, service.postTodos(note).status.value)
        }

        val response = service.deleteTodos(todoNotes[1].id)
        assertEquals(204, response.status.value)

        val todosListResponse: TodosList = service.getTodos().body()
        assertEquals(listOf(todoNotes[0]), todosListResponse)
    }

    @Test
    fun `should delete all todo notes`() = runTest {
        val todoNotes = todos(3)
        for (note in todoNotes) {
            assertEquals(201, service.postTodos(note).status.value)
        }

        for (note in todoNotes) {
            assertEquals(204, service.deleteTodos(note.id).status.value)
        }

        val todosListResponse: TodosList = service.getTodos().body()
        assertEquals(listOf(), todosListResponse)
    }

    @Test
    fun `wrong delete of non-existent todo = error`() = runTest {
        val response = service.deleteTodos(999)
        assertEquals(404, response.status.value)
    }

    @Test
    fun `wrong basic auth delete todo = error`() = runTest {
        val todo = todo()
        service.postTodos(todo)

        val response = service.deleteTodos(todo.id, "wrong", "wrong")
        assertEquals(401, response.status.value)
    }

}
