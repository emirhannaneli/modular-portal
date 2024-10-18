package dev.emirman.mp.core.controller.v1.auth

import com.fasterxml.jackson.databind.ObjectMapper
import dev.emirman.mp.core.config.app.AppConfig
import dev.emirman.mp.core.config.security.manager.AuthManager
import dev.emirman.mp.core.dto.auth.SignIn
import dev.emirman.mp.core.dto.auth.SignUp
import dev.emirman.mp.core.dto.profile.read.RProfile
import dev.emirman.mp.core.mapper.profile.ProfileMapper
import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.service.user.UserService
import dev.emirman.mp.core.spec.user.UserSpec
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import net.lubble.util.CookieUtil
import net.lubble.util.Generator
import net.lubble.util.OAuthTool
import net.lubble.util.Response
import net.lubble.util.enum.CookieType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("v1/auth")
class AuthController(
    val manager: AuthManager,
    val tool: OAuthTool,
    val service: UserService,
    val util: CookieUtil,
    val config: AppConfig.Jwt,
    val mapper: ObjectMapper,
    val request: HttpServletRequest,
    val response: HttpServletResponse,
) : ProfileMapper {
    @PostMapping("sign-up")
    fun signUp(@RequestBody @Valid signUp: SignUp): ResponseEntity<Response> {
        signUp.format()

        val user = service.signUp(signUp)
        val profile = map(user)
        val auth = manager.basicAuth(signUp.email, signUp.password)
        val token = auth.credentials as String


        val locale = request.locale
        user.locale = locale
        service.save(user)

        placeCookies(profile, token)

        return Response(
            message = "auth.messages.sign-up.success",
            details = mapOf(
                "profile" to profile,
                "token" to token
            )
        ).build()
    }

    @PostMapping("sign-in")
    fun signInBasic(@RequestBody @Valid signIn: SignIn): ResponseEntity<Response> {
        signIn.format()

        val auth = manager.basicAuth(signIn.identifier, signIn.password)
        val token = auth.credentials as String
        val user = auth.principal as User
        val profile = map(user)

        user.lastLogin = Date()
        service.save(user)

        placeCookies(profile, token)

        return Response(
            message = "auth.messages.sign-in.success",
            details = mapOf(
                "profile" to profile,
                "token" to token
            )
        ).build()
    }

    @PostMapping("oauth2/google")
    fun signInGoogle(@RequestParam credentials: String): ResponseEntity<Response> {
        val data = tool.retrieveGoogleData(credentials)

        val spec = UserSpec()
        spec.params.email = data["email"] as String
        val exists = service.exists(spec)

        lateinit var user: User
        lateinit var auth: Authentication
        lateinit var profile: RProfile
        lateinit var token: String

        if (!exists) {
            user = service.signUp(
                SignUp().apply {
                    name = data["givenName"] as String
                    surname = data["familyName"] as String
                    email = data["email"] as String
                    password = Generator.password(
                        numbers = true,
                        upper = true,
                        special = true,
                        length = 16
                    )
                    confirmPassword = password
                }
            )
            profile = map(user)
            auth = manager.basicAuth(user.email, user.password)
            token = auth.credentials as String
        } else {
            user = service.find(spec)
            profile = map(user)
            auth = manager.basicAuth(user.email, user.password)
            token = auth.credentials as String
        }

        user.lastLogin = Date()
        service.save(user)

        placeCookies(profile, token)

        return Response(
            message = "auth.messages.sign-in.success",
            details = mapOf(
                "profile" to profile,
                "token" to token
            )
        ).build()
    }

    @PostMapping("sign-out")
    fun signOut(): ResponseEntity<Response> {
        util.clearAll()

        return Response(
            message = "auth.messages.sign-out.success"
        ).build()
    }

    @PostMapping("forgot-password")
    fun forgotPassword(): ResponseEntity<Response> {
        TODO()
    }

    @PostMapping("reset-password")
    fun resetPassword(): ResponseEntity<Response> {
        TODO()
    }

    private fun placeCookies(profile: RProfile, token: String) {
        val authCookie = util.create(
            name = CookieType.AUTHENTICATION.value,
            value = token,
            maxAge = config.expiration.toMillisPart(),
            path = "/api",
            secure = true,
            httpOnly = false,
            gzip = true
        )

        val profileCookie = util.create(
            name = CookieType.PROFILE.value,
            value = mapper.writeValueAsString(profile),
            maxAge = config.expiration.toMillisPart(),
            path = "/",
            secure = true,
            httpOnly = false,
            gzip = true
        )

        response.addCookie(authCookie)
        response.addCookie(profileCookie)
    }
}