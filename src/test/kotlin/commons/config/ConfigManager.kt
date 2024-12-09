package commons.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object ConfigManager {
  private val config: Config = ConfigFactory.load()

  val apiUrl: String = config.getString("service.restApiUrl")
  val wsHost: String = config.getString("service.wsHost")
  val username: String = config.getString("service.auth.username")
  val password: String = config.getString("service.auth.password")
}
