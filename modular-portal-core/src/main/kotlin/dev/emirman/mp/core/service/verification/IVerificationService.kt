package dev.emirman.mp.core.service.verification

import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.model.verification.Verification
import dev.emirman.mp.core.repo.jpa.verification.VerificationRepo
import dev.emirman.mp.core.spec.verification.VerificationSpec
import net.lubble.util.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class IVerificationService(val repo: VerificationRepo) : VerificationService {
    override fun exits(user: User): Boolean {
        val spec = VerificationSpec()
        spec.params.userId = user.id
        val query = spec.ofSearch()
        return repo.exists(query)
    }

    override fun create(user: User): Verification {
        if (exits(user)) {
            val verification = find(user)
            destroy(verification)
        }
        val verification = Verification(user)
        return save(verification)
    }

    override fun save(verification: Verification): Verification {
        return repo.save(verification)
    }

    override fun find(user: User): Verification {
        val spec = VerificationSpec()
        spec.params.userId = user.id
        val query = spec.ofSearch()
        return repo.findOne(query).orElseThrow { NotFoundException("user-id", user.id) }
    }

    override fun verify(user: User, code: String): Boolean {
        val verification = find(user)

        if (!verification.verify(code)) return false

        destroy(verification)
        return true
    }

    override fun destroy(verification: Verification) {
        repo.delete(verification)
    }
}