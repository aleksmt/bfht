import commons.config.ConfigManager
import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.constantUsersPerSec
import io.gatling.javaapi.core.CoreDsl.exec
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status

class TodoSimulation : Simulation() {

  private val at5Rps = constantUsersPerSec(5.0).during(10)
  private val at10Rps = constantUsersPerSec(10.0).during(10)
  private val at50Rps = constantUsersPerSec(50.0).during(10)
  private val at100Rps = constantUsersPerSec(100.0).during(10)

  private val todos = exec(
    http("todos load testing").post("/todos")
      .headers(mapOf("Content-Type" to "application/json"))
      .body(StringBody("""{"id": #{randomInt(1000000,9999999)}, "text": "Load testing", "completed": true}"""))
      .check(status().`is`(201))
  )

  private val loadTest = scenario("Todos load test").exec(todos)

  init {
    setUp(
      loadTest.injectOpen(listOf(at5Rps, at10Rps, at50Rps, at100Rps))
    ).protocols(http.baseUrl(ConfigManager.gatlingHost))
  }
}
