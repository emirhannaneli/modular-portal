package dev.emirman.mp.core.config.jwt

import dev.emirman.mp.core.config.app.AppConfig
import net.lubble.util.JWTTool
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class JWTConfig(val config: AppConfig) {
    @Bean
    fun jwtTool(): JWTTool {
        val jwt = config.jwt
        return JWTTool(
            secret = jwt.secret,
            issuer = jwt.issuer,
            expiration = jwt.expiration.toMillis(),
            audience = jwt.audience
        )
    }
}