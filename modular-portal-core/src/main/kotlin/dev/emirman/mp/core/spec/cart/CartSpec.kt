package dev.emirman.mp.core.spec.cart

import dev.emirman.mp.core.model.cart.Cart
import dev.emirman.mp.core.model.user.User
import net.lubble.util.spec.BaseSpec
import org.springframework.data.jpa.domain.Specification

class CartSpec(val params: Cart.Params) : BaseSpec(params), BaseSpec.JPAModel<Cart> {
    constructor() : this(Cart.Params())

    override fun ofSearch(): Specification<Cart> {
        return Specification { root, query, builder ->
            var predicate = defaultPredicates(root, query, builder, params)

            params.userId?.let {
                val join = root.join<Cart, User>("user")
                predicate = builder.and(predicate, idPredicate(builder, join, it))
            }

            predicate
        }
    }

}