package dev.emirman.mp.core.config.security

import dev.emirman.mp.core.config.app.AppConfig
import dev.emirman.mp.core.config.security.filter.AuthFilter
import dev.emirman.mp.core.config.security.manager.AuthManager
import dev.emirman.mp.core.exception.auth.AccessDenied
import dev.emirman.mp.core.exception.auth.UnAuthorized
import dev.emirman.mp.core.service.user.UserService
import net.lubble.util.CookieUtil
import net.lubble.util.Response
import net.lubble.util.enum.CookieType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfigurer(
    val config: AppConfig.Security,
    val filter: AuthFilter,
    val manager: AuthManager,
    val service: UserService,
    val util: CookieUtil,
    val encoder: PasswordEncoder
) {
    @Bean
    fun chain(security: HttpSecurity): SecurityFilterChain {
        security.csrf { it.disable() }
        security.formLogin { it.disable() }
        security.httpBasic { it.disable() }
        security.logout { it.disable() }
        security.cors { it.disable() }
        security.headers { configurer -> configurer.frameOptions { it.disable() } }

        security.authorizeHttpRequests {
            it.requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
            config.publicApis.forEach { api ->
                it.requestMatchers(HttpMethod.valueOf(api.method), api.path)
                    .permitAll()
            }
            it.anyRequest().authenticated()
        }

        security.addFilterAt(filter, UsernamePasswordAuthenticationFilter::class.java)

        security.authenticationManager(manager)
        security.authenticationProvider(provider())

        exceptionHandling(security)

        return security.build()
    }

    private fun exceptionHandling(security: HttpSecurity) {
        security.exceptionHandling { exception: ExceptionHandlingConfigurer<HttpSecurity?> ->
            exception
                .authenticationEntryPoint { _, response, ex ->
                    val e = UnAuthorized(ex.message)
                    val res = Response(e)

                    util.clear(CookieType.entries.map { it.value }.toTypedArray())

                    response?.let { res.servletHandler(it) }
                }
                .accessDeniedHandler { _, response, ex ->
                    val e = AccessDenied(ex.message)
                    val res = Response(e)

                    response?.let { res.servletHandler(it) }
                }
        }
    }

    fun provider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(service)
        authProvider.setPasswordEncoder(encoder)
        return authProvider
    }
}