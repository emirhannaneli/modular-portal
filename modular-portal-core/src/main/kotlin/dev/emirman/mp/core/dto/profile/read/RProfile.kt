package dev.emirman.mp.core.dto.profile.read


data class RProfile(
    val id: Long,
    val name: String?,
    val surname: String?,
    val email: String,
)