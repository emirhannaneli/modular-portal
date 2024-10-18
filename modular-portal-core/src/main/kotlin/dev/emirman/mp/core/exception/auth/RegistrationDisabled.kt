package dev.emirman.mp.core.exception.auth

import net.lubble.util.model.ExceptionModel
import org.springframework.http.HttpStatus

class RegistrationDisabled : RuntimeException(), ExceptionModel {
    override fun code(): String {
        return "0x000"
    }

    override fun message(): String {
        return "auth.exception.registration.disabled"
    }

    override fun status(): HttpStatus {
        return HttpStatus.BAD_REQUEST
    }
}