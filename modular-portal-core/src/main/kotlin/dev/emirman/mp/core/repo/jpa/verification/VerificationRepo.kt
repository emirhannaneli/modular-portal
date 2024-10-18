package dev.emirman.mp.core.repo.jpa.verification

import dev.emirman.mp.core.model.verification.Verification
import net.lubble.util.repo.BaseJPARepo
import org.springframework.stereotype.Repository

@Repository
interface VerificationRepo : BaseJPARepo<Verification> {
}