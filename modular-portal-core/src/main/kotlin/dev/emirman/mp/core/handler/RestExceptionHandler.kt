package dev.emirman.mp.core.handler

import dev.emirman.mp.core.exception.auth.*
import dev.emirman.mp.core.exception.category.InfinityCategoryDetected
import net.lubble.util.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(UnAuthorized::class)
    fun handleUnAuthorized(e: UnAuthorized): ResponseEntity<Response> {
        return if (e.details() != null) {
            Response(e, e.details()!!).build()
        } else {
            Response(e).build()
        }
    }

    @ExceptionHandler(AccessDenied::class)
    fun handleAccessDenied(e: AccessDenied): ResponseEntity<Response> {
        return if (e.details() != null) {
            Response(e, e.details()!!).build()
        } else {
            Response(e).build()
        }
    }

    @ExceptionHandler(PasswordNotMatched::class)
    fun handlePasswordNotMatched(e: PasswordNotMatched): ResponseEntity<Response> {
        return Response(e).build()
    }

    @ExceptionHandler(EmailAlreadyExists::class)
    fun handleEmailAlreadyExists(e: EmailAlreadyExists): ResponseEntity<Response> {
        return Response(e, e.details()).build()
    }

    @ExceptionHandler(RegistrationDisabled::class)
    fun handleRegistrationDisabled(e: RegistrationDisabled): ResponseEntity<Response> {
        return Response(e).build()
    }

    @ExceptionHandler(InfinityCategoryDetected::class)
    fun handleInfinityCategoryDetected(e: InfinityCategoryDetected): ResponseEntity<Response> {
        return Response(e).build()
    }
}