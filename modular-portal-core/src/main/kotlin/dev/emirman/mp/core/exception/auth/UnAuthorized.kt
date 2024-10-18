package dev.emirman.mp.core.exception.auth

import net.lubble.util.model.ExceptionModel
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.UNAUTHORIZED

class UnAuthorized(private val reason: String?) : RuntimeException(), ExceptionModel {
    constructor() : this(null)

    override fun code(): String {
        return "0x000"
    }

    override fun message(): String {
        return "auth.exception.unauthorized"
    }

    override fun status(): HttpStatus {
        return UNAUTHORIZED
    }

    fun details(): String? {
        return reason
    }
}