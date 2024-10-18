package dev.emirman.mp.addon.multiple.locale.spec.product

import dev.emirman.mp.addon.multiple.locale.model.product.MLProduct
import dev.emirman.mp.core.model.product.Product
import jakarta.persistence.criteria.JoinType
import net.lubble.util.spec.BaseSpec
import org.springframework.data.jpa.domain.Specification
import java.util.*

class MLProductSpec(val params: MLProduct.Params) : BaseSpec(params), BaseSpec.JPAModel<MLProduct> {
    constructor() : this(MLProduct.Params())

    override fun ofSearch(): Specification<MLProduct> {
        return Specification { root, _, builder ->
            var predicates = defaultPredicates(root, builder, params)

            params.locale?.let {
                predicates = builder.and(predicates, builder.equal(root.get<Locale>("locale"), Locale.forLanguageTag(it)))
            }

            params.productId?.let {
                val join = root.join<MLProduct, Product>("product", JoinType.LEFT)
                predicates = idPredicate(predicates, builder, join, it)
            }

            predicates
        }
    }
}