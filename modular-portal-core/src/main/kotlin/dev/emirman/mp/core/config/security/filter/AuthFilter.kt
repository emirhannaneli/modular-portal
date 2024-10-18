package dev.emirman.mp.core.config.security.filter


import dev.emirman.mp.core.config.security.manager.AuthManager
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.lubble.util.JWTTool
import net.lubble.util.enum.CookieType
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthFilter(val manager: AuthManager, val tool: JWTTool) : Filter {
    override fun doFilter(p0: ServletRequest?, p1: ServletResponse?, p2: FilterChain?) {
        if (p0 !is HttpServletRequest || p1 !is HttpServletResponse) throw UnsupportedOperationException()

        // Check for Authorization Header
        val authHeader = p0.getHeader("Authorization") ?: null
        val isValidAuthHeader = authHeader != null && authHeader.startsWith("Bearer ") && tool.verify(authHeader.substring(7))

        authHeader?.let {
            if (isValidAuthHeader) {
                val token = authHeader.substring(7)
                manager.tokenAuth(token)
            }
        }

        // Check for Cookie
        val cookie = p0.cookies?.find { it.name == CookieType.AUTHENTICATION.value }?.value
        val isValidCookie = cookie != null && Base64.getDecoder().decode(cookie).isNotEmpty()

        cookie?.let {
            if (isValidCookie)
                manager.cookieAuth(it)
        }
        p2?.doFilter(p0, p1)
    }
}