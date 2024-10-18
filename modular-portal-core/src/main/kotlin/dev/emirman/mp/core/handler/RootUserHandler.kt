package dev.emirman.mp.core.handler

import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.service.user.UserService
import dev.emirman.mp.core.spec.user.UserSpec
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class RootUserHandler(val service: UserService, val encoder: PasswordEncoder) {
    @PostConstruct
    fun init() {
        val spec = UserSpec()
        spec.params.identifier = "root@root.com"

        if (service.exists(spec)) return

        val user = User()
        user.email = "root@root.com"
        user.setPassword("root", encoder)
        service.save(user)
    }
}