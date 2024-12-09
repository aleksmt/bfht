package commons

import com.typesafe.config.Config
import com.typesafe.config.ConfigException
import com.typesafe.config.ConfigFactory

object Config {
    private val config: Config = try {
        ConfigFactory.load("application.conf")
    } catch (e: ConfigException.Missing) {
        throw IllegalStateException("Configuration file 'application.conf' not found in resources", e)
    }

    // URL components
    val proto: String = config.getString("service.proto")
    val host: String = config.getString("service.host")
    val port: Int = config.getInt("service.port")

    // Endpoints
    val wsEndpoint: String = config.getString("service.endpoints.ws")
    val todoEndpoint: String = config.getString("service.endpoints.todo")

    // Basic auth credentials
    val username: String = config.getString("service.auth.username")
    val password: String = config.getString("service.auth.password")

    // Complex paths
    val serviceUrl: String = "$proto://$host:$port"
    val todoUrl: String = "$proto://$host:$port$todoEndpoint"

}

