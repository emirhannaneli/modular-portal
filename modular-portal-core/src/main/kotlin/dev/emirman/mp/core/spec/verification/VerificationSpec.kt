package dev.emirman.mp.core.spec.verification

import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.model.verification.Verification
import net.lubble.util.spec.BaseSpec
import org.springframework.data.jpa.domain.Specification

class VerificationSpec(val params: Verification.Params) : BaseSpec(params), BaseSpec.JPAModel<Verification> {
    constructor() : this(Verification.Params())

    override fun ofSearch(): Specification<Verification> {
        return Specification { root, query, builder ->
            var predicate = defaultPredicates(root, query, builder, params)

            params.userId?.let {
                val join = root.join<Verification, User>("user")
                predicate = builder.and(predicate, builder.equal(join.get<Long>("id"), it))
            }

            predicate
        }
    }
}