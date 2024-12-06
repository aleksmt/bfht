package commons.models

import java.math.BigInteger
import kotlinx.serialization.Serializable

typealias TodosList = List<Todo>

@Serializable
data class Todo(
  val id: Int,
  val text: String?,
  val completed: Boolean
)
