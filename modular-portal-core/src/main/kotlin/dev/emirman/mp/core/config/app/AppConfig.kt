package dev.emirman.mp.core.config.app

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "app")
class AppConfig {
    lateinit var name: String
    lateinit var version: String
    lateinit var description: String
    lateinit var security: Security
    lateinit var jwt: Jwt


    @ConfigurationProperties(prefix = "app.security")
    class Security {
        var registrationEnabled: Boolean = true
        lateinit var publicApis: List<PublicPath>
    }

    class PublicPath {
        lateinit var path: String
        lateinit var method: String
    }

    @ConfigurationProperties(prefix = "app.jwt")
    class Jwt {
        lateinit var secret: String
        lateinit var issuer: String
        lateinit var expiration: Duration
        lateinit var audience: Array<String>
    }
}