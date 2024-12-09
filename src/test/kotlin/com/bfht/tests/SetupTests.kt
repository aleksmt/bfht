package com.bfht.tests

import com.bfht.tests.todos.PostTodoTests
import commons.http.TodoManagerServiceClient
import commons.http.TodoManagerWebSocketClient
import commons.models.Todo
import commons.models.TodosList
import io.ktor.client.call.body
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Global setup/teardown and shareable methods
 */
open class SetupTests {

    protected val service = TodoManagerServiceClient()
    protected val ws = TodoManagerWebSocketClient()
    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)

    protected val todoNotes = mapOf(
        "swim" to Todo(id = 1, text = "go swim", completed = false),
        "workout" to Todo(id = 2, text = "go workout", completed = true),
        "store" to Todo(id = 3, text = "go to store", completed = true),
        "cleanup" to Todo(id = 4, text = "cleanup a room", completed = false),
        "sing" to Todo(id = 5, text = "sing in a bathroom", completed = true),
        "feed" to Todo(id = 6, text = "feed a hamster", completed = false),
        "cookies" to Todo(id = 7, text = "make cookies", completed = true)
    )

    private val availableTodos = todoNotes.values.toMutableSet()

    /**
     * Ideally, we should not relay on external APIs to prepare initial state of the app,
     * but since there is only two options:
     * either docker restart every time or remote via DELETE
     *
     * I've decided to use the second option, but ideally we need to remove all messages by using some other way
     */
    @BeforeTest
    fun setUp(): Unit = runBlocking {
        val todoList: TodosList = service.getTodos().body()
        todoList.forEach { todo -> service.deleteTodos(todo.id) }
    }

    @AfterTest
    fun tearDown(): Unit = runBlocking {
        availableTodos.clear()
        availableTodos.addAll(todoNotes.values)
    }

    /**
     * Returns a unique Todo object.
     * Throws an exception if all Todos have already been used.
     */
    protected fun todo(): Todo {
        if (availableTodos.isEmpty()) {
            throw IllegalStateException("No more unique Todos are available")
        }
        val todo = availableTodos.random()
        availableTodos.remove(todo)
        return todo
    }

    /**
     * Returns a list of unique Todo objects with the specified size.
     * Throws an exception if the requested number exceeds the available Todos.
     */
    protected fun todos(count: Int): List<Todo> {
        if (count > availableTodos.size) {
            throw IllegalArgumentException("Requested $count Todos, but only ${availableTodos.size} are available")
        }
        val todos = availableTodos.shuffled().take(count)
        availableTodos.removeAll(todos)
        return todos
    }

}
