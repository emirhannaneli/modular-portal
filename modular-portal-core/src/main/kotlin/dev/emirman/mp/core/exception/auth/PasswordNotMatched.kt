package dev.emirman.mp.core.exception.auth

import net.lubble.util.model.ExceptionModel
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST

class PasswordNotMatched : RuntimeException(), ExceptionModel {
    override fun code(): String {
        return "0x000"
    }

    override fun message(): String {
        return "auth.exception.password.not.matched"
    }

    override fun status(): HttpStatus {
        return BAD_REQUEST
    }
}