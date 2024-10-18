package dev.emirman.mp.core.config.security.manager

import dev.emirman.mp.core.exception.auth.PasswordNotMatched
import dev.emirman.mp.core.exception.auth.UnAuthorized
import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.service.user.UserService
import net.lubble.util.GZipUtil
import net.lubble.util.JWTTool
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthManager(val tool: JWTTool, val service: UserService, val encoder: PasswordEncoder) : AuthenticationManager {
    fun cookieAuth(hashedValue: String): Authentication {
        val decoded = Base64.getDecoder().decode(hashedValue)
        val token = GZipUtil.decompress(decoded)
        return tokenAuth(token)
    }

    fun tokenAuth(token: String): Authentication {
        val email = tool.subject(token)
        val user = service.loadUserByUsername(email)
        val auth = UsernamePasswordAuthenticationToken(user, token, user.authorities)
        return authenticate(auth)
    }

    fun basicAuth(identifier: String, password: String): Authentication {
        val user = service.loadUserByUsername(identifier) as User

        if (!user.matchPassword(password, encoder)) throw PasswordNotMatched()

        val token = tool.generate(
            user.username,
            mapOf("email" to user.username)
        )
        val auth = UsernamePasswordAuthenticationToken(user, token, user.authorities)
        return authenticate(auth)
    }

    override fun authenticate(authentication: Authentication?): Authentication {
        if (authentication == null) throw UnAuthorized()
        val context: SecurityContext = SecurityContextHolder.createEmptyContext()
        context.authentication = authentication
        SecurityContextHolder.setContext(context)
        return authentication
    }
}