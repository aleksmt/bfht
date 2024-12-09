package com.bfht.tests.todos

import com.bfht.tests.SetupTests
import commons.models.Todo
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest

/**
 * This is Todos Manager WS endpoint tests
 */
class WsTests : SetupTests() {

    @BeforeTest
    fun setUpWs(): Unit = runBlocking {
        ws.listen()
        delay(1000)
    }

    @Test
    fun `should receive event from websocket once new todo created`() = runTest {
        service.postTodos(todoNotes["workout"])

        val messages = ws.awaitMessagesWs(1)

        assertTrue(messages.isNotEmpty())
        assertEquals(todoNotes["workout"], messages[0].data)
    }

    @Test
    fun `should receive two events from websocket once new todos created`() = runTest {
        service.postTodos(todoNotes["workout"])
        service.postTodos(todoNotes["feed"])

        val messages = ws.awaitMessagesWs(2)

        assertTrue(messages.isNotEmpty())
        assertEquals(todoNotes["workout"], messages[0].data)
        assertEquals(todoNotes["feed"], messages[1].data)
    }

    @Test
    fun `should not receive two events from websocket when modified`() = runTest {
        val todo = mapOf(
            "not_completed" to Todo(id = 5, text = "cleanup room", completed = false),
            "completed" to Todo(id = 5, text = "cleanup room", completed = true),
        )
        service.postTodos(todo["not_completed"])
        service.putTodos(todo["completed"])

        val messages = ws.awaitMessagesWs(1)
        assertTrue(messages.isNotEmpty())
        assertEquals(todo["not_completed"], messages[0].data)
    }

    @AfterTest
    fun tearDownWs(): Unit = runBlocking {
        ws.stopListening()
    }

}
