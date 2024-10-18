package dev.emirman.mp.core.dto.auth

import jakarta.validation.constraints.NotBlank
import net.lubble.util.ReFormat


class SignUp {
    var name: String? = null

    var surname: String? = null

    @NotBlank
    lateinit var email: String

    @NotBlank
    lateinit var password: String

    @NotBlank
    lateinit var confirmPassword: String

    fun format() {
        name = name?.let { ReFormat(it).trim().format() }
        surname = surname?.let { ReFormat(it).trim().format() }
        email = ReFormat(email).trim().lower().format()
    }
}