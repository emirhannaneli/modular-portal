package dev.emirman.mp.core.controller.v1.account

import dev.emirman.mp.core.dto.auth.ChangePassword
import dev.emirman.mp.core.dto.profile.read.RProfile
import dev.emirman.mp.core.dto.profile.update.UProfile
import dev.emirman.mp.core.exception.auth.PasswordNotMatched
import dev.emirman.mp.core.mapper.profile.ProfileMapper
import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.service.user.UserService
import jakarta.validation.Valid
import net.lubble.util.AuthTool
import net.lubble.util.Response
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v1/account")
class AccountController(val service: UserService, val encoder: PasswordEncoder) : ProfileMapper {
    @GetMapping
    fun profile(): RProfile {
        val current = AuthTool.principal<User>()!!
        return map(current)
    }

    @PutMapping
    fun updateProfile(@RequestBody @Valid update: UProfile): ResponseEntity<Response> {
        update.format()

        val current = AuthTool.principal<User>()!!
        map(update, current)
        service.save(current)

        return Response(
            message = "account.messages.profile.updated",
        ).build()
    }

    @PutMapping("password")
    fun updatePassword(@RequestBody @Valid changePassword: ChangePassword): ResponseEntity<Response> {
        val current = AuthTool.principal<User>()!!

        if (!current.matchPassword(changePassword.oldPassword, encoder)) throw PasswordNotMatched()
        if (changePassword.password != changePassword.confirmPassword) throw PasswordNotMatched()

        current.setPassword(changePassword.password, encoder)

        return Response(
            message = "account.messages.password.updated",
        ).build()
    }

    @DeleteMapping
    fun deleteAccount(): ResponseEntity<Response> {
        val current = AuthTool.principal<User>()!!
        service.delete(current)

        return Response(
            message = "account.messages.account.deleted",
        ).build()
    }
}