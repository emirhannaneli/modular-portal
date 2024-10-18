package dev.emirman.mp.core.spec.discount

import dev.emirman.mp.core.model.discount.Discount
import dev.emirman.mp.core.model.product.Product
import dev.emirman.mp.core.model.user.User
import jakarta.persistence.criteria.JoinType
import net.lubble.util.spec.BaseSpec
import org.springframework.data.jpa.domain.Specification

class DiscountSpec(val params: Discount.Params) : BaseSpec(params), BaseSpec.JPAModel<Discount> {
    constructor() : this(Discount.Params())

    override fun ofSearch(): Specification<Discount> {
        return Specification { root, query, builder ->
            var predicates = defaultPredicates(root, query, builder, params)

            params.productId?.let {
                val join = root.joinSet<Discount, Product>("products", JoinType.LEFT)
                predicates = builder.and(predicates, idPredicate(builder, join, it))
            }

            params.userId?.let {
                val join = root.joinSet<Discount, User>("users", JoinType.LEFT)
                predicates = builder.and(predicates, idPredicate(builder, join, it))
            }

            params.allProducts?.let {
                predicates = builder.and(predicates, builder.equal(root.get<Boolean>("allProducts"), it))
            }

            params.allUsers?.let {
                predicates = builder.and(predicates, builder.equal(root.get<Boolean>("allUsers"), it))
            }

            params.startDate?.let {
                predicates = builder.and(predicates, builder.greaterThanOrEqualTo(root.get("startDate"), it))
            }

            params.endDate?.let {
                predicates = builder.and(predicates, builder.lessThanOrEqualTo(root.get("endDate"), it))
            }

            params.always?.let {
                predicates = builder.and(predicates, builder.equal(root.get<Boolean>("always"), it))
            }

            params.active?.let {
                predicates = builder.and(predicates, builder.equal(root.get<Boolean>("active"), it))
            }

            predicates
        }
    }
}