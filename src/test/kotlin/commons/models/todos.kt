package commons.models

import java.math.BigInteger
import kotlinx.serialization.Serializable

typealias TodosList = List<Todo>

@Serializable
data class Todo(
  var id: Int? = (0..999999).random(),
  var text: String? = null,
  var completed: Boolean? = null
)

@Serializable
data class WsTodo(
  var data: Todo,
  var type: String
)
