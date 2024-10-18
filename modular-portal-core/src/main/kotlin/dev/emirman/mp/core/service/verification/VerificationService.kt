package dev.emirman.mp.core.service.verification

import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.model.verification.Verification

interface VerificationService {
    fun exits(user: User): Boolean

    fun create(user: User): Verification

    fun save(verification: Verification): Verification

    fun find(user: User): Verification

    fun verify(user: User, code: String): Boolean

    fun destroy(verification: Verification)
}