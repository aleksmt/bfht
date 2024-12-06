package com.bfht.tests.todos

import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation

/**
 * This is Todos Manager happy-path test suite with one goal:
 * perform task create, modify, list and remove cycle in one procedure (+incl. /ws listener),
 * thus tests should depend on each other
 */
@TestMethodOrder(OrderAnnotation::class)
class HappyPathTests {

}
