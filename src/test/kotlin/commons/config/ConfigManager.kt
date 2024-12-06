package commons.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object ConfigManager {
  private val config: Config = ConfigFactory.load()

  val apiUrl: String = config.getString("service.restApiUrl")
  val wsUrl: String = config.getString("service.wsApiUrl")
  val username: String = config.getString("service.auth.username")
  val password: String = config.getString("service.auth.password")
}
