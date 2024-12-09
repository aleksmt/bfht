package commons.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigException
import com.typesafe.config.ConfigFactory

object ConfigManager {
  private val config: Config = try {
    ConfigFactory.load("application.conf")
  } catch (e: ConfigException.Missing) {
    throw IllegalStateException("Configuration file 'application.conf' not found in resources", e)
  }

  val apiUrl: String = config.getString("service.restApiUrl")
  val wsHost: String = config.getString("service.wsHost")
  val gatlingHost: String = config.getString("service.gatlingHost")
  val username: String = config.getString("service.auth.username")
  val password: String = config.getString("service.auth.password")
}
