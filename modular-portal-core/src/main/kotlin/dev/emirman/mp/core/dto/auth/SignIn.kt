package dev.emirman.mp.core.dto.auth

import jakarta.validation.constraints.NotBlank
import net.lubble.util.ReFormat


class SignIn {
    @NotBlank
    lateinit var identifier: String

    @NotBlank
    lateinit var password: String

    fun format() {
        identifier = ReFormat(identifier).trim().lower().format()
    }
}