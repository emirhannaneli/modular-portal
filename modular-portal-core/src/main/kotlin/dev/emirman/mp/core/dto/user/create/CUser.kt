package dev.emirman.mp.core.dto.user.create

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class CUser(
    @NotBlank
    private val name: String,
    @NotBlank
    private val surname: String,
    @Email
    @NotBlank
    private val email: String,
    @NotBlank
    val password: String,
) {
    fun name(): String {
        return name.trim()
    }

    fun surname(): String {
        return surname.trim()
    }

    fun email(): String {
        return email.trim()
    }
}