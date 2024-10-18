package dev.emirman.mp.core.spec.user

import dev.emirman.mp.core.model.user.User
import net.lubble.util.spec.BaseSpec
import org.springframework.data.jpa.domain.Specification

class UserSpec(val params: User.Params) : BaseSpec(params), BaseSpec.JPAModel<User> {
    constructor() : this(User.Params())

    override fun ofSearch(): Specification<User> {
        return Specification { root, query, builder ->
            var predicates = defaultPredicates(root, query, builder, params)

            params.email?.let {
                predicates = builder.and(predicates, builder.equal(builder.lower(root.get("email")), it))
            }

            params.identifier?.let {
                predicates = builder.and(predicates, builder.equal(builder.lower(root.get("email")), it))
            }

            params.search?.let {
                val searchPredicate = searchPredicate(predicates, builder, root, it, "name", "surname", "email")
                predicates = builder.and(predicates, searchPredicate)
            }

            predicates
        }
    }


}