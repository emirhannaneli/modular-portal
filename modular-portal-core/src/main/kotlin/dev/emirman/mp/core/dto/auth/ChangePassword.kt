package dev.emirman.mp.core.dto.auth

import jakarta.validation.constraints.NotBlank

class ChangePassword {
    @NotBlank
    lateinit var oldPassword: String

    @NotBlank
    lateinit var password: String

    @NotBlank
    lateinit var confirmPassword: String
}